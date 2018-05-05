package org.josvaldor.prospero.terra.biosphere;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

public class City {

	private String fileName = "./data/biosphere/city/ne_10m_populated_places/ne_10m_populated_places.shp";
	
	public static void main(String[] args) {
		City a = new City();
		a.point(-75.17194183200792, 40.001919022526465);
		a.box(110, -45, 155, -10);
	}

	public Point point(double latitude, double longitude) {
		File file = new File(fileName);
		Point p = null;
		try {
			ShapefileDataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
			String[] typeNames = dataStore.getTypeNames();
			String typeName = typeNames[0];
			SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
			Filter filter = ECQL.toFilter(" CONTAINS (the_geom, POINT(" + latitude + " " + longitude + "))");
			SimpleFeatureCollection collection = featureSource.getFeatures(filter);
			SimpleFeatureIterator iterator = collection.features();
			
			try {
				while (iterator.hasNext()) {
					SimpleFeature feature = iterator.next();
					GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
					WKTReader reader = new WKTReader( geometryFactory );
					p = (Point) reader.read(feature.getDefaultGeometry()+"");
				}
			} finally {
				iterator.close();
			}

		} catch (Throwable e) {
		}
		return p;
	}

	public List<Point> box(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
		File file = new File(fileName);
		List<Point> pList = new LinkedList<Point>();
		try {
			ShapefileDataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
			String[] typeNames = dataStore.getTypeNames();
			String typeName = typeNames[0];
			SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
			Filter filter = ECQL.toFilter("BBOX (the_geom, " + latitudeA + ", " + longitudeA + ", " + latitudeB + ", " + longitudeB + ")");
			SimpleFeatureCollection collection = featureSource.getFeatures(filter);
			SimpleFeatureIterator iterator = collection.features();
			try {
				while (iterator.hasNext()) {
					SimpleFeature feature = iterator.next();
					GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
					WKTReader reader = new WKTReader( geometryFactory );
					Point p = (Point) reader.read(feature.getDefaultGeometry()+"");
					pList.add(p);
//					String name = (String) feature.getAttribute("NAMEASCII");
//					System.out.println("Name:" + name);
				}
			} finally {
				iterator.close();
			}

		} catch (Throwable e) {
		}
		return pList;
	}
}
