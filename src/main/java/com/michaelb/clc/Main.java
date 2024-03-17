package com.michaelb.clc;

//  imports
import javax.swing.SwingUtilities;
import com.michaelb.clc.math.geom.*;

public interface Main {
    static void main(String... args) {
        /*
        initLogger();
        SwingUtilities.invokeLater(() ->
            new com.michaelb.clc.gui.Frame());
        */
        Shape tank = new Cylinder(5, 20);
        Shape taper = new TruncatedCone(5, 3, 2);
        Shape upperTank = new Cylinder(3, 10);
        Shape noseCone = new Cone(3, 2);
        Figure3D craft = new Figure3D(tank, taper, upperTank, noseCone);
        System.out.println(craft.volume());
        System.out.println(craft.surfaceArea());
    }

    private static void initLogger() {
        com.michaelb.clc.util.Logger.init();
        com.michaelb.clc.util.Logger.outputStream(System.out);
        com.michaelb.clc.util.Logger.info("Started program", "Main::main");
    }
}
