package satellite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import balise.SynchronisationEndEvent;
import balise.SynchronisationListener;
import balise.SynchronisationStartEvent;
import src.nicellipse.component.NiRectangle;

public class SatelliteView extends NiRectangle implements SatelliteListener, SynchronisationListener {

	private final Satellite satellite;
	private Color color = Color.GRAY;
	private Color normalColor = Color.GRAY;
	private Color syncColor = Color.ORANGE; // Couleur pendant la synchronisation
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

	public Satellite getSatellite() {
		return satellite;
	}

	@Override
	public void onSynchronisationStart(SynchronisationStartEvent event) {
		// Changer la couleur en orange pendant la synchronisation
		color = syncColor;
		repaint();
		System.out.println("🟠 Vue Satellite: " + satellite.getId() + " en synchronisation (orange)");
	}

	@Override
	public void onSynchronisationEnd(SynchronisationEndEvent event) {
		// Revenir à la couleur normale après la synchronisation
		color = normalColor;
		repaint();
		System.out.println("⚪ Vue Satellite: " + satellite.getId() + " termine la synchronisation (gris)");
	}
}
