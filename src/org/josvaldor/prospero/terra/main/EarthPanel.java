package org.josvaldor.prospero.terra.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import org.josvaldor.prospero.terra.Terra;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EarthPanel extends JPanel implements MouseWheelListener, KeyListener, Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 360 * 3;
	private static final int HEIGHT = 180 * 3;
	private Terra earth;
	private int increment = 1;
	private int type = Calendar.DATE;
	private boolean run = true;
	private boolean realTime = true;
	private Thread thread = new Thread(this);
	private Image image = null;
	private Graphics graphics = null;

	public EarthPanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addMouseWheelListener(this);
		addKeyListener(this);
	}

	public void setEarth(Terra earth) {
		this.earth = earth;
		// thread.start();
	}

	public void setTime(Calendar c) {
		this.earth.setTime(c);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.paint(g);
	}

	@Override
	public void paint(Graphics g) {
		if (image == null) {
			image = createImage(WIDTH, HEIGHT);
		}
		graphics = image.getGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		graphics.translate((int) (WIDTH / 2.0), (int) (HEIGHT / 2.0));
		earth.draw(graphics);
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println(e);
		Calendar c = this.earth.getTime();
		int notches = e.getWheelRotation();
		if (notches < 0) {
			c.add(this.type, increment);
			this.earth.setTime(c);
			this.realTime = false;

		} else {
			c.add(this.type, -increment);
			this.earth.setTime(c);
			this.realTime = false;
		}
		paint(this.getGraphics());
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'y') {
			this.type = Calendar.DATE;
			this.increment = 365;
		}
		if (e.getKeyChar() == 'm') {
			this.type = Calendar.DATE;
			this.increment = 31;
		}
		if (e.getKeyChar() == 'd') {
			this.type = Calendar.DATE;
			this.increment = 1;
		}
		if (e.getKeyChar() == 'h') {
			this.type = Calendar.HOUR;
			this.increment = 1;
		}
		if (e.getKeyChar() == 's') {
			this.realTime = true;
		}

		Calendar c = this.earth.getTime();
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			c.add(this.type, increment);
			this.earth.setTime(c);
			this.realTime = false;

		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			c.add(this.type, -increment);
			this.earth.setTime(c);
			this.realTime = false;
		}
		this.paint(this.getGraphics());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		this.increment = 1;
		this.type = Calendar.DATE;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		double scale = this.earth.scale;
		if (e.getKeyChar() == '+' || e.getKeyChar() == '=') {
			this.earth.setScale(scale * 2);
		}
		if (e.getKeyChar() == '-' || e.getKeyChar() == '_') {
			this.earth.setScale(scale / 2);
		}
		this.paint(this.getGraphics());
	}

	@Override
	public void run() {
		while (this.run) {
			while (this.realTime) {
				try {
					this.setTime(new GregorianCalendar());
					sleep(1000);
					this.paint(this.getGraphics());
				} catch (InterruptedException ex) {
					Logger.getLogger(EarthPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(EarthPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}