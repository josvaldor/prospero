package org.josvaldor.prospero.energy.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JPanel;
import org.josvaldor.prospero.energy.system.Solar;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolarPanel extends JPanel implements MouseWheelListener, KeyListener, Runnable, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1000;
	private static final int HEIGHT = WIDTH / 16 * 9;
	private Solar solar;
	private int increment = 1;
	private int type = Calendar.DATE;
	private boolean run = true;
	private boolean realTime = true;
	private Thread thread = new Thread(this);
	private Image image = null;
	private Graphics graphics = null;

	public SolarPanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addMouseWheelListener(this);
		addKeyListener(this);
		addMouseListener(this);
	}

	public void setSolar(Solar solar) {
		this.solar = solar;
		// thread.start();
	}

	public void setTime(Calendar c) {
		this.solar.setTime(c);
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
		solar.draw(graphics);
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Calendar c = this.solar.getTime();
		int notches = e.getWheelRotation();
		if (notches < 0) {
			c.add(this.type, increment);
			this.solar.setTime(c);
			this.realTime = false;

		} else {
			c.add(this.type, -increment);
			this.solar.setTime(c);
			this.realTime = false;
		}
		this.paint(this.getGraphics());

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

		Calendar c = this.solar.getTime();
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			c.add(this.type, increment);
			this.solar.setTime(c);
			this.realTime = false;

		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			c.add(this.type, -increment);
			this.solar.setTime(c);
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
		double scale = this.solar.scale;
		if (e.getKeyChar() == '+' || e.getKeyChar() == '=') {
			if (scale < 1.024E-4) {
				this.solar.setScale(scale * 2);
			}

		}
		if (e.getKeyChar() == '-' || e.getKeyChar() == '_') {
			if (scale > 8.0E-8) {
				this.solar.setScale(scale / 2);
			}
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
					Logger.getLogger(SolarPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(SolarPanel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}
