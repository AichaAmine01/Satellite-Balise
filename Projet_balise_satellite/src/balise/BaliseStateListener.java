package balise;

/**
 * Interface pour écouter les changements d'état d'une balise
 */
public interface BaliseStateListener {
	public void onStateChange(BaliseStateChangeEvent event);
}
