package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
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
	private static Random random = new Random();
	
	public static void main(String[] args) throws InterruptedException {
		// Créer la fenêtre principale
		NiSpace space = new NiSpace("Simulation Balises & Satellites", new Dimension(800, 650));
		space.setBackground(Color.WHITE);
		space.setLayout(null);

		// Listes pour gérer les balises et satellites
		List<Balise> balises = new ArrayList<>();
		List<BaliseView> baliseViews = new ArrayList<>();
		List<Satellite> satellites = new ArrayList<>();
		List<SatelliteView> satelliteViews = new ArrayList<>();

		// ==================== ZONES (Visuelle) ====================
		
		// Zone océan (bleu)
		JPanel oceanZone = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.BLUE);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		oceanZone.setOpaque(false);
		oceanZone.setSize(800, 300);
		oceanZone.setLocation(0, OCEAN_START_Y);
		oceanZone.setLayout(null);
		// Ajouter l'océan en premier (arrière-plan)
		space.add(oceanZone);
		space.setComponentZOrder(oceanZone, space.getComponentCount() - 1); // Forcer l'océan en arrière-plan

		// ==================== PANNEAU DE CONTRÔLE ====================
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		controlPanel.setSize(800, 50);
		controlPanel.setLocation(0, 600);
		controlPanel.setBackground(Color.LIGHT_GRAY);
		
		// Bouton pour ajouter une balise
		JButton addBaliseBtn = new JButton("Ajouter Balise");
		addBaliseBtn.addActionListener(e -> {
			// Créer une balise avec un type de mouvement aléatoire
			int baliseType = random.nextInt(4);
			int x = 50 + random.nextInt(700);
			int y = OCEAN_START_Y + 50 + random.nextInt(200);
			
			Balise balise = new Balise(x, y, random.nextBoolean() ? 1 : -1);
			BaliseView baliseView = new BaliseView(balise);
			baliseView.setColor(Color.YELLOW);
			baliseView.setBounds(balise.getX(), balise.getY(), 30, 30);
			
			// Assigner une stratégie de mouvement
			switch(baliseType) {
				case 0: // Linéaire
					balise.setMovingMethod(new LinearMethod(2));
					break;
				case 1: // Vertical
					balise.setMovingMethod(new VerticalMethod(2, OCEAN_START_Y + 30, OCEAN_START_Y + 250));
					break;
				case 2: // Sinusoïdal
					balise.setMovingMethod(new SinusoidalMethod(2, 40, 2));
					break;
				case 3: // Statique
					balise.setMovingMethod(new StaticMethod(x, y));
					break;
			}
			
			balise.registerMoveEvent(baliseView);
			balises.add(balise);
			baliseViews.add(baliseView);
			space.add(baliseView);
			space.setComponentZOrder(baliseView, 0); // Mettre la balise au premier plan
			space.revalidate();
			space.repaint();
			
			System.out.println("✨ Balise ajoutée : " + balise.getId() + " (Type: " + 
				(baliseType == 0 ? "Linéaire" : baliseType == 1 ? "Vertical" : 
				 baliseType == 2 ? "Sinusoïdal" : "Statique") + ")");
		});
		
		// Bouton pour ajouter un satellite
		JButton addSatelliteBtn = new JButton("Ajouter Satellite");
		addSatelliteBtn.addActionListener(e -> {
			int x = random.nextInt(800);
			int y = 50 + random.nextInt(200);
			int direction = random.nextBoolean() ? 1 : -1;
			
			Satellite satellite = new Satellite(x, y, direction);
			satellite.setScreenWidth(800);
			SatelliteView satelliteView = new SatelliteView(satellite);
			satelliteView.setColor(Color.GRAY);
			satelliteView.setBounds(satellite.getX(), satellite.getY(), 25, 25);
			
			satellite.registerMoveEvent(satelliteView);
			satellites.add(satellite);
			satelliteViews.add(satelliteView);
			space.add(satelliteView);
			space.revalidate();
			space.repaint();
			
			System.out.println("🛰️ Satellite ajouté : " + satellite.getId());
		});
		
		controlPanel.add(addBaliseBtn);
		controlPanel.add(addSatelliteBtn);
		space.add(controlPanel);

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

				// Déplacer les satellites (dans l'espace) avec des vitesses variables
				for (Satellite satellite : satellites) {
					// Vitesse aléatoire entre 1 et 3 pour chaque satellite
					int speed = 1 + random.nextInt(3);
					satellite.move(speed);
				}
				
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

				// ==================== MISE À JOUR DES COULEURS (VISUEL) ====================
				// Par défaut, réinitialiser les couleurs
				for (BaliseView bv : baliseViews) {
					bv.setColor(Color.YELLOW);
				}
				for (SatelliteView sv : satelliteViews) {
					sv.setColor(Color.GRAY);
				}
				// Mettre en vert les paires en synchronisation
				for (Balise balise : balises) {
					if (balise.isSynchronizing() && balise.getCurrentSatellite() != null) {
						Satellite sat = balise.getCurrentSatellite();
						// Trouver la vue de la balise
						for (BaliseView bv : baliseViews) {
							if (bv.getBalise() == balise) {
								bv.setColor(Color.GREEN);
								break;
							}
						}
						// Trouver la vue du satellite
						for (SatelliteView sv : satelliteViews) {
							if (sv.getSatellite() == sat) {
								sv.setColor(Color.GREEN);
								break;
							}
						}
					}
				}
				
				// Mettre à jour les positions visuelles des balises
				for (int i = 0; i < balises.size(); i++) {
					Balise b = balises.get(i);
					BaliseView bv = baliseViews.get(i);
					bv.setBounds(b.getX(), b.getY(), 30, 30);
				}
				
				// Mettre à jour les positions visuelles des satellites
				for (int i = 0; i < satellites.size(); i++) {
					Satellite s = satellites.get(i);
					SatelliteView sv = satelliteViews.get(i);
					sv.setBounds(s.getX(), s.getY(), 25, 25);
				}
				
				// Forcer repaint du conteneur
				space.repaint();

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
