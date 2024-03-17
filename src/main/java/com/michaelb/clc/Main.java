package com.michaelb.clc;

import com.michaelb.clc.util.Logger;

public interface Main {
    static void main(String... args) {
        initLogger();
        new com.michaelb.clc.gui.Frame();
    }

    private static void initLogger() {
        com.michaelb.clc.util.Logger.init();
        com.michaelb.clc.util.Logger.outputStream(System.out);
        Logger.info("Started program", "Main::main");
    }
}
