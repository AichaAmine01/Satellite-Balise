package balise;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import src.nicellipse.component.NiImage;

/**
 * Vue graphique d'une balise (Pattern Observable).
 * 
 * Cette classe est responsable de l'affichage graphique d'une balise et
 * réagit aux événements émis par le modèle Balise pour mettre à jour l'affichage.
 * 
 * Implémente :
 * - BaliseListener : pour recevoir les événements de mouvement
 * - SynchronisationListener : pour recevoir les événements de synchronisation
 * 
 * Séparation Modèle-Vue : BaliseView observe Balise mais ne modifie jamais son état.
 * 
 * @see Balise
 * @see BaliseListener
 * @see SynchronisationListener
 */
public class BaliseView extends NiImage implements BaliseListener, SynchronisationListener {
    private final Balise balise;
    private static final int ICON_WIDTH = 50;
    private static final int ICON_HEIGHT = 50;

    public Balise getBalise() {
        return this.balise;
    }

    /**
     * Constructeur de la vue d'une balise.
     * Charge l'image de la balise et initialise sa position.
     * 
     * @param balise Le modèle de balise à afficher
     * @throws IOException Si l'image balise.png n'est pas trouvée
     */
    public BaliseView(Balise balise) throws IOException {
        super(loadAndResizeBaliseImage());
        this.balise = balise;
        this.setOpaque(false);
        this.setBounds(balise.getX(), balise.getY(), ICON_WIDTH, ICON_HEIGHT);
    }

    /**
     * Charge et redimensionne l'image de la balise.
     * 
     * @return L'image redimensionnée aux dimensions voulues
     * @throws IOException Si le fichier balise.png n'est pas trouvé
     */
    private static Image loadAndResizeBaliseImage() throws IOException {
        // Essai dans resources/
        File f = new File("resources" + File.separator + "balise.png");
        if (!f.exists()) {
            // Essai chemin alternatif
            f = new File("Projet_balise_satellite" + File.separator + "resources" + File.separator + "balise.png");
            if (!f.exists()) {
                // Essai chemin complet depuis le dossier Satellite-Balise
                f = new File("Satellite-Balise" + File.separator + "Projet_balise_satellite" + File.separator + "resources" + File.separator + "balise.png");
                if (!f.exists()) {
                    throw new IOException("balise.png not found in resources");
                }
            }
        }
        // Charger et redimensionner l'image
        Image originalImage = ImageIO.read(f);
        return originalImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
    }

    /**
     * Réagit aux événements de mouvement de la balise.
     * Met à jour la position graphique de la vue pour refléter le déplacement.
     * 
     * @param event L'événement contenant la nouvelle position de la balise
     */
    @Override
    public void onBaliseMove(BaliseMoveEvent event) {
        Balise source = (Balise) event.getSource();
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
        System.out.println(" Vue: " + balise.getId() + " commence la synchronisation");
    }

    /**
     * Réagit à la fin d'une synchronisation.
     * Affiche un message console pour tracer l'événement.
     * 
     * @param event L'événement de fin de synchronisation
     */
    @Override
    public void onSynchronisationEnd(SynchronisationEndEvent event) {
        System.out.println("Vue: " + balise.getId() + " termine la synchronisation");
    }

}

