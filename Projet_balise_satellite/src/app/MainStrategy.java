package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import balise.Balise;
import balise.BaliseState;
import balise.BaliseView;
import method.LinearMethod;
import method.StaticMethod;
import method.SinusoidalMethod;
import method.VerticalMethod;
import satellite.Satellite;
import satellite.SatelliteView;
import src.nicellipse.component.NiSpace;

/**
 * Programme de simulation des balises et satellites.
 * 
 * La fenêtre est divisée en deux zones :
 * - Zone supérieure (blanche) : Espace aérien avec les satellites
 * - Zone inférieure (bleue) : Océan avec les balises
 */
public class MainStrategy {
	
	private static final int OCEAN_START_Y = 300;  // Y où commence l'océan
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// Créer la fenêtre principale
		NiSpace space = new NiSpace("Simulation Balises & Satellites", new Dimension(800, 600));
		space.setBackground(Color.WHITE);

		// Listes pour gérer les balises et satellites
		List<Balise> balises = new ArrayList<>();
		List<BaliseView> baliseViews = new ArrayList<>();
		List<Satellite> satellites = new ArrayList<>();
		List<SatelliteView> satelliteViews = new ArrayList<>();
		
		// Panel pour dessiner les lignes de synchronisation
		SynchronisationLinePanel syncLinePanel = new SynchronisationLinePanel();
		syncLinePanel.setBounds(0, 0, 800, 600);
		syncLinePanel.setLayout(null);

		// ==================== SATELLITES - Dans l'espace (zone blanche) ====================
		
		Satellite s1 = new Satellite(100, 50, 1);
		s1.setScreenWidth(800);  // Configurer la largeur de l'écran
		SatelliteView sv1 = new SatelliteView(s1);
		s1.registerMoveEvent(sv1);
		satellites.add(s1);
		satelliteViews.add(sv1);

		Satellite s2 = new Satellite(400, 100, -1);
		s2.setScreenWidth(800);  // Configurer la largeur de l'écran
		SatelliteView sv2 = new SatelliteView(s2);
		s2.registerMoveEvent(sv2);
		satellites.add(s2);
		satelliteViews.add(sv2);

		Satellite s3 = new Satellite(650, 150, 1);
		s3.setScreenWidth(800);  // Configurer la largeur de l'écran
		SatelliteView sv3 = new SatelliteView(s3);
		s3.registerMoveEvent(sv3);
		satellites.add(s3);
		satelliteViews.add(sv3);

		// ==================== ZONES (Visuelle) ====================
		
		// Zone océan (bleu) - Panel qui peint l'océan en background
		JPanel oceanZone = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Peindre le background bleu de l'océan
				g.setColor(Color.BLUE);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		oceanZone.setOpaque(false);  // Transparent pour ne pas masquer les composants
		oceanZone.setSize(800, 300);  // Hauteur: 600 - 300 = 300
		oceanZone.setLocation(0, OCEAN_START_Y);
		oceanZone.setLayout(null);  // Pas de layout manager

		// ==================== BALISES - Dans l'océan (zone bleue) ====================
		// Les balises démarrent EN PROFONDEUR pour bien visualiser la phase de collecte
		// Surface = OCEAN_START_Y (300), Fond = 600
		// Zone de collecte : entre 450 et 570 (profondeur significative)
		
		// Balise 1 : Mouvement linéaire horizontal (EN PROFONDEUR)
		Balise b1 = new Balise(100, OCEAN_START_Y + 200, 1, "Balise_Linear"); // Y=500 (profondeur)
		BaliseView bv1 = new BaliseView(b1);
		b1.setMovingMethod(new LinearMethod(2));
		b1.registerMoveEvent(bv1);
		b1.registerSynchronisationStartEvent(bv1); // Enregistrer pour changement de couleur
		b1.registerSynchronisationEndEvent(bv1);
		b1.registerSynchronisationStartEvent(syncLinePanel); // Enregistrer pour dessiner la ligne
		b1.registerSynchronisationEndEvent(syncLinePanel);
		balises.add(b1);
		baliseViews.add(bv1);
		
		// Balise 2 : Mouvement statique (immobile EN PROFONDEUR)
		Balise b2 = new Balise(300, OCEAN_START_Y + 220, 0, "Balise_Static"); // Y=520 (profondeur)
		BaliseView bv2 = new BaliseView(b2);
		b2.setMovingMethod(new StaticMethod(300, OCEAN_START_Y + 220)); // Profondeur fixe
		b2.registerMoveEvent(bv2);
		b2.registerSynchronisationStartEvent(bv2);
		b2.registerSynchronisationEndEvent(bv2);
		b2.registerSynchronisationStartEvent(syncLinePanel);
		b2.registerSynchronisationEndEvent(syncLinePanel);
		balises.add(b2);
		baliseViews.add(bv2);

