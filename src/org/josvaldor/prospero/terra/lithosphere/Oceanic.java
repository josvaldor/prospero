package org.josvaldor.prospero.terra.lithosphere;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.josvaldor.prospero.terra.unit.Coordinate;

import ucar.nc2.Variable;
import ucar.nc2.NetcdfFile;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayShort;
import ucar.nc2.NetcdfFileWriter;

public class Oceanic {
	public static int longitudeCount = 0;
	public static int latitudeCount = 0;
	public static int elevationCount = 0;
	public static int timeCount = 0;
	public static int uCount = 0;
	public static int vCount = 0;
	public static String startDate = null;
	public static String fileName = "./data/lithosphere/oceanic/RN-8098_1510354754870/GEBCO_2014_2D.nc";
	public static NetcdfFileWriter netCDFFile;

	public static void main(String[] args) {
		Oceanic oceanic = new Oceanic();
		System.out.println(oceanic.box(-89,109, 0, 112));
	}
	
	public List<Coordinate> box(double latA, double lonA, double latB, double lonB) {
		NetcdfFile dataFile = null;
		List<Coordinate> cList = new LinkedList<Coordinate>();
		try {
			dataFile = NetcdfFile.open(fileName, null);
			Variable latVar = dataFile.findVariable("lat");
			Variable lonVar = dataFile.findVariable("lon");
			Variable elevation = dataFile.findVariable("elevation");
			longitudeCount = (int) lonVar.getSize();
			latitudeCount = (int) latVar.getSize();
			elevationCount = (int) elevation.getSize();
			ArrayDouble.D1 latArray = (ArrayDouble.D1) latVar.read();
			ArrayDouble.D1 lonArray = (ArrayDouble.D1) lonVar.read();
			ArrayShort.D2 elevationArray = (ArrayShort.D2) elevation.read();
			double latitude = 0;
			double longitude = 0;
			double ele = 0;
			int latInt = 0;
			int lonInt = 0;
			double latRemainder = 0;
			double lonRemainder = 0;
			double range = 0.01;
			Coordinate c = null;
			for (int j = 0; j < latitudeCount; j++) {
				latitude = latArray.get(j);
				for (int i = 0; i < longitudeCount; i++) {
					longitude = lonArray.get(i);
					ele = elevationArray.get(j,i);
					if(latA < latitude && lonA < longitude && latitude < latB && longitude < lonB) {
						latInt = (int)latitude;
						lonInt = (int)longitude;
						latRemainder = Math.abs(latitude%latInt);
						lonRemainder = Math.abs(longitude%lonInt);
						if(latRemainder < range && lonRemainder < range) {
							c = new Coordinate();
							c.latitude = latitude;
							c.longitude = longitude;
							c.elevation = ele;
							cList.add(c);
//							System.out.println("lat:"+latitude+" lon:"+longitude+" elevation:"+ele);
						}
					}
				}
			}
		} catch (java.io.IOException e) {
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
