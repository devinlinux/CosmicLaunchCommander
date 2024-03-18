package com.michaelb.clc;

//  imports
import javax.swing.SwingUtilities;
import com.michaelb.clc.math.geom.*;

public interface Main {
    static void main(String... args) {
        initLogger();
        SwingUtilities.invokeLater(() ->
            new com.michaelb.clc.gui.Frame());
    }

    private static void initLogger() {
        com.michaelb.clc.util.Logger.init();
        com.michaelb.clc.util.Logger.outputStream(System.out);
        com.michaelb.clc.util.Logger.info("Started program", "Main::main");
    }
}
