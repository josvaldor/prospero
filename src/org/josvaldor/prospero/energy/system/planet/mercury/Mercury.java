package org.josvaldor.prospero.energy.system.planet.mercury;

import org.josvaldor.prospero.energy.Orbital;
import org.josvaldor.prospero.energy.Unit;
import org.josvaldor.prospero.energy.system.planet.Planet;
import java.awt.Color;
import java.util.Calendar;

public class Mercury extends Planet {
    
    public Mercury(Calendar calendar,Orbital centroid) {
        super();
        this.centroid = centroid;
        this.name = "mercury";
        this.radius = 2439.0;
        this.mass = 3.30104E23;
        this.color = Color.green;
        this.longitudeOfAscendingNode[0] = 48.3313;//o
        this.longitudeOfAscendingNode[1] = 3.24587E-5;//o
        this.inclination[0] = 7.0047;//i
        this.inclination[1] = 5.00E-8;//i
        this.argumentOfPeriapsis[0] = 29.1241;
        this.argumentOfPeriapsis[1] = 1.01444E-5;
        this.semiMajorAxis[0] = 0.387098 * Unit.ASTRONOMICAL;//a
        this.semiMajorAxis[1] = 0;
        this.eccentricity[0] = 0.205635;//e
        this.eccentricity[1] = 5.59E-10;//e
        this.meanAnomaly[0] = 168.6562;
        this.meanAnomaly[1] = 4.0923344368;
        this.orbitalPeriod = 87.9691;
        this.time = calendar;
        this.position = this.getSpace(this.time).eliptic;
        this.angularVelocity = 1.240013441242619e-06;
    }
}
