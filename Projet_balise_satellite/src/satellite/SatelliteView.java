package satellite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import src.nicellipse.component.NiRectangle;

public class SatelliteView extends NiRectangle implements SatelliteListener{

	private final Satellite satellite;
	private Color color = Color.GRAY;
	private final int w = 25, h = 25;

	public SatelliteView(Satellite satellite) {
		this.satellite = satellite;
		this.setBounds(satellite.getX(), satellite.getY(), w, h);
		this.setOpaque(true);
	}

	@Override
	public void onSatelliteMove(SatelliteMoveEvent event) {
		Satellite source = (Satellite) event.getSource();
		int x = source.getX();
		int y = source.getY();
		this.setBounds(x, y, w, h);
		this.revalidate();
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}
}
