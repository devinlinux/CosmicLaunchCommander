package com.michaelb.clc.gui.ui.splash;

//  imports
import javax.swing.JPanel;

import com.michaelb.clc.util.IOUtils;

public class SplashScreen extends JPanel {

    public SplashScreen() {
        super();

        IOUtils.checkForGameFiles();
    }
}
