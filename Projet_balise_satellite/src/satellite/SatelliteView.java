package satellite;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import balise.SynchronisationEndEvent;
import balise.SynchronisationListener;
import balise.SynchronisationStartEvent;
import src.nicellipse.component.NiImage;

/**
 * Vue graphique d'un satellite (Pattern Observable).
 * 
 * Cette classe est responsable de l'affichage graphique d'un satellite et
 * réagit aux événements émis par le modèle Satellite pour mettre à jour l'affichage.
 * 
 * Implémente :
 * - SatelliteListener : pour recevoir les événements de mouvement
 * - SynchronisationListener : pour recevoir les événements de synchronisation
 * 
 * Séparation Modèle-Vue : SatelliteView observe Satellite mais ne modifie jamais son état.
 * 
 * @see Satellite
 * @see SatelliteListener
 * @see SynchronisationListener
 */
public class SatelliteView extends NiImage implements SatelliteListener, SynchronisationListener {

	private final Satellite satellite;
	private static final int ICON_WIDTH = 45;
	private static final int ICON_HEIGHT = 45;

	public Satellite getSatellite() {
		return this.satellite;
	}

	/**
	 * Constructeur de la vue d'un satellite.
	 * Charge l'image du satellite et initialise sa position.
	 * 
	 * @param satellite Le modèle de satellite à afficher
	 * @throws IOException Si l'image satellite.png n'est pas trouvée
	 */
	public SatelliteView(Satellite satellite) throws IOException {
		super(loadAndResizeSatelliteImage());
		this.satellite = satellite;
		this.setOpaque(false);
		this.setBounds(satellite.getX(), satellite.getY(), ICON_WIDTH, ICON_HEIGHT);
	}

	/**
	 * Charge et redimensionne l'image du satellite.
	 * 
	 * @return L'image redimensionnée aux dimensions voulues
	 * @throws IOException Si le fichier satellite.png n'est pas trouvé
	 */
	private static Image loadAndResizeSatelliteImage() throws IOException {
		// Essai dans resources/
		File f = new File("resources" + File.separator + "satellite.png");
		if (!f.exists()) {
			// Essai chemin alternatif
			f = new File("Projet_balise_satellite" + File.separator + "resources" + File.separator + "satellite.png");
			if (!f.exists()) {
				// Essai chemin complet depuis le dossier Satellite-Balise
				f = new File("Satellite-Balise" + File.separator + "Projet_balise_satellite" + File.separator + "resources" + File.separator + "satellite.png");
				if (!f.exists()) {
					throw new IOException("satellite.png not found in resources");
				}
			}
		}
		// Charger et redimensionner l'image
		Image originalImage = ImageIO.read(f);
		return originalImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
	}

	/**
	 * Réagit aux événements de mouvement du satellite.
	 * Met à jour la position graphique de la vue pour refléter le déplacement.
	 * 
	 * @param event L'événement contenant la nouvelle position du satellite
	 */
	@Override
	public void onSatelliteMove(SatelliteMoveEvent event) {
		Satellite source = (Satellite) event.getSource();
		int x = source.getX();
		int y = source.getY();
		// Mettre à jour la position de l'image
		this.setBounds(x, y, ICON_WIDTH, ICON_HEIGHT);
		this.revalidate();
		this.repaint();
	}

	/**
	 * Réagit au début d'une synchronisation.
	 * Affiche un message console pour tracer l'événement.
	 * 
	 * @param event L'événement de début de synchronisation
	 */
	@Override
	public void onSynchronisationStart(SynchronisationStartEvent event) {
		System.out.println("Vue Satellite: " + satellite.getId() + " en synchronisation");
	}

	/**
	 * Réagit à la fin d'une synchronisation.
	 * Affiche un message console pour tracer l'événement.
	 * 
	 * @param event L'événement de fin de synchronisation
	 */
	@Override
	public void onSynchronisationEnd(SynchronisationEndEvent event) {
		System.out.println(" Vue Satellite: " + satellite.getId() + " termine la synchronisation");
	}
}

