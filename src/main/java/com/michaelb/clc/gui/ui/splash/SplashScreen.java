package com.michaelb.clc.gui.ui.splash;

//  imports
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;

import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;

import java.util.List;

import com.michaelb.clc.gui.Frame;
import com.michaelb.clc.gui.Stage;
import com.michaelb.clc.util.IOUtils;
import com.michaelb.clc.util.Logger;

import static com.michaelb.clc.util.IOUtils.SPLASH_SCREEN_BACKGROUND_PATH;

public class SplashScreen extends JPanel {

    private final Frame context;

    private JProgressBar progressBar;

    public SplashScreen(final Frame context) {
        this.context = context;

        this.initComponents();
        this.startMonitoring();
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());

        this.progressBar = new JProgressBar(0, IOUtils.jobs());
        this.add(progressBar);
    }

    private void startMonitoring() {
        new MonitorWorker().execute();
        IOUtils.checkForGameFiles();
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
        @Override
        protected Void doInBackground() throws Exception {
            SwingUtilities.invokeLater(SplashScreen.this::initComponents);
            int prevCompletedJobs = 0;
            while (IOUtils.completedJobs() < IOUtils.jobs()) {
                int currentCompletedJobs = IOUtils.completedJobs();
                if (currentCompletedJobs > prevCompletedJobs) {
                    publish(currentCompletedJobs);
                    prevCompletedJobs = currentCompletedJobs;
                }
                Thread.sleep(1);
            }
            publish(IOUtils.jobs());
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            int progress = chunks.getLast();
            SwingUtilities.invokeLater(() -> {
                progressBar.setValue(progress);
                if (progress == IOUtils.jobs()) {
                    System.out.println("done... all jobs completed");
                    context.stage(Stage.MAIN);
                }
            });
        }
    }
}
