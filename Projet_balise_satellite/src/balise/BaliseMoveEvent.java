package balise;

import announcer.AbstractEvent;

/**
 * Événement déclenché lorsqu'une balise se déplace.
 * Cet événement est émis à chaque itération de la boucle d'animation (30ms)
 * pour notifier les observateurs (notamment BaliseView) du changement de position.
 * 
 * @see BaliseListener
 * @see Balise#move()
 */
public class BaliseMoveEvent extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur créant un événement de mouvement de balise.
     * 
     * @param source La balise qui s'est déplacée
     */
    public BaliseMoveEvent(Object source) {
        super(source);
    }

    /**
     * Transmet l'événement au listener en appelant onBaliseMove().
     * 
     * @param target Le BaliseListener à notifier
     */
    @Override
    public void sentTo(Object target) {
        ((BaliseListener) target).onBaliseMove(this);
    }
}
