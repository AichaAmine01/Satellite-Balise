package method;

import balise.Balise;

/**
 * Interface définissant le contrat d'une stratégie de mouvement (Pattern STRATÉGIE).
 * 
 * Permet d'encapsuler différents algorithmes de déplacement pour les balises.
 * Chaque implémentation concrète définit un comportement de mouvement spécifique :
 * 
 * Pattern Stratégie : Permet de changer dynamiquement l'algorithme de mouvement
 * sans modifier la classe. La balise délègue son déplacement à la stratégie
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
