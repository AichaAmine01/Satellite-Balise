package app;

import balise.Balise;
import balise.SynchronisationEndEvent;
import balise.SynchronisationListener;
import balise.SynchronisationStartEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import satellite.Satellite;

/**
 * Panel transparent qui dessine les lignes de synchronisation entre balises et satellites.
 * 
 * Ce panel se superpose à tous les autres composants et réagit aux événements
 * de synchronisation pour afficher/masquer les lignes de connexion.
 * 
 * Fonctionnement :
 * - Écoute les SynchronisationStartEvent pour commencer à dessiner une ligne
 * - Écoute les SynchronisationEndEvent pour arrêter de dessiner la ligne
 * - Redessine automatiquement les lignes à chaque repaint de la fenêtre
 * 
 * @see SynchronisationListener
 * @see SynchronisationStartEvent
 * @see SynchronisationEndEvent
 */
public class SynchronisationLinePanel extends JPanel implements SynchronisationListener {
    
    // Map pour stocker les paires balise-satellite en synchronisation
    private Map<String, SyncPair> activeSynchronisations = new HashMap<>();
    
    /**
     * Constructeur créant un panel transparent sans layout.
     */
    public SynchronisationLinePanel() {
        setOpaque(false); // Transparent pour voir à travers
        setLayout(null); // Pas de layout
    }
    
    /**
     * Réagit au début d'une synchronisation.
     * Enregistre la paire balise-satellite et redessine le panel.
     * 
     * @param event L'événement de début de synchronisation
     */
    @Override
    public void onSynchronisationStart(SynchronisationStartEvent event) {
        Balise balise = event.getBalise();
        Satellite satellite = event.getSatellite();
        String key = balise.getId() + "-" + satellite.getId();
        
        // Enregistrer la paire en synchronisation
        activeSynchronisations.put(key, new SyncPair(balise, satellite));
        
        System.out.println("📡 Ligne de synchro: " + balise.getId() + " <-> " + satellite.getId());
        repaint(); // Redessiner le panel
    }
    
    /**
     * Réagit à la fin d'une synchronisation.
     * Retire la paire balise-satellite et redessine le panel.
     * 
     * @param event L'événement de fin de synchronisation
     */
    @Override
    public void onSynchronisationEnd(SynchronisationEndEvent event) {
        Balise balise = event.getBalise();
        Satellite satellite = event.getSatellite();
        String key = balise.getId() + "-" + satellite.getId();
        
        // Retirer la paire de la map
        activeSynchronisations.remove(key);
        
        System.out.println("📡 Fin ligne de synchro: " + balise.getId() + " <-> " + satellite.getId());
        repaint(); // Redessiner le panel
    }
    
    /**
     * Dessine toutes les lignes de synchronisation actives.
     * Appelé automatiquement par Swing lors du repaint.
     * 
     * @param g Le contexte graphique
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Configurer le style de la ligne (rouge, épaisseur 2)
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        
        // Dessiner une ligne pour chaque synchronisation active
        for (SyncPair pair : activeSynchronisations.values()) {
            int baliseX = pair.balise.getX() + 25; // Centre de la balise (50/2)
            int baliseY = pair.balise.getY(); // Haut de la balise
            int satelliteX = pair.satellite.getX() + 22; // Centre horizontal du satellite (45/2)
            int satelliteY = pair.satellite.getY() + 45; // Bas du satellite (hauteur 45)
            
            g2d.drawLine(baliseX, baliseY, satelliteX, satelliteY);
        }
    }
    
    /**
     * Classe interne pour stocker une paire balise-satellite en synchronisation.
     */
    private static class SyncPair {
        Balise balise;
        Satellite satellite;
        
        SyncPair(Balise balise, Satellite satellite) {
            this.balise = balise;
            this.satellite = satellite;
        }
    }
}
