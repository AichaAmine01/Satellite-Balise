package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import balise.Balise;
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
	
	public static void main(String[] args) throws InterruptedException {
		// Créer la fenêtre principale
		NiSpace space = new NiSpace("Simulation Balises & Satellites", new Dimension(800, 600));
		space.setBackground(Color.WHITE);

		// ==================== SATELLITES - Dans l'espace (zone blanche) ====================
		
		Satellite s1 = new Satellite(100, 50, 1);
		SatelliteView sv1 = new SatelliteView(s1);
		sv1.setBackground(Color.GRAY);
		sv1.setSize(25, 25);
		sv1.setLocation(s1.getX(), s1.getY());
		s1.registerMoveEvent(sv1);

		Satellite s2 = new Satellite(400, 100, -1);
		SatelliteView sv2 = new SatelliteView(s2);
		sv2.setBackground(Color.GRAY);
		sv2.setSize(25, 25);
		sv2.setLocation(s2.getX(), s2.getY());
		s2.registerMoveEvent(sv2);

		Satellite s3 = new Satellite(650, 150, 1);
		SatelliteView sv3 = new SatelliteView(s3);
		sv3.setBackground(Color.GRAY);
		sv3.setSize(25, 25);
		sv3.setLocation(s3.getX(), s3.getY());
		s3.registerMoveEvent(sv3);

		// ==================== ZONES (Visuelle) ====================
		
		// Zone océan (bleu) - utiliser un JPanel personnalisé qui peint juste le background
		// sans couvrir les composants enfants (balises)
		JPanel oceanZone = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// Peindre juste le background bleu, pas les enfants
				g.setColor(Color.BLUE);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		oceanZone.setOpaque(false);  // Ne pas remplir le fond par défaut
		oceanZone.setSize(800, 300);  // Hauteur: 600 - 300 = 300
		oceanZone.setLocation(0, OCEAN_START_Y);
		oceanZone.setLayout(null);  // Pas de layout pour qu'il ne gère pas les enfants

		// ==================== BALISES - Dans l'océan (zone bleue) ====================
		
		// Balise 1 : Mouvement linéaire horizontal
		Balise b1 = new Balise(100, OCEAN_START_Y + 50, 1, "Balise_Linear");
		BaliseView bv1 = new BaliseView(b1);
		bv1.setBackground(Color.YELLOW);
		bv1.setSize(30, 30);
		bv1.setLocation(b1.getX(), b1.getY());
		b1.setMovingMethod(new LinearMethod(2));
		b1.registerMoveEvent(bv1);
		
		// Balise 2 : Mouvement statique (immobile)
		Balise b2 = new Balise(300, OCEAN_START_Y + 80, 0, "Balise_Static");
		BaliseView bv2 = new BaliseView(b2);
		bv2.setBackground(Color.YELLOW);
		bv2.setSize(30, 30);
		bv2.setLocation(b2.getX(), b2.getY());
		b2.setMovingMethod(new StaticMethod(300, OCEAN_START_Y + 80));
		b2.registerMoveEvent(bv2);

		// Balise 3 : Mouvement sinusoïdal
		Balise b3 = new Balise(450, OCEAN_START_Y + 100, 1, "Balise_Sinusoidal");
		BaliseView bv3 = new BaliseView(b3);
		bv3.setBackground(Color.YELLOW);
		bv3.setSize(30, 30);
		bv3.setLocation(b3.getX(), b3.getY());
		b3.setMovingMethod(new SinusoidalMethod(2, 40, 2));
		b3.registerMoveEvent(bv3);

		// Balise 4 : Mouvement vertical
		Balise b4 = new Balise(600, OCEAN_START_Y + 50, 1, "Balise_Vertical");
		BaliseView bv4 = new BaliseView(b4);
		bv4.setBackground(Color.YELLOW);
		bv4.setSize(30, 30);
		bv4.setLocation(b4.getX(), b4.getY());
		b4.setMovingMethod(new VerticalMethod(2, OCEAN_START_Y + 30, OCEAN_START_Y + 250));
		b4.registerMoveEvent(bv4);

		// ==================== Ajout des éléments à l'interface (ORDRE IMPORTANT!) ====================
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

		// Forcer un revalidate/repaint après l'ajout initial des composants
		space.revalidate();
		space.repaint();

		space.openInWindow();

		// ==================== Boucle de simulation (dans un thread séparé) ====================
		
		Thread simulationThread = new Thread(() -> {
			int iterationCount = 0;
			while (true) {

				// Déplacer les balises (dans l'océan)
				b1.move();
				b2.move();
				b3.move();
				b4.move();

				// Déplacer les satellites (dans l'espace)
				s1.move(3);
				s2.move(2);
				s3.move(2);
				
				// Mettre à jour les positions visuelles des balises
				bv1.setBounds(b1.getX(), b1.getY(), 30, 30);
				bv2.setBounds(b2.getX(), b2.getY(), 30, 30);
				bv3.setBounds(b3.getX(), b3.getY(), 30, 30);
				bv4.setBounds(b4.getX(), b4.getY(), 30, 30);
				
				// Mettre à jour les positions visuelles des satellites
				sv1.setBounds(s1.getX(), s1.getY(), 25, 25);
				sv2.setBounds(s2.getX(), s2.getY(), 25, 25);
				sv3.setBounds(s3.getX(), s3.getY(), 25, 25);
				
				// Forcer repaint du conteneur
				space.repaint();

				iterationCount++;
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


