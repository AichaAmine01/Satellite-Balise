package balise;

import announcer.Announcer;
import method.*;
import satellite.Satellite;

/**
 * Classe représentant une balise autonome dans l'océan.
 * 
 * Une balise suit un cycle en 4 phases (Pattern État) :
 * 1. COLLECTE : Se déplace selon sa stratégie (Pattern Stratégie) et collecte des données
 * 2. REMONTEE : Remonte vers la surface lorsque sa mémoire est pleine
 * 3. SYNCHRONISATION : Transfère ses données vers un satellite aligné
 * 4. DESCENTE : Redescend vers sa profondeur initiale pour recommencer
 * 
 * La balise utilise le Pattern Observable pour notifier ses changements :
 * - BaliseMoveEvent : à chaque déplacement
 * - BaliseStateChangeEvent : à chaque changement d'état
 * - SynchronisationStartEvent/EndEvent : début/fin de synchronisation
 * 
 * @see BaliseState
 * @see MovingMethod
 * @see Announcer
 */
public class Balise {
    private int x, y;
    private int direction;
    private String id;                          // Identificateur unique de la balise
    Announcer announcer;
    private method.MovingMethod movingMethod;
    
    // Dimensions de la balise
    private static final int BALISE_SIZE = 30;  // Taille de la balise (largeur et hauteur)
    
    // Gestion des états
    private BaliseState state;                  // État actuel de la balise
    private int memory;                         // Mémoire actuelle (données collectées)
    private int maxMemory;                      // Capacité maximale de mémoire
    private int collectSpeed;                   // Vitesse de collecte (données par move())
    private int initialY;                       // Position Y initiale (profondeur maximale)
    private static final int SURFACE_Y = 290;  // Y où se trouve la surface de l'océan (10 pixels plus haut)
    private static final int OCEAN_BOTTOM = 600; // Fond de l'océan
    private static final int SCREEN_WIDTH = 800;  // Largeur de l'écran
    private int riseSpeed;                      // Vitesse de remontée en pixels par move()
    private int descentSpeed;                   // Vitesse de descente après synchronisation
    
    // Gestion de la synchronisation
    private Satellite currentSatellite;         // Satellite actuellement en synchronisation
    private int transferSpeed;                  // Vitesse de transfert (données par move())
    private static final int SYNC_TOLERANCE = 10; // Tolérance horizontale pour la synchro (pixels)

    /**
     * Constructeur simple de la balise
     * @param x Position horizontale initiale
     * @param y Position verticale initiale (profondeur)
     * @param direction Direction du mouvement (1 ou -1)
     */
    public Balise(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = "Balise_" + Math.abs(x + y + System.nanoTime());
        this.announcer = new Announcer();
        this.state = BaliseState.COLLECTE;      // État initial : collecte
        this.memory = 0;                        // Mémoire initialement vide
        // Variabilité : chaque balise a des caractéristiques différentes
        this.maxMemory = 150 + (int)(Math.random() * 150);     // Capacité entre 150 et 300
        this.collectSpeed = 1 + (int)(Math.random() * 3);      // Vitesse entre 1 et 3 (plus lent)
        this.riseSpeed = 1 + (int)(Math.random() * 3);         // Vitesse de remontée entre 1 et 3
        this.descentSpeed = 1 + (int)(Math.random() * 2);      // Vitesse de descente entre 1 et 2 (lente)
        this.transferSpeed = 5 + (int)(Math.random() * 10);    // Vitesse de transfert entre 5 et 14
        this.initialY = y;                      // Mémoriser la profondeur initiale
        this.currentSatellite = null;           // Pas de satellite en cours
    }

