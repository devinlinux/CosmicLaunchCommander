package com.michaelb.clc.util;

//  imports
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.FontFormatException;

import java.nio.file.Path;
import java.nio.file.Files;

import java.io.IOException;
import java.io.File;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URI;

public final class IOUtils {
    public static final char SEP = java.io.File.separatorChar;
    
    private static final int MAX_INTERNET_ATTEMPTS = 3;
    private static final int INTERNET_RETRY_INTERVAL = 200;

    private static final String NOIZE_SPORT_FONT_PATH = "src%smain%sresources%sNoizeSportRegular.ttf".formatted(SEP, SEP, SEP);

    public static void readNoizeSportRegularFont() {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            env.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(NOIZE_SPORT_FONT_PATH)));
        } catch (IOException | FontFormatException e) {
            Logger.err("Failed to read Noize Sport Regular", "IOUtils::readNoizeSportRegularFont");
        }
    }

    public static void readNecessaryFiles() {
        readNoizeSportRegularFont();
    }

    private static boolean isInternetAvailable() {
        for (int attempt = 1; attempt <= MAX_INTERNET_ATTEMPTS; attempt++) {
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
}
