package com.michaelb.clc.util;

//  imports
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;

import java.nio.ByteBuffer;
import java.nio.file.Path;
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

    private static final int NUM_THREADS = 20;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    public static final char SEP = java.io.File.separatorChar;

    private static final int MAX_INTERNET_ACCESS_ATTEMPTS = 3;
    private static final int INTERNET_RETRY_INTERVAL = 200;

    private static final long SHUTDOWN_RETRY_INTERVAL = 800;

    private static final String NOIZE_SPORT_FONT_PATH = "src%smain%sresources%sNoizeSportRegular.ttf".formatted(SEP, SEP, SEP);

    private static void downloadFile(final String downloadLink, final String destinationPath, final String filename) {
        if (!isInternetAvailable()) {
            Logger.err("Cannot reach internet, cannot download %s".formatted(filename), "IOUtils::downloadFile");
            return;
        }

        try {
            URL url = new URI(downloadLink).toURL();
            Path destination = Path.of(destinationPath);

            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(destination, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

            CompletionHandler<Integer, Void> completionHandler = new CompletionHandler<Integer,Void>() {
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

            Callable<Boolean> downloadTask = () -> {
                try {
                    fileChannel.write(buffer, 0, null, completionHandler);
                    return true;
                } catch (Exception e) {
                    Logger.err("Error while downloading %s: %s".formatted(filename, e.getMessage()), "IOUtils::downloadFile");
                    return false;
                }
            };

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

    public static void readNoizeSportRegularFont() {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            env.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(NOIZE_SPORT_FONT_PATH)));
        } catch (IOException | FontFormatException e) {
            Logger.err("Failed to read Noize Sport Regular", "IOUtils::readNoizeSportRegularFont");
        }
    }

    public static void readFonts() {
        readNoizeSportRegularFont();
    }

    public static void checkForGameFiles() {

    }
}
