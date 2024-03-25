package com.michaelb.clc;

public interface Main {
    static void main(String... args) {
        initLogger();
        com.michaelb.clc.util.IOUtils.checkForGameFiles();
        javax.swing.SwingUtilities.invokeLater(com.michaelb.clc.gui.Frame::new);
    }

    private static void initLogger() {
        com.michaelb.clc.util.Logger.init();
        com.michaelb.clc.util.Logger.outputStream(System.out);
        com.michaelb.clc.util.Logger.info("Started program", "Main::main");
    }
}
