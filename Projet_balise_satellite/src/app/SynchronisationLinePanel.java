package app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

import balise.Balise;
import balise.SynchronisationEndEvent;
import balise.SynchronisationListener;
import balise.SynchronisationStartEvent;
import satellite.Satellite;

/**
 * Panel transparent qui dessine les lignes de synchronisation entre balises et satellites.
 * Ce panel se superpose à tous les autres composants.
 */
public class SynchronisationLinePanel extends JPanel implements SynchronisationListener {
    
    // Map pour stocker les paires balise-satellite en synchronisation
    private Map<String, SyncPair> activeSynchronisations = new HashMap<>();
    
    public SynchronisationLinePanel() {
        setOpaque(false); // Transparent pour voir à travers
        setLayout(null); // Pas de layout
    }
    
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
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Configurer le style de la ligne
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2)); // Ligne épaisse
        
        // Dessiner une ligne pour chaque synchronisation active
        for (SyncPair pair : activeSynchronisations.values()) {
            int baliseX = pair.balise.getX() + 15; // Centre de la balise (30/2)
            int baliseY = pair.balise.getY() + 15;
            int satelliteX = pair.satellite.getX() + 12; // Centre du satellite (25/2)
            int satelliteY = pair.satellite.getY() + 12;
            
            g2d.drawLine(baliseX, baliseY, satelliteX, satelliteY);
        }
    }
    
    /**
     * Classe interne pour stocker une paire balise-satellite
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
