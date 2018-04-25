package org.josvaldor.prospero.terra.unit;

public class Coordinate {
	
	public double latitude;
	public double longitude;
	public double elevation;
	
	public String toString(){
		return this.latitude+" "+this.longitude+" "+elevation;
	}
}
