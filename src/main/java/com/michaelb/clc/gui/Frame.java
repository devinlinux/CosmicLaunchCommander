package com.michaelb.clc.gui;

//  imports
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.Component;

import com.michaelb.clc.util.Logger;
import com.michaelb.clc.util.ProgramInfo;

import com.michaelb.clc.gui.ui.main.MainScreen;
import com.michaelb.clc.gui.ui.complex.ComplexScreen;

import static com.michaelb.clc.util.IOUtils.SEP;

public class Frame extends JFrame {

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

        Logger.info("Created new Frame: %s".formatted(super.getTitle()), "Frame::new");
    }

    private void setup() {
        this.setTitle(LONG_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(INIT_WIDTH, INIT_HEIGHT);
        this.setIconImage(ICON.getImage());
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setTitle(getWidth() <= 300 ? SHORT_TITLE : LONG_TITLE);
            }
        });
        this.setVisible(true);
    }

    private void display() {
        switch (this.stage) {
            case SPLASH_SCREEN -> System.out.println("add(new SplashScreen())");
            case LOGIN -> System.out.println("add(new LoginSceren())");
            case MAIN -> this.add(new MainScreen(this), BorderLayout.CENTER);
            case CONSTRUCTION -> System.out.println("add(new ConstructionScreen())");
            case LAUNCH -> System.out.println("add(new LaunchScreen())");
            case COMPLEX -> this.add(new ComplexScreen(this), BorderLayout.CENTER);
        }
        setVisible(true);
    }

    public void stage(final Stage stage) {
        this.stage = stage;

        clearComponents();
        display();
    }

    private void clearComponents() {
        for (Component component : this.getContentPane().getComponents())
            this.getContentPane().remove(component);

        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }
}
