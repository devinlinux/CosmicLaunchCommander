package com.michaelb.clc.util;

//  imports
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.PrintStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    private static PrintStream outputStream;
    private static final List<String> logMessages;
    private static final Map<Level, Integer> logCountsMap;
    private static int logNumber;

    private static final String PREFERRED_LOG_DIRECTORY = com.michaelb.clc.util.IOUtils.LOGS_DIRECTORY_PATH;
    private static Path logFilePath;

    private static boolean INSTANCE = false;

    static {
        outputStream = null;
        logMessages = new ArrayList<>();
        logCountsMap = new HashMap<>();
        logNumber = 1;

        try {
            LocalDateTime currentTime = LocalDateTime.now();
            String fileName = STR."\{currentTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))}.log";
            Path dir = Paths.get(PREFERRED_LOG_DIRECTORY);
            if (!Files.exists(dir))
                Files.createDirectory(dir);
            logFilePath = Files.createFile(Path.of(PREFERRED_LOG_DIRECTORY + fileName));
        } catch (IOException e) {
            System.err.printf("Error creating log file: %s%n", e.getMessage());
        }
    }

    private Logger() {}

    public static void init() {
        if (!INSTANCE)
            INSTANCE = true;
        else
            throw new IllegalStateException("Logger has already been initialized");
    }

    public enum Level {
        PROGRESS(ANSI_GREEN),
        INFO(ANSI_BLUE),
        WARNING(ANSI_YELLOW),
        ERROR(ANSI_RED);

        private final String color;

        Level(final String color) {
            this.color = color;
        }
    }

    private static String[] formatLog(final Level level, final String message, final String cause) {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DATE_TIME_FORMATTER);

        String coloredLog = "%s    %d    %s    [%s%s%s]: %s%n".formatted(
                formattedTime,
                logNumber,
                cause,
                level.color,
                level.toString(),
                ANSI_RESET,
                message);
        String colorlessLog = "%s    %d    %s    [%s]: %s%n".formatted(
                formattedTime,
                logNumber++,
                cause,
                level.toString(),
                message);

        logMessages.add(colorlessLog);
        int logCount = logCountsMap.getOrDefault(level, 0) + 1;
        logCountsMap.put(level, logCount);

        return new String[] { coloredLog, colorlessLog };
    }

    private static void write(final String log) {
        try {
            Files.write(logFilePath, log.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.printf("Error logging to log file: %s%n", e.getMessage());
        }
    }

    public static void log(final Level level, final String message, final String cause) {
        String[] log = formatLog(level, message, cause);
        if (outputStream != null)
            outputStream.print(log[0]);
        write(log[1]);
    }

    public static void progress(final String message, final String cause) {
        log(Level.PROGRESS, message, cause);
    }

    public static void info(final String message, final String cause) {
        log(Level.INFO, message, cause);
    }

    public static void warn(final String message, final String cause) {
        log(Level.WARNING, message, cause);
    }

    public static void err(final String message, final String cause) {
        log(Level.ERROR, message, cause);
    }

    public static void reset() {
        info("Resetting logs", "Logger::reset");
        logNumber = 1;
        logCountsMap.clear();
    }

    public static int logCountOf(final Level level) {
        return logCountsMap.getOrDefault(level, 0);
    }

    public static void dump(final String path) {
        for (String logMessage : logMessages) {
            try {
                Files.write(Path.of(path), logMessage.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.err.printf("Error logging to log file: %s%n", e.getMessage());
            }
        }
    }

    public static void outputStream(final PrintStream stream) {
        outputStream = stream;
    }
}
