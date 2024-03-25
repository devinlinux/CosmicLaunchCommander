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
import java.nio.channels.CompletionHandler;

import java.io.IOException;
import java.io.File;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URI;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
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
    private static final GameFile NOIZE_SPORT_REGULAR = new GameFile(Path.of(GAME_FONTS_DIRECTORY.path.toString() + SEP + "fonts/NoizeSportRegular.ttf"), "Noize Sport Regular", "https://github.com/devinlinux/CosmicLaunchCommander/raw/master/src/main/resources/fonts/NoizeSportRegular.ttf");

    /* utility methods */

    private static void downloadFile(final String downloadLink, final String destinationPath, final String filename) {
        if (!isInternetAvailable()) {
            Logger.err("Cannot reach internet, cannot download %s".formatted(filename), "IOUtils::downloadFile");
            return;
        }

        try {
            URL url = new URI(downloadLink).toURL();
            Path destination = Path.of(destinationPath);

            Callable<Boolean> downloadTask;
            try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(destination, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE)) {
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

                CompletionHandler<Integer, Void> completionHandler = new CompletionHandler<Integer, Void>() {
                    @Override
                    public void completed(Integer result, Void attachment) {
                        if (result == -1) {
                            try {
                                fileChannel.close();
                                Logger.info("Successfully downloaded %s".formatted(filename), "IOUtils::downloadFile");
                            } catch (IOException e) {
                                Logger.err("Error while downloading %s: %s".formatted(filename, e.getMessage()), "IOUtils::downloadFile");
                            }
                        } else {
                            buffer.flip();
                            fileChannel.write(buffer, 0, null, this);
                            buffer.clear();
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        Logger.err("Error while downloading %s: %s".formatted(filename, exc.getMessage()), "IOUtils::downloadFile");
                    }
                };

                downloadTask = () -> {
                    try {
                        fileChannel.write(buffer, 0, null, completionHandler);
                        return true;
                    } catch (Exception e) {
                        Logger.err("Error while downloading %s: %s".formatted(filename, e.getMessage()), "IOUtils::downloadFile");
                        return false;
                    }
                };
            }

            Future<Boolean> future = executorService.submit(downloadTask);
            future.get();

            Logger.info("Successfully downloaded %s".formatted(filename), "IOUtils::downloadFile");
        } catch (URISyntaxException | IOException | InterruptedException | ExecutionException e) {
            Logger.err("Error while downloading %s: %s".formatted(filename, e.getMessage()), "IOUtils::downloadFile");
        }
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

    private static void gameResourcesDirectory() {
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
    }

    /* directories */

    private static void checkForGameResourcesDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_RESOURCES_DIRECTORY);
    }

    private static void checkForGameFontsDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_FONTS_DIRECTORY);
    }

    private static void checkForGameImagesDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_IMAGES_DIRECTORY);
    }

    private static void checkForGameSoundsDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_SOUNDS_DIRECTORY);
    }

    private static void checkForGameMusicDirectoryAndCreateIfNotFound() {
        createDirectoryIfNotFound(GAME_MUSIC_DIRECTORY);
    }

    /* files */

    private static void checkForImageIconAndDownloadIfNotFound() {
        if (Files.exists(IMAGE_ICON.path))
            Logger.info("Image icon already exists", "IOUtils::checkForImageIconAndDownloadIfNotFound");
        else
            downloadFile(IMAGE_ICON.downloadLink, IMAGE_ICON.path.toString(), IMAGE_ICON.name);
    }

    private static void checkForNoizeSportRegularFontAndDownloadIfNotFound() {
        if (Files.exists(NOIZE_SPORT_REGULAR.path))
            Logger.info("Noize Sport Regular font already exists", "IOUtils::checkForNoizeSportRegularFontAndDownloadIfNotFound");
        else
            downloadFile(NOIZE_SPORT_REGULAR.downloadLink, NOIZE_SPORT_REGULAR.path.toString(), NOIZE_SPORT_REGULAR.name);
    }

    /* fonts */

    public static void readNoizeSportRegularFont() {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            env.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(NOIZE_SPORT_REGULAR.path.toString())));
        } catch (IOException | FontFormatException e) {
            Logger.err("Failed to read Noize Sport Regular", "IOUtils::readNoizeSportRegularFont");
        }
    }

    /* driver methods */

    public static void checkForGameDirectories() {
        checkForGameResourcesDirectoryAndCreateIfNotFound();
        checkForGameFontsDirectoryAndCreateIfNotFound();
        checkForGameImagesDirectoryAndCreateIfNotFound();
        checkForGameSoundsDirectoryAndCreateIfNotFound();
        checkForGameMusicDirectoryAndCreateIfNotFound();
    }

    public static void readFonts() {
        readNoizeSportRegularFont();
    }

    public static void checkForGameFiles() {
        checkForGameDirectories();
        readFonts();

        checkForImageIconAndDownloadIfNotFound();
        checkForNoizeSportRegularFontAndDownloadIfNotFound();

        shutdownExecutorService();
    }
}
