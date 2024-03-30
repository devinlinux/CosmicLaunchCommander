package com.michaelb.clc;
import com.michaelb.clc.physics.*;
import com.michaelb.clc.physics.celestial.bodies.CelestialBody;
import com.michaelb.clc.physics.celestial.bodies.Planet;
import com.michaelb.clc.physics.celestial.StarSystem;
import com.michaelb.clc.ship.Spacecraft;
public interface Main {
    static void main(String... args) {
        CelestialBody a = new Planet("", 5.2972e24, 12, 0, 0, 6378100, 0);
        CelestialBody b = new Planet("", 5.2972e24, 12, 0, 0, -6378100, 0);
        StarSystem s = new StarSystem("", a, b);

        Spacecraft c = new Spacecraft("", 10, 1, 0, 0, 0);

        TrajectoryCalculator calc = new TrajectoryCalculator(c, s);
        System.out.println(calc.netGravitationalForce());
//        initLogger();
//        javax.swing.SwingUtilities.invokeLater(com.michaelb.clc.gui.Frame::new);
    }

    private static void initLogger() {
        com.michaelb.clc.util.Logger.init();
        com.michaelb.clc.util.Logger.outputStream(System.out);
        com.michaelb.clc.util.Logger.info("Started program", "Main::main");
    }
}
