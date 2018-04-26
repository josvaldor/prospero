package org.josvaldor.prospero.energy.system.planet;

import org.josvaldor.prospero.energy.Orbital;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Planet extends Orbital {

	public Planet() {
		super();
		this.name = "planet";
	}

	public void draw(Graphics g) {
		double x = this.position.getX() * scale;
		double y = this.position.getY() * scale;
		g.setColor(Color.yellow);
		g.drawLine((int) x, (int) y, (int) (this.force.getX() * scale), (int) (this.force.getY() * scale));
		g.setColor(this.color);
		double radius = 5;
		g.drawLine((int) x, (int) y, (int)this.centroid.position.getX(), (int)this.centroid.position.getY());
		x = x - (radius / 2);
		y = y - (radius / 2);
		g.fillOval((int) x, (int) y, (int) radius, (int) radius);
		g.setColor(Color.WHITE);
		g.drawString(this.name.substring(0, 1).toUpperCase() + this.name.substring(1) + "", (int) x, (int) y);
		List<Vector3D> vertexList = this.getOrbit();
		g.setColor(Color.white);
		radius = 5;
		for (int i = 1; i < vertexList.size(); i++) {
			g.drawLine((int) (vertexList.get(i - 1).getX() * scale), (int) (vertexList.get(i - 1).getY() * scale),
					(int) (vertexList.get(i).getX() * scale), (int) (vertexList.get(i).getY() * scale));
		}
	}
}
