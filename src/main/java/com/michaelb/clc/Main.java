package com.michaelb.clc;
import static com.michaelb.clc.math.MathUtil.*;
public interface Main {
    static void main(String... args) {
        Cartesian3D a = new Cartesian3D(1, 1, 1);
        System.out.println(a.toSpherical());
        System.out.println(a.toSpherical().toCartesian3D());
//        initLogger();
//        javax.swing.SwingUtilities.invokeLater(com.michaelb.clc.gui.Frame::new);
    }

    private static void initLogger() {
        com.michaelb.clc.util.Logger.init();
        com.michaelb.clc.util.Logger.outputStream(System.out);
        com.michaelb.clc.util.Logger.info("Started program", "Main::main");
    }
}
