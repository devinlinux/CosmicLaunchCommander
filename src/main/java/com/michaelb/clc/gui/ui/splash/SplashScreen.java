package com.michaelb.clc.gui.ui.splash;

//  imports
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import java.awt.GridBagLayout;

import java.util.List;

import com.michaelb.clc.gui.Frame;
import com.michaelb.clc.gui.Stage;
import com.michaelb.clc.util.IOUtils;

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
                Thread.sleep(0);
            }
            publish(IOUtils.jobs());
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            int progress = chunks.getLast();
            progressBar.setValue(progress);
            if (progress == IOUtils.jobs()) {
                System.out.println("done... all jobs completed");
                context.stage(Stage.MAIN);
            }
        }
    }
}
