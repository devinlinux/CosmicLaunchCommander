package com.michaelb.clc.util;

//  imports
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.io.PrintStream;
import java.io.IOException;

import java.nio.file.Files;
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
    private static List<String> logMessages;
    private static Map<Level, Integer> logCountsMap;
    private static int logNumber;

    private static final String PREFERRED_LOG_DIRECTORY = "logs%s".formatted(com.michaelb.clc.util.IOUtils.SEP);
    private static Path logFilePath;

    static {
        outputStream = null;
        logMessages = new ArrayList<>();
        logCountsMap = new HashMap<>();
        logNumber = 1;

        try {
            LocalDateTime currentTime = LocalDateTime.now();
            String fileName = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".log";
            logFilePath = Files.createFile(Path.of(PREFERRED_LOG_DIRECTORY + fileName));
        } catch (IOException e) {
            System.err.printf("Error creating log file: %s%n", e.getMessage());
        }
    }

    public static enum Level {
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
                logNumber++,
                cause,
                level.color,
                level.toString(),
                ANSI_RESET,
                message);
        String colorlessLog = "%s    %d    %s    [%s]: %s%n".formatted(
                formattedTime,
                logNumber,
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
        log(Level.PROGRESS, cause, message);
    }

    public static void info(final String message, final String cause) {
        log(Level.INFO, cause, message);
    }

    public static void warn(final String message, final String cause) {
        log(Level.WARNING, cause, message);
    }

    public static void err(final String message, final String cause) {
        log(Level.ERROR, cause, message);
    }

    public static void reset() {
        info("Reseting logs", "Logger::reset");
        logNumber = 1;
        logCountsMap.clear();
    }

    public static int logCountOf(Level level) {
        return logCountsMap.getOrDefault(level, 0);
    }

    public static void dump(String path) {
        for (String logMessage : logMessages) {
            try {
                Files.write(Path.of(path), logMessage.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.err.printf("Error logging to log file: %s%n", e.getMessage());
            }
        }
    }

    public static void outputStream(PrintStream stream) {
        outputStream = stream;
    }
}
