/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.josvaldor.prospero.terra;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.josvaldor.prospero.terra.atmosphere.tornado.Tornado;
import org.josvaldor.prospero.terra.biosphere.City;
import org.josvaldor.prospero.terra.biosphere.Country;
import org.josvaldor.prospero.terra.lithosphere.Continental;
import org.josvaldor.prospero.terra.lithosphere.Oceanic;
import org.josvaldor.prospero.terra.lithosphere.Tectonic;
import org.josvaldor.prospero.terra.lithosphere.earthquake.Earthquake;
import org.josvaldor.prospero.terra.lithosphere.volcano.Volcano;
import org.josvaldor.prospero.terra.unit.Coordinate;
import org.josvaldor.prospero.terra.unit.Event;
import org.josvaldor.prospero.terra.unit.Time;
import org.josvaldor.prospero.terra.unit.Type;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

public class Terra {
	public Calendar time;
	public double latitudeMin = -90.0;
	public double latitudeMax = 90.0;
	public double longitudeMin = -180;
	public double longitudeMax = 180;
	public double pressureMin = 0;
	public double pressureMax = 10000;
	public List<org.josvaldor.prospero.energy.Coordinate> coordinateList;
	public Country country;
	public City city;
	public Tectonic tectonic;
	public Oceanic oceanic;
	public Continental continental;
	public double scale;
	public List<Event> eventList;
	List<MultiPolygon> countryList;
	List<Point> cityList;
	List<MultiLineString> tectonicList;
	List<Coordinate> oceanicList;
	List<Coordinate> continentalList;
	Tornado tornado = new Tornado();
	Volcano volcano = new Volcano();
	Earthquake earthquake = new Earthquake();

	public Terra(Calendar calendar) {
		this.time = calendar;
		city = new City();
		country = new Country();
		tectonic = new Tectonic();
		oceanic = new Oceanic();
		continental = new Continental();
		cityList = city.box(-180,90,180, -90);
		countryList = country.box(-180,90,180, -90);
		tectonicList = tectonic.box(-180,90,180, -90);
//		oceanicList = oceanic.box(-90,-180, 90, 180);
		oceanicList = new LinkedList<Coordinate>();
		continentalList = continental.box(-90,-180, 90, 180);
		eventList = this.getEventList();
		this.setScale(3.0);
	}
	
	public List<Event> getEventList(){
		List<Event> tornadoList;
		List<Event> volcanoList;
		List<Event> earthquakeList;
		List<Event> eventList = new LinkedList<Event>();
		tornadoList = tornado.read();
		volcanoList = volcano.read();
		earthquakeList = earthquake.read();
		eventList.addAll(tornadoList);
		eventList.addAll(volcanoList);
		eventList.addAll(earthquakeList);
		return eventList;
	}
	
	public void setScale(double scale){
		this.scale = scale;
	}
	
	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}
	
	public List<Event> getTimeEventList(List<Event> eventList,Calendar time){
		List<Event> eList = new LinkedList<Event>();
		for(Event event:eventList){
			if(event.time.size()>0&event.coordinate.size()>0&&event.time.get(0) != null && event.coordinate.get(0)!=null){
				if(event.time.get(0).time.get(Calendar.YEAR)==(time).get(Calendar.YEAR)&&
						event.time.get(0).time.get(Calendar.MONTH)==(time).get(Calendar.MONTH)&&
						event.time.get(0).time.get(Calendar.WEEK_OF_MONTH)==(time).get(Calendar.WEEK_OF_MONTH)&&
						event.time.get(0).time.get(Calendar.DAY_OF_WEEK)==(time).get(Calendar.DAY_OF_WEEK)){
					eList.add(event);
				}
			}
		}
		return eList;
	}
	
	public void draw(Graphics g){
//		coordinateList = solar.getCoordinateList(new org.josvaldor.prospero.energy.system.planet.earth.Earth(this.time, new Sun(this.time)), solar.getEnergyList(this.time));
		g.setColor(Color.yellow);
		double radius = 5;
		g.drawString(Time.getCalendarString(null, this.time),(int)(0*scale), (int)(0*scale));
		if(this.coordinateList != null){
		for(org.josvaldor.prospero.energy.Coordinate c:coordinateList){
			g.drawString(c.label,(int)(c.longitude*scale), (int)(c.latitude*scale));
			g.fillOval((int)(c.longitude*scale), (int)(c.latitude*scale), (int)radius, (int)radius);
		}
		}
		for(Event event:this.getTimeEventList(eventList,this.time)){
			if(event.type==Type.VOLCANO){
				g.setColor(Color.BLUE);
			}else if(event.type==Type.TORNADO){
				g.setColor(Color.RED);
			}else if(event.type==Type.EARTHQUAKE){
				g.setColor(Color.GREEN);
			}
			g.fillOval((int)(event.coordinate.get(0).longitude*scale), (int)-(event.coordinate.get(0).latitude*scale), (int)radius, (int)radius);
		}
		g.setColor(Color.PINK);
		com.vividsolutions.jts.geom.Coordinate[] a;
		for(MultiLineString f:tectonicList){
			a = f.getCoordinates();
			for(int i=0; i<a.length;i++){
				g.fillOval((int)(a[i].x*scale), -(int)(a[i].y*scale), (int)2, (int)2);
			}
		}
		g.setColor(Color.WHITE);
		for(MultiPolygon f:countryList){
			a = f.getCoordinates();
			for(int i=0; i<a.length;i++){
				g.fillOval((int)(a[i].x*scale), -(int)(a[i].y*scale), (int)2, (int)2);
			}
		}
		
		g.setColor(Color.gray);
		for(Point f:cityList){
			a = f.getCoordinates();
			for(int i=0; i<a.length;i++){
				g.fillOval((int)(a[i].x*scale), -(int)(a[i].y*scale), (int)2, (int)2);
			}
		}
		
		for(Coordinate c: oceanicList) {
			g.setColor(this.getElevationColor(c.elevation,'b'));
			g.fillOval((int)(c.longitude*scale), (int)-(c.latitude*scale), (int)2, (int)2);
		}
		
		for(Coordinate c: continentalList) {
			g.setColor(this.getElevationColor(c.elevation,'g'));
			g.fillOval((int)(c.longitude*scale), (int)-(c.latitude*scale), (int)2, (int)2);
		}
	}
	
	public Color getElevationColor(double elevation, char c) {
		Color color = null;
		int min = -50000;
		int max = 50000;
		int diff = max - min;
		double range = 0;
		double conversion = 0;
		range = elevation+max;
		conversion = range/diff;
		switch(c) {
		case 'r':{
			color = new Color((int)(255*conversion),0,0);
			break;
		}
		case 'g':{
			color = new Color(0,(int)(255*conversion),0);
			break;
		}
		case 'b':{
			color = new Color(0,0,(int)(255*conversion));
			break;
		}
		}
		return color;
	}
}