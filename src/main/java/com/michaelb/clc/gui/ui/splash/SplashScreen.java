package com.michaelb.clc.gui.ui.splash;

//  imports
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.util.List;

import com.michaelb.clc.util.IOUtils;

public class SplashScreen extends JPanel {
    private JProgressBar progressBar;

    public SplashScreen() {
        super();
        initComponents();
        startMonitoring();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        progressBar = new JProgressBar(0, IOUtils.jobs());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(progressBar, gbc);
    }

    private void startMonitoring() {
        new MonitorWorker().execute();
    }

    private class MonitorWorker extends SwingWorker<Void, Integer> {
        @Override
        protected Void doInBackground() throws Exception {
            int prevCompletedJobs = 0;
            while (IOUtils.completedJobs() < IOUtils.jobs()) {
                int currentCompletedJobs = IOUtils.completedJobs();
                if (currentCompletedJobs > prevCompletedJobs) {
                    publish(currentCompletedJobs);
                    prevCompletedJobs = currentCompletedJobs;
                }
                Thread.sleep(100);  // adjust the delay as needed
            }
            publish(IOUtils.jobs());  //  ensure progress bar reaches 100%
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            int progress = chunks.getLast();
            progressBar.setValue(progress);
            if (progress == IOUtils.jobs()) {
                System.out.println("done... all jobs completed");
            }
        }
    }
}