    /**
     * Constructeur complet de la balise
     * @param x Position horizontale initiale
     * @param y Position verticale initiale (profondeur)
     * @param direction Direction du mouvement
     * @param id Identificateur unique
     */
    public Balise(int x, int y, int direction, String id) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
        this.announcer = new Announcer();
        this.state = BaliseState.COLLECTE;      // État initial : collecte
        this.memory = 0;                        // Mémoire initialement vide
        // Variabilité : chaque balise a des caractéristiques différentes
        this.maxMemory = 150 + (int)(Math.random() * 150);     // Capacité entre 150 et 300
        this.collectSpeed = 1 + (int)(Math.random() * 3);      // Vitesse entre 1 et 3 (plus lent)
        this.riseSpeed = 1 + (int)(Math.random() * 3);         // Vitesse de remontée entre 1 et 3
        this.descentSpeed = 1 + (int)(Math.random() * 2);      // Vitesse de descente entre 1 et 2 (lente)
        this.transferSpeed = 5 + (int)(Math.random() * 10);    // Vitesse de transfert entre 5 et 14
        this.initialY = y;                      // Mémoriser la profondeur initiale
        this.currentSatellite = null;           // Pas de satellite en cours
    }

    /**
     * Définit la stratégie de mouvement de la balise (Pattern Stratégie).
     * Cette stratégie est utilisée uniquement en état COLLECTE.
     * 
     * @param movingMethod La stratégie de mouvement à appliquer (LinearMethod, SinusoidalMethod, etc.)
     */
    public void setMovingMethod(method.MovingMethod movingMethod) {
        this.movingMethod = movingMethod;
    }

    /**
     * Exécute un cycle de mouvement de la balise selon son état actuel (Pattern État).
     * 
     * Comportement selon l'état :
     * - COLLECTE : Applique la stratégie de mouvement et collecte des données
     * - REMONTEE : Monte vers la surface à vitesse constante
     * - SYNCHRONISATION : Transfère les données vers le satellite
     * - DESCENTE : Redescend vers la profondeur initiale
     * 
     * Émet un BaliseMoveEvent à la fin de chaque cycle.
     */
    public void move() {
        // Machine à états : comportement différent selon l'état actuel (Pattern État)
        if (state == BaliseState.COLLECTE) {
            // 🔵 ÉTAT 1 : COLLECTE - Déplacement selon stratégie + collecte de données
            // Appliquer la stratégie de mouvement (Pattern Stratégie : Linear, Sinusoidal, Vertical, Static)
            if (movingMethod != null) {
                movingMethod.move(this);
            }
            // Simuler la collecte de données environnementales (température, salinité, etc.)
            // Vitesse variable selon les caractéristiques de chaque balise
            memory += collectSpeed;
            // Vérifier si la mémoire est saturée → déclenchement de la remontée
            if (memory >= maxMemory) {
                setState(BaliseState.REMONTEE);
            }
        } else if (state == BaliseState.REMONTEE) {
            // ⬆️  ÉTAT 2 : REMONTEE - Montée vers la surface pour synchronisation
            // Monter progressivement vers la surface (vitesse variable par balise)
            if (y > SURFACE_Y) {
                y -= riseSpeed;  // Décrémenter Y pour monter (Y=0 en haut)
            } else {
                y = SURFACE_Y;  // Atteindre exactement la surface et attendre un satellite
            }
        } else if (state == BaliseState.SYNCHRONISATION) {
            // 🔄 ÉTAT 3 : SYNCHRONISATION - Transfert des données vers le satellite
            if (currentSatellite != null && memory > 0) {
                // Calculer combien de données transférer ce cycle (limité par transferSpeed)
                int dataToTransfer = Math.min(transferSpeed, memory);
                // Retirer les données de la balise
                memory -= dataToTransfer;
                // Transférer au satellite
                currentSatellite.receiveData(dataToTransfer);
                
                // Vérifier si tout est transféré → fin de synchronisation
                if (memory == 0) {
                    endSynchronisation();  // Libère le satellite et passe en DESCENTE
                }
            }
        } else if (state == BaliseState.DESCENTE) {
            // ⬇️  ÉTAT 4 : DESCENTE - Retour progressif à la profondeur initiale
            if (y < initialY) {
                y += descentSpeed;  // Incrémenter Y pour descendre (Y augmente vers le bas)
                // Vérifier si on a atteint ou dépassé la profondeur cible
                if (y >= initialY) {
                    y = initialY;  // Corriger à la profondeur exacte
                    setState(BaliseState.COLLECTE);  // Reprendre un nouveau cycle de collecte
                }
            } else {
                // Cas rare : déjà à la bonne profondeur (ex: profondeur initiale = surface)
                setState(BaliseState.COLLECTE);
            }
        }
        // 📢 Pattern Observable : Émettre un événement de mouvement à chaque cycle
        // Notifie les vues pour qu'elles se rafraîchissent
        announcer.announce(new BaliseMoveEvent(this));
    }
    
    /**
     * Tente de démarrer une synchronisation avec un satellite.
     * 
     * La synchronisation nécessite 3 conditions simultanées :
     * 1. La balise doit être en état REMONTEE (a fini de remonter)
     * 2. La balise doit être à la surface (y == SURFACE_Y)
     * 3. Le satellite doit être aligné horizontalement (distance <= SYNC_TOLERANCE)
     * 
     * @param satellite Le satellite avec lequel tenter la synchronisation
     * @return true si la synchronisation a démarré, false sinon
     */
    public boolean trySynchronize(Satellite satellite) {
        // Vérifier les 3 conditions de synchronisation
        // Condition 1 : Balise en état REMONTEE (pas en collecte, synchro ou descente)
        // Condition 2 : Balise à la surface (y == SURFACE_Y)
        // Condition 3 : Satellite au-dessus et aligné (isAbove() vérifie distance et disponibilité)
        if (state == BaliseState.REMONTEE && y == SURFACE_Y && 
            satellite.isAbove(this.x, this.y, SYNC_TOLERANCE)) {
            
            // 🔍 DEBUG: Afficher les positions pour tracer les alignements
            int distance = Math.abs(satellite.getX() - this.x);
            System.out.println("🔗 SYNCHRO DÉTECTÉE: " + this.id + 
                             " (X=" + this.x + ") <-> " + satellite.getId() + 
                             " (X=" + satellite.getX() + ") Distance=" + distance + " pixels");
            
            // Démarrer le transfert de données
            startSynchronisation(satellite);
            return true;
        }
        // Si l'une des 3 conditions n'est pas remplie, pas de synchronisation
        return false;
    }
    
    /**
     * Démarre la synchronisation avec un satellite.
     * Change l'état à SYNCHRONISATION et émet un SynchronisationStartEvent.
     * 
     * @param satellite Le satellite avec lequel synchroniser
     */
    private void startSynchronisation(Satellite satellite) {
        this.currentSatellite = satellite;
        satellite.setDisponible(false);  // Le satellite devient occupé
        setState(BaliseState.SYNCHRONISATION);
        // Émettre l'événement de début de synchronisation
        announcer.announce(new SynchronisationStartEvent(this, satellite));
    }
    
    /**
     * Termine la synchronisation et commence la descente progressive.
     * Libère le satellite et change l'état à DESCENTE.
     * Émet un SynchronisationEndEvent.
     */
    private void endSynchronisation() {
        if (currentSatellite != null) {
            // Émettre l'événement de fin de synchronisation
            announcer.announce(new SynchronisationEndEvent(this, currentSatellite));
            currentSatellite.setDisponible(true);  // Le satellite redevient disponible
            currentSatellite = null;
        }
        // Passer en mode DESCENTE pour redescendre progressivement
        setState(BaliseState.DESCENTE);
    }

    /**
     * Enregistre un listener pour les événements de mouvement.
     * 
     * @param o L'objet listener (doit implémenter BaliseListener)
     */
    public void registerMoveEvent(Object o) {
        this.announcer.register(o, BaliseMoveEvent.class);
    }
    
    /**
     * Enregistre un listener pour les événements de début de synchronisation.
     * 
     * @param o L'objet listener (doit implémenter SynchronisationListener)
     */
    public void registerSynchronisationStartEvent(Object o) {
        this.announcer.register(o, SynchronisationStartEvent.class);
    }
    
    /**
     * Enregistre un listener pour les événements de fin de synchronisation.
     * 
     * @param o L'objet listener (doit implémenter SynchronisationListener)
     */
    public void registerSynchronisationEndEvent(Object o) {
        this.announcer.register(o, SynchronisationEndEvent.class);
    }
    
    /**
     * Enregistre un listener pour les événements de changement d'état.
     * 
     * @param o L'objet listener (doit implémenter BaliseStateListener)
     */
    public void registerStateChangeEvent(Object o) {
        this.announcer.register(o, BaliseStateChangeEvent.class);
    }

    /**
     * Initialise la position de la balise.
     * 
     * @param x Position horizontale
     * @param y Position verticale (profondeur)
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    /**
     * Modifie la position X avec détection des bords et rebond.
     * Inverse la direction si la balise atteint un bord de l'écran.
     * 
     * @param x Nouvelle position X
     */
    public void setX(int x) {
        // Limiter X dans les bornes de l'écran en tenant compte de la taille de la balise
        // La balise ne peut pas dépasser les bords avec ses extrémités
        if (x < 0) {
            this.x = 0;
            // Inverser la direction quand on touche le bord gauche
            this.direction = -this.direction;
        } else if (x > SCREEN_WIDTH - BALISE_SIZE) {
            this.x = SCREEN_WIDTH - BALISE_SIZE;
            // Inverser la direction quand on touche le bord droit
            this.direction = -this.direction;
        } else {
            this.x = x;
        }
    }

    public int getY() {
        return y;
    }

    /**
     * Modifie la position Y avec contraintes selon l'état.
     * Limite la balise dans la zone océan (entre surface et fond).
     * 
     * @param y Nouvelle position Y
     */
    public void setY(int y) {
        // Limiter Y dans la zone océan en tenant compte de la taille de la balise
        // La balise ne peut pas dépasser les bords avec ses extrémités
        if (state == BaliseState.COLLECTE || state == BaliseState.DESCENTE) {
            // En collecte ou descente, rester entre la surface et le fond
            if (y < SURFACE_Y) {
                this.y = SURFACE_Y;
            } else if (y > OCEAN_BOTTOM - BALISE_SIZE) {
                this.y = OCEAN_BOTTOM - BALISE_SIZE;
            } else {
                this.y = y;
            }
        } else {
            // En remontée ou synchronisation, peut être à la surface
            if (y < SURFACE_Y) {
                this.y = SURFACE_Y;
            } else if (y > OCEAN_BOTTOM - BALISE_SIZE) {
                this.y = OCEAN_BOTTOM - BALISE_SIZE;
            } else {
                this.y = y;
            }
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public MovingMethod getMovingMethod() {
        return movingMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BaliseState getState() {
        return state;
    }

    /**
     * Change l'état de la balise et émet un BaliseStateChangeEvent.
     * Affiche un message console pour tracer le cycle de vie.
     * 
     * @param newState Le nouvel état de la balise
     */
    public void setState(BaliseState newState) {
        if (this.state != newState) {
            this.state = newState;
            
            // Messages console pour suivre le cycle
            switch (newState) {
                case COLLECTE:
                    System.out.println("🔵 " + id + " : DESCENTE terminée → Début COLLECTE (profondeur: " + y + ")");
                    break;
                case REMONTEE:
                    System.out.println("⬆️  " + id + " : Mémoire PLEINE (" + memory + "/" + maxMemory + ") → REMONTÉE vers surface");
                    break;
                case SYNCHRONISATION:
                    System.out.println("🔄 " + id + " : À la surface → Début SYNCHRONISATION");
                    break;
                case DESCENTE:
                    System.out.println("⬇️  " + id + " : Synchronisation terminée → DESCENTE vers profondeur " + initialY);
                    break;
            }
            
            // Émettre un événement de changement d'état
            announcer.announce(new BaliseStateChangeEvent(this));
        }
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }
    
    public Satellite getCurrentSatellite() {
        return currentSatellite;
    }
    
    public boolean isSynchronizing() {
        return state == BaliseState.SYNCHRONISATION && currentSatellite != null;
    }

    @Override
    public String toString() {
        return id + " [Position: (" + x + ", " + y + "), Direction: " + direction + ", État: " + state.getDescription() + ", Mémoire: " + memory + "/" + maxMemory + "]";
    }

}
