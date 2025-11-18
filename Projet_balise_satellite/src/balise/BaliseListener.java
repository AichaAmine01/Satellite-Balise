package balise;

/**
 * Interface pour écouter les mouvements d'une balise.
 * Les objets implémentant cette interface (comme BaliseView) sont notifiés
 * à chaque déplacement de la balise pour mettre à jour l'affichage.
 * 
 * @see BaliseMoveEvent
 * @see BaliseView
 */
public interface BaliseListener {
    /**
     * Méthode appelée lorsqu'une balise se déplace.
     * 
     * @param event L'événement contenant la balise qui s'est déplacée
     */
    void onBaliseMove(BaliseMoveEvent event);
}
