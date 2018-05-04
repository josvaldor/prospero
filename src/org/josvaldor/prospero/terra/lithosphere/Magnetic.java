package org.josvaldor.prospero.terra.lithosphere;

import ucar.nc2.Variable;
import ucar.nc2.NetcdfFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.josvaldor.prospero.terra.unit.Coordinate;

import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayInt;

public class Magnetic {
	String fileName  = "./data/lithosphere/magnetic/EMAG2_V2.nc";
	public static int longitudeCount = 0;
	public static int latitudeCount = 0;
	public static int zCount = 0;
	public static int dimensionCount = 0;
	public static int spacingCount = 0;
	
	public static void main(String[] args) {
		Magnetic magnetic = new Magnetic();
		magnetic.box(-89,109, 0, 112);
	}
	
	public List<Coordinate> box(double latA, double lonA, double latB, double lonB) {
		NetcdfFile dataFile = null;
		List<Coordinate> cList = new LinkedList<Coordinate>();
		try {
			dataFile = NetcdfFile.open(fileName, null);
			Variable latVar = dataFile.findVariable("y_range");
			Variable lonVar = dataFile.findVariable("x_range");
			Variable zVar = dataFile.findVariable("z");
			Variable dimensionVar = dataFile.findVariable("dimension");
			Variable spacingVar = dataFile.findVariable("spacing");
			longitudeCount = (int) lonVar.getSize();
			latitudeCount = (int) latVar.getSize();
			zCount = (int) zVar.getSize();
			dimensionCount = (int) dimensionVar.getSize();
			spacingCount = (int) spacingVar.getSize();
			System.out.println(longitudeCount);
			System.out.println(latitudeCount);
			System.out.println(zCount);
			System.out.println(dimensionCount);
			System.out.println(spacingCount);
			ArrayDouble.D1 latArray = (ArrayDouble.D1) latVar.read();
			ArrayDouble.D1 lonArray = (ArrayDouble.D1) lonVar.read();
			ArrayFloat.D1 zArray = (ArrayFloat.D1)zVar.read();
			ArrayInt.D1 dimensionArray = (ArrayInt.D1)dimensionVar.read();
			System.out.println(latArray.getSize());
			System.out.println(lonArray.getSize());
			double latitude = 0;
			double longitude = 0;
			for(int i=0;i<dimensionArray.get(0);i++) {
				for(int j=0;j<dimensionArray.get(1);j++) {
					System.out.println(zArray.get(i*j));
				}
			}
		}catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			if (dataFile != null) {
				try {
					dataFile.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return cList;
	}
}