		// Balise 3 : Mouvement sinusoïdal (EN PROFONDEUR)
		Balise b3 = new Balise(450, OCEAN_START_Y + 180, 1, "Balise_Sinusoidal"); // Y=480 (profondeur)
		BaliseView bv3 = new BaliseView(b3);
		b3.setMovingMethod(new SinusoidalMethod(2, 40, 2)); // Ondule en profondeur
		b3.registerMoveEvent(bv3);
		b3.registerSynchronisationStartEvent(bv3);
		b3.registerSynchronisationEndEvent(bv3);
		b3.registerSynchronisationStartEvent(syncLinePanel);
		b3.registerSynchronisationEndEvent(syncLinePanel);
		balises.add(b3);
		baliseViews.add(bv3);

		// Balise 4 : Mouvement vertical
		Balise b4 = new Balise(600, OCEAN_START_Y + 200, 1, "Balise_Vertical"); // Y=500 (profondeur)
		BaliseView bv4 = new BaliseView(b4);
		// Yo-yo entre profondeur moyenne (450) et grande profondeur (550)
		b4.setMovingMethod(new VerticalMethod(2, OCEAN_START_Y + 150, OCEAN_START_Y + 250));
		b4.registerMoveEvent(bv4);
		b4.registerSynchronisationStartEvent(bv4);
		b4.registerSynchronisationEndEvent(bv4);
		b4.registerSynchronisationStartEvent(syncLinePanel);
		b4.registerSynchronisationEndEvent(syncLinePanel);
		balises.add(b4);
		baliseViews.add(bv4);

		// ==================== Ajout des éléments à l'interface ====================
		// Z-order (profondeur) : satellites en arrière -> océan (mais transparent pour laisser voir balises) -> balises en avant
		
		// 1. Ajouter les satellites d'abord (zone blanche)
		space.add(sv1);
		space.add(sv2);
		space.add(sv3);
		
		// 2. Ajouter les balises (par-dessus les satellites mais sous l'océan visuellement)
		space.add(bv1);
		space.add(bv2);
		space.add(bv3);
		space.add(bv4);
		
		// 3. Ajouter la zone océan en dernier (en avant mais transparent/semi-transparent pour laisser voir les balises)
		// L'océan sera dessiné mais les balises resteront actives par-dessus
		space.add(oceanZone);
		
		// 4. Ajouter le panel de lignes de synchronisation (par-dessus tout)
		space.add(syncLinePanel);

		// Forcer un revalidate/repaint après l'ajout initial des composants
		space.revalidate();
		space.repaint();

		space.openInWindow();

		// ==================== Boucle de simulation (dans un thread séparé) ====================
		
		Thread simulationThread = new Thread(() -> {
			while (true) {

				// Déplacer les balises (dans l'océan)
				for (Balise balise : balises) {
					balise.move();
				}

				// Déplacer les satellites (dans l'espace)
				s1.move(3);
				s2.move(2);
				s3.move(2);
				
				// ==================== DÉTECTION ET SYNCHRONISATION ====================
				// Pour chaque balise en attente (REMONTEE), vérifier les satellites disponibles
				for (Balise balise : balises) {
					if (balise.getState() == BaliseState.REMONTEE) {
						// Tenter la synchronisation avec chaque satellite
						for (Satellite satellite : satellites) {
							if (balise.trySynchronize(satellite)) {
								// Synchronisation réussie !
								System.out.println("✅ SYNCHRONISATION: " + balise.getId() + 
								                   " <-> " + satellite.getId());
								break;  // Une balise ne se synchronise qu'avec un seul satellite à la fois
							}
						}
					}
					// Afficher le changement d'état pour debug
					if (balise.isSynchronizing()) {
						System.out.println("🔄 TRANSFERT: " + balise.getId() + 
						                   " -> " + balise.getCurrentSatellite().getId() + 
						                   " (Mémoire restante: " + balise.getMemory() + ")");
					}
				}
				
				// Les positions visuelles sont mises à jour automatiquement via les événements
				// (onBaliseMove et onSatelliteMove)
				
				// Forcer repaint du conteneur
				space.repaint();
				
				// Redessiner le panel de lignes pour mettre à jour les positions
				syncLinePanel.repaint();

				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		simulationThread.setDaemon(true);  // Le thread se ferme avec l'application
		simulationThread.start();  // Lancer la simulation
	}
}


