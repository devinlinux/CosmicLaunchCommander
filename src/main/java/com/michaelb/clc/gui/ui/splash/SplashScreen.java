package com.michaelb.clc.gui.ui.splash;

//  imports
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;

import com.michaelb.clc.gui.Frame;
import com.michaelb.clc.gui.Stage;
import com.michaelb.clc.util.IOUtils;
import com.michaelb.clc.util.Logger;

import static com.michaelb.clc.util.IOUtils.SPLASH_SCREEN_BACKGROUND_PATH;

public class SplashScreen extends JPanel {

    private final Frame context;

    public SplashScreen(final Frame context) {
        this.context = context;

        this.startMonitoring();
    }

    private void startMonitoring() {
        new MonitorWorker().execute();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            BufferedImage image = ImageIO.read(new File(SPLASH_SCREEN_BACKGROUND_PATH));
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        } catch (Exception e) {
            Logger.err("Failed to load splash screen background image", "SplashScreen::paintComponent");
        }
    }

    private class MonitorWorker extends SwingWorker<Void, Integer> {
        private boolean checked = false;
        @Override
        protected Void doInBackground() {
            if (!checked) {
                IOUtils.checkForGameFiles();
                checked = true;
            }
            int prevCompletedJobs = 0;
            while (IOUtils.completedJobs() < IOUtils.jobs()) {
                int currentCompletedJobs = IOUtils.completedJobs();
                if (currentCompletedJobs > prevCompletedJobs) {
                    publish(currentCompletedJobs);
                    prevCompletedJobs = currentCompletedJobs;
                }
                if (!SwingUtilities.isEventDispatchThread())
                    SwingUtilities.invokeLater(() -> {});
            }
            return null;
        }

        @Override
        protected void done() {
            SwingUtilities.invokeLater(() -> context.stage(Stage.MAIN));
        }
    }
}