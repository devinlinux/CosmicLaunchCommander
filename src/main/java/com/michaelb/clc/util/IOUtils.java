package com.michaelb.clc.util;

//  imports
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URI;

public final class IOUtils {

    private IOUtils() {}

    /* symbolic constants */

    public static final char SEP = java.io.File.separatorChar;

    private static final int MAX_INTERNET_ACCESS_ATTEMPTS = 3;
    private static final int INTERNET_RETRY_INTERVAL = 200;

    /* job counter */
    private static int completedJobs = 0;
    private static final int JOBS = 12;

    private static final String GAME_DIRECTORY = STR."\{System.getProperty("user.home")}\{SEP}.clc";

    /* utility records */
    private record GameDirectory(Path path, String name) {}
    private record GameFile(Path path, String name, String downloadLink) {}

    /* directories */

    private static final GameDirectory GAME_RESOURCES_DIRECTORY = new GameDirectory(Path.of(STR."\{GAME_DIRECTORY}\{SEP}resources"), "game resources");
    private static final GameDirectory GAME_FONTS_DIRECTORY = new GameDirectory(Path.of(STR."\{GAME_DIRECTORY}\{SEP}resources\{SEP}fonts"), "game fonts");
    private static final GameDirectory GAME_IMAGES_DIRECTORY = new GameDirectory(Path.of(STR."\{GAME_DIRECTORY}\{SEP}resources\{SEP}images"), "game images");
    private static final GameDirectory GAME_SOUNDS_DIRECTORY = new GameDirectory(Path.of(STR."\{GAME_DIRECTORY}\{SEP}resources\{SEP}sounds"), "game sounds");
    private static final GameDirectory GAME_MUSIC_DIRECTORY = new GameDirectory(Path.of(STR."\{GAME_DIRECTORY}\{SEP}resources\{SEP}music"), "game music");
    private static final GameDirectory GAME_LOGS_DIRECTORY = new GameDirectory(Path.of(STR."\{GAME_DIRECTORY}\{SEP}logs"), "game logs");

    /* files */

    private static final GameFile IMAGE_ICON = new GameFile(Path.of(STR."\{GAME_IMAGES_DIRECTORY.path.toString()}\{SEP}image_icon.png"), "image icon", "https://raw.githubusercontent.com/devinlinux/CosmicLaunchCommander/master/src/main/resources/images/image_icon.png");
    private static final GameFile SPLASH_SCREEN_BACKGROUND = new GameFile(Path.of(STR."\{GAME_IMAGES_DIRECTORY.path.toString()}\{SEP}splash.png"), "splash screen background", "https://raw.githubusercontent.com/devinlinux/CosmicLaunchCommander/master/src/main/resources/images/splash.png");
    private static final GameFile NASALIZATION_REGULAR = new GameFile(Path.of(STR."\{GAME_FONTS_DIRECTORY.path.toString()}\{SEP}nasalization-rg.otf"), "Nasalization Regular", "https://github.com/devinlinux/CosmicLaunchCommander/raw/master/src/main/resources/fonts/nasalization-rg.otf");

    /* directory paths */
    public static final String LOGS_DIRECTORY_PATH = GAME_LOGS_DIRECTORY.path.toString();

    /* file paths */
    public static final String IMAGE_ICON_PATH = IMAGE_ICON.path.toString();
    public static final String SPLASH_SCREEN_BACKGROUND_PATH = SPLASH_SCREEN_BACKGROUND.path.toString();

    /* utility methods */

    private static void downloadFile(GameFile file) {
        if (!isInternetAvailable()) {
            Logger.err("Cannot reach internet, cannot download %s".formatted(file.name), "IOUtils::downloadFile");
            return;
        }

        try {
            URL url = new URI(file.downloadLink).toURL();
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            try (FileOutputStream fos = new FileOutputStream(file.path.toString())) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                Logger.info("Successfully downloaded %s".formatted(file.name), "IOUtils::downloadFile");
            }
        } catch (URISyntaxException | IOException e) {
            Logger.err("Failed to download %s: %s".formatted(file.name, e.getMessage()), "IOUtils::downloadFile");
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

    private static void mkdir(final GameDirectory gameDirectory) {
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

        File programDirectory = new File(STR."\{homeDirectory}\{SEP}.clc");
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

    private static void ensureResourcesDir() {
        mkdir(GAME_RESOURCES_DIRECTORY);
        completedJobs++;
    }

    private static void ensureFontsDir() {
        mkdir(GAME_FONTS_DIRECTORY);
        completedJobs++;
    }

    private static void ensureImagesDir() {
        mkdir(GAME_IMAGES_DIRECTORY);
        completedJobs++;
    }

    private static void ensureSoundsDir() {
        mkdir(GAME_SOUNDS_DIRECTORY);
        completedJobs++;
    }

    private static void ensureMusicDir() {
        mkdir(GAME_MUSIC_DIRECTORY);
        completedJobs++;
    }

    private static void ensureLogsDir() {
        mkdir(GAME_LOGS_DIRECTORY);
        completedJobs++;
    }

    /* files */

    private static void verifyImageIcon() {
        if (Files.exists(IMAGE_ICON.path))
            Logger.info("Image icon already exists", "IOUtils::checkForImageIconAndDownloadIfNotFound");
        else
            downloadFile(IMAGE_ICON);
        completedJobs++;
    }

    private static void verifySplashBackground() {
        if (Files.exists(SPLASH_SCREEN_BACKGROUND.path))
            Logger.info("Splash screen background already exists", "IOUtils::checkForSplashScreenBackgroundAndDownloadIfNotFound");
        else
            downloadFile(SPLASH_SCREEN_BACKGROUND);
        completedJobs++;
    }

    private static void verifyNasalizationRg() {
        if (Files.exists(NASALIZATION_REGULAR.path))
            Logger.info("Nasalization Regular font already exists", "IOUtils::checkForNasalizationRegularFontAndDownloadIfNotFound");
        else
            downloadFile(NASALIZATION_REGULAR);
        completedJobs++;
    }

    /* fonts */

    private static void readNasalizationRg() {
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

    private static void ensureAllGameDirs() {
        ensureResourcesDir();
        ensureFontsDir();
        ensureImagesDir();
        ensureSoundsDir();
        ensureMusicDir();
        ensureLogsDir();
    }

    private static void readAllFonts() {
        readNasalizationRg();
        completedJobs++;
    }

    public static void checkForGameFiles() {
        makeGameResourcesDirectory();
        ensureAllGameDirs();

        verifyImageIcon();
        verifySplashBackground();
        verifyNasalizationRg();

        readAllFonts();
    }

    /* getters */

    public static int completedJobs() {
        return completedJobs;
    }

    public static int jobs() {
        return JOBS;
    }
}
