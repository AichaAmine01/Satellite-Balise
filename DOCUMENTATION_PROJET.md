# Projet Balise-Satellite

## Vue d'ensemble

Simulation d'un système de collecte de données océanographiques par des balises autonomes communicant avec des satellites en orbite. Le projet met en œuvre **3 design patterns majeurs** pour gérer le cycle de vie des balises, leurs stratégies de mouvement et la communication événementielle.

---


![Simulation](Projet_balise_satellite/resources/Simulation.png)


---

## Organisation des Packages

```
src/
├── announcer/          # Pattern Observable - Gestion des événements
├── app/                # Point d'entrée et interface graphique
├── balise/             # Modèle des balises autonomes
├── method/             # Pattern Stratégie - Algorithmes de mouvement
└── satellite/          # Modèle des satellites
```

---

## Package `announcer` - Pattern Observable

### Rôle
Implémente le **Pattern Observable** (médiateur d'événements) permettant la communication découplée entre les modèles (Balise, Satellite) et les vues (BaliseView, SatelliteView).

### Classes

#### `Announcer`
**Description** : Gestionnaire central des événements. Maintient un registre des listeners et distribue les événements.

**Attributs** :
- `registrationIndex : Map<Class, List<Object>>` - Index des listeners par type d'événement

**Méthodes principales** :
- `register(Object listener, Class eventClass)` - Abonne un listener à un type d'événement
- `unregister(Object listener, Class eventClass)` - Désabonne un listener
- `announce(AbstractEvent event)` - Diffuse un événement à tous les listeners abonnés

#### `AbstractEvent`
**Description** : Classe abstraite pour tous les événements. Utilise le **Double Dispatch Pattern**.

**Méthodes** :
- `sentTo(Object listener)` - Méthode abstraite pour transmettre l'événement au listener approprié

---

## Package `balise` - Modèle des Balises

### Rôle
Représente les balises océanographiques autonomes qui collectent des données et se synchronisent avec les satellites.

### Classes

#### `Balise`
**Description** : Balise autonome suivant un cycle en 4 phases (Pattern État). Contient son propre `Announcer` (composition).

**Attributs** :
- `x, y : int` - Position dans l'océan
- `state : BaliseState` - État actuel (COLLECTE, REMONTEE, SYNCHRONISATION, DESCENTE)
- `memory : int` - Données collectées actuellement
- `maxMemory : int` - Capacité maximale (150-300, variable par balise)
- `movingMethod : MovingMethod` - Stratégie de mouvement (Pattern Stratégie)
- `announcer : Announcer` - Gestionnaire d'événements (composition)
- `currentSatellite : Satellite` - Satellite en cours de synchronisation
- `collectSpeed : int` - Vitesse de collecte (1-3)
- `transferSpeed : int` - Vitesse de transfert (5-14)

**Méthodes principales** :
- `move()` - Exécute un cycle selon l'état actuel (machine à états)
- `trySynchronize(Satellite)` - Tente de démarrer une synchronisation (3 conditions)
- `setMovingMethod(MovingMethod)` - Définit la stratégie de mouvement
- `setState(BaliseState)` - Change l'état et émet un événement

**Cycle de vie** :
1. **COLLECTE** : Déplacement selon stratégie + collecte de données
2. **REMONTEE** : Montée vers la surface quand mémoire pleine
3. **SYNCHRONISATION** : Transfert des données vers satellite aligné
4. **DESCENTE** : Retour à la profondeur initiale

#### `BaliseState` (Enum)
**Description** : États possibles d'une balise (Pattern État).

**Valeurs** :
- `COLLECTE` - Collecte de données en profondeur
- `REMONTEE` - Remontée vers la surface
- `SYNCHRONISATION` - Transfert de données au satellite
- `DESCENTE` - Retour à la profondeur initiale

**Méthode** :
- `getDescription()` - Retourne une description textuelle de l'état

#### `BaliseView`
**Description** : Vue graphique d'une balise. Implémente 2 interfaces de listener.

**Interfaces implémentées** :
- `BaliseListener` - Reçoit les événements de mouvement
- `BaliseStateListener` - Reçoit les événements de changement d'état

**Méthodes** :
- `onBaliseMove(BaliseMoveEvent)` - Redessine la balise à sa nouvelle position
- `onBaliseStateChange(BaliseStateChangeEvent)` - Change la couleur selon l'état
- `paint(Graphics)` - Affiche la balise (triangle coloré) et sa barre de mémoire

### Événements

#### `BaliseMoveEvent`
**Description** : Événement émis à chaque déplacement de balise.

**Méthode** :
- `sentTo(Object)` - Transmet l'événement à `BaliseListener.onBaliseMove()`

#### `BaliseStateChangeEvent`
**Description** : Événement émis lors d'un changement d'état.

**Méthode** :
- `sentTo(Object)` - Transmet l'événement à `BaliseStateListener.onBaliseStateChange()`

#### `SynchronisationStartEvent`
**Description** : Événement émis au début d'une synchronisation.

**Attributs** :
- `balise : Balise` - Balise concernée
- `satellite : Satellite` - Satellite concerné

**Méthode** :
- `sentTo(Object)` - Transmet à `SynchronisationListener.onSynchronisationStart()`

#### `SynchronisationEndEvent`
**Description** : Événement émis à la fin d'une synchronisation.

**Attributs** :
- `balise : Balise` - Balise concernée
- `satellite : Satellite` - Satellite concerné

**Méthode** :
- `sentTo(Object)` - Transmet à `SynchronisationListener.onSynchronisationEnd()`

### Interfaces de Listener

#### `BaliseListener`
**Méthode** :
- `onBaliseMove(BaliseMoveEvent)` - Appelé à chaque mouvement de balise

#### `BaliseStateListener`
**Méthode** :
- `onBaliseStateChange(BaliseStateChangeEvent)` - Appelé à chaque changement d'état

#### `SynchronisationListener`
**Méthodes** :
- `onSynchronisationStart(SynchronisationStartEvent)` - Début de synchronisation
- `onSynchronisationEnd(SynchronisationEndEvent)` - Fin de synchronisation

---

## Package `satellite` - Modèle des Satellites

### Rôle
Représente les satellites en orbite qui se déplacent horizontalement et reçoivent les données des balises.

### Classes

#### `Satellite`
**Description** : Satellite en orbite avec mouvement horizontal et effet de boucle infinie (wrap-around).

**Attributs** :
- `x, y : int` - Position (Y fixe en orbite, X variable)
- `direction : int` - Direction de déplacement (1 = droite, -1 = gauche)
- `disponible : boolean` - Indique si le satellite peut synchroniser
- `dataReceived : int` - Quantité totale de données reçues
- `announcer : Announcer` - Gestionnaire d'événements (composition)
- `screenWidth : int` - Largeur de l'écran pour le wrap-around

**Méthodes principales** :
- `move(int gap)` - Déplace le satellite avec effet de boucle infinie
- `isAbove(int baliseX, int baliseY, int tolerance)` - Vérifie l'alignement avec une balise (2 conditions)
- `receiveData(int amount)` - Reçoit des données d'une balise
- `setDisponible(boolean)` - Change la disponibilité (occupé/libre)

**Comportement wrap-around** :
- Si X > largeur écran → réapparaît à gauche (X = 0)
- Si X < 0 → réapparaît à droite (X = largeur)

#### `SatelliteView`
**Description** : Vue graphique d'un satellite. Implémente 1 interface de listener.

**Interface implémentée** :
- `SatelliteListener` - Reçoit les événements de mouvement

**Méthodes** :
- `onSatelliteMove(SatelliteMoveEvent)` - Redessine le satellite
- `paint(Graphics)` - Affiche le satellite (rectangle avec antennes)

### Événements

#### `SatelliteMoveEvent`
**Description** : Événement émis à chaque déplacement de satellite.

**Méthode** :
- `sentTo(Object)` - Transmet à `SatelliteListener.onSatelliteMove()`

### Interfaces de Listener

#### `SatelliteListener`
**Méthode** :
- `onSatelliteMove(SatelliteMoveEvent)` - Appelé à chaque mouvement de satellite

---

## Package `method` - Pattern Stratégie

### Rôle
Implémente le **Pattern Stratégie** permettant de définir différents algorithmes de mouvement pour les balises de manière interchangeable.

### Interface

#### `MovingMethod`
**Description** : Interface définissant le contrat pour les stratégies de mouvement.

**Méthode** :
- `move(Balise balise)` - Calcule et applique le mouvement à une balise

### Implémentations

#### `LinearMethod`
**Description** : Mouvement linéaire horizontal à vitesse constante.

**Comportement** :
- Déplace la balise horizontalement selon sa direction
- Inverse la direction aux bords de l'écran (rebond)
- Vitesse : 2 pixels par cycle

#### `SinusoidalMethod`
**Description** : Mouvement sinusoïdal (oscillation verticale + déplacement horizontal).

**Attributs** :
- `frequency : double` - Fréquence de l'oscillation
- `amplitude : int` - Amplitude de l'oscillation verticale
- `time : int` - Compteur pour calculer la phase
- `initialY : int` - Position Y initiale mémorisée

**Comportement** :
- Déplacement horizontal linéaire
- Oscillation verticale selon la formule : `Y = initialY + amplitude × sin(2π × frequency × time)`
- Mémorise la position initiale pour éviter l'accumulation d'erreurs

#### `VerticalMethod`
**Description** : Mouvement vertical en yo-yo (monte et descend).

**Attributs** :
- `speed : int` - Vitesse verticale (1-3 pixels)
- `maxDepth : int` - Profondeur maximale
- `currentDirection : int` - Direction verticale (1 = descend, -1 = remonte)

**Comportement** :
- Déplacement vertical uniquement
- Inverse la direction aux limites (surface et profondeur max)
- Mode yo-yo continu

#### `StaticMethod`
**Description** : Pas de mouvement (balise fixe).

**Comportement** :
- La balise reste à sa position initiale
- Utilisé pour simuler des balises ancrées

---

## Package `app` - Application et Interface

### Classes

#### `MainStrategy`
**Description** : Point d'entrée de l'application. Initialise et lance la simulation.

**Méthodes** :
- `main(String[] args)` - Crée l'interface graphique et démarre la boucle d'animation
- Boucle d'animation : 30ms par cycle
- Détection de synchronisation : appelle `balise.trySynchronize(satellite)` à chaque cycle

**Structure** :
1. Création de la fenêtre Swing
2. Création de 3 balises avec stratégies différentes :
   - Balise 1 : LinearMethod
   - Balise 2 : SinusoidalMethod
   - Balise 3 : VerticalMethod
3. Création de 2 satellites
4. Enregistrement des vues comme listeners
5. Boucle d'animation infinie (30ms)

#### `SynchronisationLinePanel`
**Description** : Panneau graphique affichant les lignes de connexion lors des synchronisations.

**Interface implémentée** :
- `SynchronisationListener` - Reçoit les événements de synchronisation

**Méthodes** :
- `onSynchronisationStart(SynchronisationStartEvent)` - Ajoute une ligne rouge entre balise et satellite
- `onSynchronisationEnd(SynchronisationEndEvent)` - Retire la ligne de connexion
- `paint(Graphics)` - Dessine les lignes de connexion actives

---

##  Design Patterns Utilisés

### 1. Pattern État (State Pattern)

**Description** : Permet à une balise de changer son comportement selon son état interne.

**Classes concernées** :
- `BaliseState` (enum) - Définit les 4 états possibles
- `Balise` - Contient l'état actuel et adapte son comportement dans `move()`

**États** :
1. **COLLECTE** : Mouvement selon stratégie + collecte de données
2. **REMONTEE** : Montée vers la surface
3. **SYNCHRONISATION** : Transfert de données
4. **DESCENTE** : Retour à la profondeur

**Transitions** :
- COLLECTE → REMONTEE (quand mémoire pleine)
- REMONTEE → SYNCHRONISATION (quand satellite aligné)
- SYNCHRONISATION → DESCENTE (quand transfert terminé)
- DESCENTE → COLLECTE (quand profondeur initiale atteinte)



---

### 2. Pattern Stratégie (Strategy Pattern)

**Description** : Permet de définir une famille d'algorithmes de mouvement et de les rendre interchangeables.

**Classes concernées** :
- `MovingMethod` (interface) - Contrat des stratégies
- `LinearMethod` - Mouvement linéaire horizontal
- `SinusoidalMethod` - Mouvement sinusoïdal
- `VerticalMethod` - Mouvement vertical yo-yo
- `StaticMethod` - Balise fixe
- `Balise` - Utilise une stratégie via `setMovingMethod()`

**Utilisation** :
```
Balise balise = new Balise(x, y, direction);
balise.setMovingMethod(new SinusoidalMethod()); // Stratégie interchangeable
```



---

### 3. Pattern Observable (Observer Pattern)

**Description** : Implémente un mécanisme de notification événementielle permettant le découplage entre modèles et vues.

**Classes concernées** :
- `Announcer` - Médiateur central (registre + diffusion)
- `AbstractEvent` - Classe mère des événements
- Tous les événements (BaliseMoveEvent, SatelliteMoveEvent, etc.)
- Toutes les interfaces Listener (BaliseListener, SatelliteListener, etc.)
- Toutes les vues (BaliseView, SatelliteView, SynchronisationLinePanel)

**Architecture** :
```
Modèle (Balise/Satellite)
    ↓ contient (composition)
Announcer
    ↓ announce(event)
AbstractEvent
    ↓ sentTo(listener)
Listener (BaliseView/SatelliteView)
    ↓ onEvent()
Mise à jour de la vue
```

**Flux d'événements** :
1. Modèle change d'état : `balise.move()`
2. Modèle émet événement : `announcer.announce(new BaliseMoveEvent(this))`
3. Announcer récupère les listeners : `List<Object> listeners = registrationIndex.get(eventClass)`
4. Événement se transmet : `event.sentTo(listener)` (Double Dispatch)
5. Listener reçoit l'événement : `listener.onBaliseMove(event)`
6. Vue se met à jour : `repaint()`



---


## 🔄 Cycle de Synchronisation Complet

### Conditions de déclenchement (3 conditions simultanées)
1. Balise en état **REMONTEE** et à la surface (Y = 290)
2. Satellite **disponible** (pas déjà en synchronisation)
3. **Alignement horizontal** : distance ≤ 10 pixels

### Phases
1. **Détection** : MainStrategy appelle `balise.trySynchronize(satellite)` à chaque cycle
2. **Démarrage** :
   - `balise.startSynchronisation(satellite)`
   - Changement état balise : REMONTEE → SYNCHRONISATION
   - Satellite devient indisponible
   - Émission `SynchronisationStartEvent` → ligne rouge apparaît
3. **Transfert progressif** :
   - À chaque cycle : transfert de `transferSpeed` données (5-14)
   - Durée moyenne : 150 données ÷ 10 par cycle = 15 cycles = 450ms
4. **Fin** :
   - Quand mémoire balise = 0
   - `balise.endSynchronisation()`
   - Émission `SynchronisationEndEvent` → ligne rouge disparaît
   - Satellite redevient disponible
   - Changement état balise : SYNCHRONISATION → DESCENTE

---

## 📊 Diagrammes UML

### Diagramme d'architecture

**Diagramme de classes complet**

![Diagramme de classes](Projet_balise_satellite/resources/diagramme%20de%20classe.png)

Diagramme de classes complet montrant les 4 packages principaux (MODÈLE, VUE, CONTRÔLEUR, OBSERVABLE/OBSERVATEUR).

---

### Diagrammes de séquence

#### Phase COLLECTE

![Collecte](Projet_balise_satellite/resources/Collecte.png)

La balise se déplace en profondeur selon sa stratégie (Linear, Sinusoidal, Vertical, Static) et collecte progressivement des données océaniques. Quand la mémoire est pleine (memory >= maxMemory), elle passe automatiquement en état REMONTEE.

#### Phase REMONTEE

![Remontee](Projet_balise_satellite/resources/Remontee.png)

La balise remonte progressivement vers la surface (y -= riseSpeed) jusqu'à atteindre SURFACE_Y (290). Une fois à la surface, elle attend qu'un satellite disponible passe au-dessus. Si un satellite est détecté via `trySynchronize()`, elle passe en état SYNCHRONISATION.

#### Phase SYNCHRONISATION

![Synchronisation](Projet_balise_satellite/resources/Synchro.png)

La balise transfère ses données vers le satellite en 3 étapes : **Début** (satellite devient indisponible, ligne rouge apparaît), **Transfert** (boucle qui transfère progressivement memory -= transferSpeed), **Fin** (memory == 0, ligne rouge disparaît, satellite redevient disponible, passage en DESCENTE).

#### Phase DESCENTE

![Descente](Projet_balise_satellite/resources/Descente.png)

Après avoir transféré toutes ses données (memory == 0), la balise redescend progressivement vers sa profondeur initiale (y += descentSpeed). Une fois arrivée (y >= initialY), elle repasse en état COLLECTE et un nouveau cycle recommence.


---



## Auteurs

Projet réalisé dans le cadre du cours de Conception Objet - Master 2 TIIL-A par :
 - Naël ABASY
 - Aïcha AMINE
