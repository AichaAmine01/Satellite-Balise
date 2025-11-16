package satellite;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import src.nicellipse.component.NiImage;

public class SatelliteView extends NiImage implements SatelliteListener {

	private final Satellite satellite;
	private static final int ICON_WIDTH = 45;
	private static final int ICON_HEIGHT = 45;

	public Satellite getSatellite() {
		return this.satellite;
	}

	public SatelliteView(Satellite satellite) throws IOException {
		super(loadAndResizeSatelliteImage());
		this.satellite = satellite;
		this.setOpaque(false);
		this.setBounds(satellite.getX(), satellite.getY(), ICON_WIDTH, ICON_HEIGHT);
	}

	private static Image loadAndResizeSatelliteImage() throws IOException {
		// Essai dans resources/
		File f = new File("resources" + File.separator + "satellite.png");
		if (!f.exists()) {
			// Essai chemin alternatif
			f = new File("Projet_balise_satellite" + File.separator + "resources" + File.separator + "satellite.png");
			if (!f.exists()) {
				throw new IOException("satellite.png not found in resources");
			}
		}
		// Charger et redimensionner l'image
		Image originalImage = ImageIO.read(f);
		return originalImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
	}

	@Override
	public void onSatelliteMove(SatelliteMoveEvent event) {
		Satellite source = (Satellite) event.getSource();
		int x = source.getX();
		int y = source.getY();
		this.setBounds(x, y, ICON_WIDTH, ICON_HEIGHT);
		this.revalidate();
		this.repaint();
	}
}
