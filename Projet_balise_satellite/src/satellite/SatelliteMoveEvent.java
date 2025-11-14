package satellite;

import announcer.AbstractEvent;

public class SatelliteMoveEvent extends AbstractEvent{

	public SatelliteMoveEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void sentTo(Object target) {
		((SatelliteListener) target).onSatelliteMove(this);
	}
}
