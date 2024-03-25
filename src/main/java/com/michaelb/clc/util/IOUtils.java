package com.michaelb.clc.util;

//  imports
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.channels.AsynchronousFileChannel;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URI;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class IOUtils {

    private IOUtils() {}

    /* symbolic constants */

    private static final int NUM_THREADS = 20;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    public static final char SEP = java.io.File.separatorChar;

    private static final int MAX_INTERNET_ACCESS_ATTEMPTS = 3;
    private static final int INTERNET_RETRY_INTERVAL = 200;

    private static final long SHUTDOWN_RETRY_INTERVAL = 800;

    /* job counter */
    private static int completedJobs = 0;
    private static final int JOBS = 9;

    private static final String GAME_DIRECTORY = System.getProperty("user.home") + SEP + ".clc";

    /* utility records */
    private record GameDirectory(Path path, String name) {}
    private record GameFile(Path path, String name, String downloadLink) {}


    /* directories */

    private static final GameDirectory GAME_RESOURCES_DIRECTORY = new GameDirectory(Path.of(GAME_DIRECTORY + SEP + "resources"), "game resources");
    private static final GameDirectory GAME_FONTS_DIRECTORY = new GameDirectory(Path.of(GAME_DIRECTORY + SEP + "resources" + SEP + "fonts"), "game fonts");
    private static final GameDirectory GAME_IMAGES_DIRECTORY = new GameDirectory(Path.of(GAME_DIRECTORY + SEP + "resources" + SEP + "images"), "game images");
    private static final GameDirectory GAME_SOUNDS_DIRECTORY = new GameDirectory(Path.of(GAME_DIRECTORY + SEP + "resources" + SEP + "sounds"), "game sounds");
    private static final GameDirectory GAME_MUSIC_DIRECTORY = new GameDirectory(Path.of(GAME_DIRECTORY + SEP + "resources" + SEP + "music"), "game music");

    /* files */

    private static final GameFile IMAGE_ICON = new GameFile(Path.of(GAME_IMAGES_DIRECTORY.path.toString() + SEP + "image_icon.png"), "image icon", "https://raw.githubusercontent.com/devinlinux/CosmicLaunchCommander/master/src/main/resources/images/image_icon.png");
    private static final GameFile NASALIZATION_REGULAR = new GameFile(Path.of(GAME_FONTS_DIRECTORY.path.toString() + SEP + "nasalization-rg.otf"), "Nasalization Regular", "https://github.com/devinlinux/CosmicLaunchCommander/raw/master/src/main/resources/fonts/nasalization-rg.otf");

    /* utility methods */

    private static void downloadFile(GameFile file) {
        if (!isInternetAvailable()) {
            Logger.err("Cannot reach internet, cannot download %s".formatted(file.name), "IOUtils::downloadFile");
            return;
        }

        executorService.submit(() -> {
            try {
                URL url = new URI(file.downloadLink).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    long fileSize = connection.getContentLengthLong();
                    ByteBuffer buffer = ByteBuffer.allocateDirect((int) Math.min(fileSize, Integer.MAX_VALUE));

                    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                            file.path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

                    Future<Integer> bytesRead = fileChannel.read(buffer, 0);
                    while (!bytesRead.isDone()) {
                        // Wait for the read operation to complete
                    }

                    int totalBytesRead = bytesRead.get();
                    int bytesRemaining = (int) (fileSize - totalBytesRead);

                    while (bytesRemaining > 0) {
                        buffer.clear();
                        bytesRead = fileChannel.read(buffer, totalBytesRead);
                        while (!bytesRead.isDone()) {
                            // Wait for the read operation to complete
                        }

                        int bytesReadThisTime = bytesRead.get();
                        totalBytesRead += bytesReadThisTime;
                        bytesRemaining -= bytesReadThisTime;
                    }

                    fileChannel.close();
                    Logger.info("Successfully downloaded %s".formatted(file.name), "IOUtils::downloadFile");
                } else {
                    Logger.err("Failed to download %s, response code: %d".formatted(file.name, responseCode), "IOUtils::downloadFile");
                }
            } catch (IOException | URISyntaxException | InterruptedException | ExecutionException e) {
                Logger.err("Failed to download %s: %s".formatted(file.name, e.getMessage()), "IOUtils::downloadFile");
            }
        });
    }

    private static void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(SHUTDOWN_RETRY_INTERVAL, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(SHUTDOWN_RETRY_INTERVAL, TimeUnit.MILLISECONDS))
                    Logger.err("Executor service did not terminate", "IOUtils::shutdownExecutorService");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static boolean isInternetAvailable() {
        for (int attempt = 1; attempt <= MAX_INTERNET_ACCESS_ATTEMPTS; attempt++) {
            try {
                URL url = new URI("https://gnu.org").toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    return true;
            } catch (IOException | URISyntaxException e) {
                Logger.warn("Attempt %d to reach internet failed".formatted(attempt), "IOUtils::isInternetAvailable");
            }

            try {
                Thread.sleep(INTERNET_RETRY_INTERVAL);
            } catch (InterruptedException e) {
                Logger.err("Error waiting to retry checking for internet availability", "IOUtils::isInternetAvailable");
            }
        }

        return false;
    }

    private static void createDirectoryIfNotFound(final GameDirectory gameDirectory) {
        if (Files.exists(gameDirectory.path))
            Logger.info("Directory %s already exists".formatted(gameDirectory.name), "IOUtils::createDirectoryIfNotFound");
        else {
            try {
                Files.createDirectories(gameDirectory.path);
                Logger.info("Successfully created directory %s".formatted(gameDirectory.name), "IOUtils::createDirectoryIfNotFound");
            } catch (IOException e) {
                Logger.err("Could not create directory %s: %s".formatted(gameDirectory.name, e.getMessage()), "IOUtils::createDirectoryIfNotFound");
            }
        }
    }

    private static void makeGameResourcesDirectory() {
        String homeDirectory = System.getProperty("user.home");

        File programDirectory = new File(homeDirectory + SEP + ".clc");
        if (!programDirectory.exists()) {
            if (programDirectory.mkdir())
                Logger.info("Created program directory", "IOUtils::gameResourcesDirectory");
            else
                Logger.err("Failed to create program directory", "IOUtils::gameResourcesDirectory");
        } else {
            Logger.info("Program directory already exists", "IOUtils::gameResourcesDirectory");
        }
        completedJobs++;
    }

    /* directories */

    private static void checkForGameResourcesDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_RESOURCES_DIRECTORY);
        completedJobs++;
    }

    private static void checkForGameFontsDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_FONTS_DIRECTORY);
        completedJobs++;
    }

    private static void checkForGameImagesDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_IMAGES_DIRECTORY);
        completedJobs++;
    }

    private static void checkForGameSoundsDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_SOUNDS_DIRECTORY);
        completedJobs++;
    }

    private static void checkForGameMusicDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_MUSIC_DIRECTORY);
        completedJobs++;
    }

    /* files */

    private static void checkForImageIconAndDownloadIfNotFound() {
        if (Files.exists(IMAGE_ICON.path))
            Logger.info("Image icon already exists", "IOUtils::checkForImageIconAndDownloadIfNotFound");
        else
            downloadFile(IMAGE_ICON);
        completedJobs++;
    }

    private static void checkForNasalizationRegularFontAndDownloadIfNotFound() {
        if (Files.exists(NASALIZATION_REGULAR.path))
            Logger.info("Nasalization Regular font already exists", "IOUtils::checkForNasalizationRegularFontAndDownloadIfNotFound");
        else
            downloadFile(NASALIZATION_REGULAR);
        completedJobs++;
    }

    /* fonts */

    private static void readNasalizationRegularFont() {
        //  read in the damn font, ensure there is not a buffer underflow exception
        try (InputStream is = new BufferedInputStream(new FileInputStream(NASALIZATION_REGULAR.path.toString()))) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            Logger.info("Successfully read in Nasalization Regular font", "IOUtils::readNasalizationRegularFont");
        } catch (IOException | FontFormatException e) {
            Logger.err("Failed to read in Nasalization Regular font: %s".formatted(e.getMessage()), "IOUtils::readNasalizationRegularFont");
        }
        completedJobs++;
    }

    /* driver methods */

    private static void checkForGameDirectories() {
        checkForGameResourcesDirectoryAndCreateIfNotFound();
        checkForGameFontsDirectoryAndCreateIfNotFound();
        checkForGameImagesDirectoryAndCreateIfNotFound();
        checkForGameSoundsDirectoryAndCreateIfNotFound();
        checkForGameMusicDirectoryAndCreateIfNotFound();
    }

    private static void readFonts() {
        readNasalizationRegularFont();
    }

    public static void checkForGameFiles() {
        makeGameResourcesDirectory();
        checkForGameDirectories();

        checkForImageIconAndDownloadIfNotFound();
        checkForNasalizationRegularFontAndDownloadIfNotFound();

        readFonts();

        shutdownExecutorService();
    }

    /* getters */

    public static int completedJobs() {
        return completedJobs;
    }

    public static int jobs() {
        return JOBS;
    }
}
