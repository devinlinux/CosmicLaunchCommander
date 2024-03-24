package com.michaelb.clc.gui;

//  imports
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.michaelb.clc.util.Logger;
import com.michaelb.clc.util.ProgramInfo;

import com.michaelb.clc.gui.panels.MainScreen;

import static com.michaelb.clc.util.IOUtils.SEP;

public class Frame extends JFrame implements ComponentListener {

    private static final String LONG_TITLE = "Cosmic Launch Commander %s".formatted(ProgramInfo.VERSION);
    private static final String SHORT_TITLE = "CLC %s".formatted(ProgramInfo.VERSION);

    private static final int INIT_WIDTH = 1700;
    private static final int INIT_HEIGHT = 1000;

    private static final ImageIcon ICON = new ImageIcon("src%smain%sresources%simages%simage_icon.png".formatted(SEP, SEP, SEP, SEP));

    private Stage stage;

    public Frame() {
        //this(Stage.SPLASH_SCREEN);
        this(Stage.MAIN);
    }

    public Frame(final Stage stage) {
        super(LONG_TITLE);
        this.stage = stage;
        setup();
        display();

        Logger.info("Created new Frame: %s".formatted(super.getName()), "Frame::new");
    }

    private void setup() {
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(INIT_WIDTH, INIT_HEIGHT);
        super.setIconImage(ICON.getImage());
        super.setLocationRelativeTo(null);
        super.addComponentListener(this);
        super.setVisible(true);
    }

    private void display() {
        switch (this.stage) {
            case SPLASH_SCREEN -> System.out.println("add(new SplashScreen())");
            case LOGIN -> System.out.println("add(new LoginSceren())");
            case MAIN -> {
                this.add(new MainScreen(this));
                this.setVisible(true);
            }
            case CONSTRUCTION -> System.out.println("add(new ConstructionScreen())");
            case LAUNCH -> System.out.println("add(new LaunchScreen())");
        }
    }

    public void stage(final Stage stage) {
        this.stage = stage;
    }

    /* component listener */

    @Override
    public void componentResized(ComponentEvent e) {
        super.setTitle(super.getWidth() <= 300 ? SHORT_TITLE : LONG_TITLE);
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}
