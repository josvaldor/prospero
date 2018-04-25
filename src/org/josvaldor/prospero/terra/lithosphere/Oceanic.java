package org.josvaldor.prospero.terra.lithosphere;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.josvaldor.prospero.terra.unit.Coordinate;

import ucar.nc2.Variable;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.ma2.ArrayInt;
import ucar.ma2.ArrayShort;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFileWriter;

/*
 * 
 */

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
		System.out.println(oceanic.getBox(-89,111, -88, 112));
	}
	
	public List<Coordinate> getBox(double latA, double lonA, double latB, double lonB) {
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
			Coordinate c = null;
			for (int j = 0; j < latitudeCount; j++) {
				latitude = latArray.get(j);
				for (int i = 0; i < longitudeCount; i++) {
					longitude = lonArray.get(i);
					ele = elevationArray.get(j,i);
					if(latA < latitude && lonA < longitude && latitude < latB && longitude < lonB) {
						c = new Coordinate();
						c.latitude = latitude;
						c.longitude = longitude;
						c.elevation = ele;
						cList.add(c);
						System.out.println("lat:"+latitude+" lon:"+longitude+" elevation:"+ele);
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

	public Double getElevation(double lat, double lon) {
		NetcdfFile dataFile = null;
		Double elev = null;
		try {
			dataFile = NetcdfFile.open(fileName, null);
			Variable latVar = dataFile.findVariable("lat");
			Variable lonVar = dataFile.findVariable("lon");
			Variable elevation = dataFile.findVariable("elevation");
			longitudeCount = (int) lonVar.getSize();
			latitudeCount = (int) latVar.getSize();
			elevationCount = (int) elevation.getSize();
			System.out.println(longitudeCount);
			System.out.println(latitudeCount);
			System.out.println(elevationCount);
			ArrayDouble.D1 latArray = (ArrayDouble.D1) latVar.read();
			ArrayDouble.D1 lonArray = (ArrayDouble.D1) lonVar.read();
			ArrayShort.D2 elevationArray = (ArrayShort.D2) elevation.read();
			double latitude = 0;
			double longitude = 0;
			double ele = 0;
			for (int j = 0; j < latitudeCount; j++) {
				latitude = latArray.get(j);
				for (int i = 0; i < longitudeCount; i++) {
					longitude = lonArray.get(i);
					ele = elevationArray.get(j,i);
					if(lat == latitude && lon == longitude) 
					{
						System.out.println("lat:"+latitude+" lon:"+longitude+" elevation:"+ele);
						elev = ele;
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
		return elev;
	}
}
