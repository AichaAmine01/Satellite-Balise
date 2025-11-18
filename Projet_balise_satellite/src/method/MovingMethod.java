package method;

import balise.Balise;

/**
 * Interface définissant le contrat d'une stratégie de mouvement (Pattern STRATÉGIE).
 * 
 * <p>Permet d'encapsuler différents algorithmes de déplacement pour les balises.
 * Chaque implémentation concrète définit un comportement de mouvement spécifique :
 * <ul>
 *   <li>{@link LinearMethod} : Mouvement horizontal linéaire avec rebonds</li>
 *   <li>{@link SinusoidalMethod} : Mouvement sinusoïdal (ondulation)</li>
 *   <li>{@link VerticalMethod} : Mouvement vertical yo-yo</li>
 *   <li>{@link StaticMethod} : Position fixe (immobile)</li>
 * </ul>
 * 
 * <p><b>Pattern Stratégie :</b> Permet de changer dynamiquement l'algorithme de mouvement
 * sans modifier la classe {@link Balise}. La balise délègue son déplacement à la stratégie
 * via {@link Balise#move()}.
 * 
 * @see Balise#setMovingMethod(MovingMethod)
 * @see Balise#move()
 */
public interface MovingMethod {
    /**
     * Déplace la balise selon l'algorithme de cette stratégie.
     * 
     * <p>Cette méthode est appelée à chaque itération de la boucle d'animation
     * (toutes les 30ms) lorsque la balise est en état COLLECTE.
     * 
     * @param balise La balise à déplacer (modifie sa position X et/ou Y)
     */
    void move(Balise balise);
}
