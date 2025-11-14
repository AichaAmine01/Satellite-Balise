package balise;

import announcer.AbstractEvent;

/**
 * Événement déclenché quand l'état d'une balise change
 * (COLLECTE -> REMONTEE ou vice versa)
 */
public class BaliseStateChangeEvent extends AbstractEvent {

	public BaliseStateChangeEvent(Object source) {
		super(source);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void sentTo(Object target) {
		if (target instanceof BaliseStateListener) {
			((BaliseStateListener) target).onStateChange(this);
		}
	}
}
