package balise;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import src.nicellipse.component.NiImage;

public class BaliseView extends NiImage implements BaliseListener {
    private final Balise balise;
    private static final int ICON_WIDTH = 50;
    private static final int ICON_HEIGHT = 50;

    public BaliseView(Balise balise) throws IOException {
        super(loadAndResizeBaliseImage());
        this.balise = balise;
        this.setOpaque(false);
        this.setBounds(balise.getX(), balise.getY(), ICON_WIDTH, ICON_HEIGHT);
    }

    private static Image loadAndResizeBaliseImage() throws IOException {
        // Essai dans resources/
        File f = new File("resources" + File.separator + "balise.png");
        if (!f.exists()) {
            // Essai chemin alternatif
            f = new File("Projet_balise_satellite" + File.separator + "resources" + File.separator + "balise.png");
            if (!f.exists()) {
                throw new IOException("balise.png not found in resources");
            }
        }
        // Charger et redimensionner l'image
        Image originalImage = ImageIO.read(f);
        return originalImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
    }

    @Override
    public void onBaliseMove(BaliseMoveEvent event) {
        Balise source = (Balise) event.getSource();
        int x = source.getX();
        int y = source.getY();
        this.setBounds(x, y, ICON_WIDTH, ICON_HEIGHT);
        this.revalidate();
        this.repaint();
    }

    public Balise getBalise() {
        return balise;
    }
}
