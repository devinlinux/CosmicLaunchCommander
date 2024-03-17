package com.michaelb.clc.ship.components;

//  imports
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.michaelb.clc.math.geom.Shape;
import com.michaelb.clc.sci.Material;

public class Stage {

    private String name;
    private Shape externalGeometry;
    private Material externalMaterial;
    private List<ShipComponent> internalComponents;

    public double externalMass() {
        return externalGeometry.volume() * externalMaterial.density();
    }
}
