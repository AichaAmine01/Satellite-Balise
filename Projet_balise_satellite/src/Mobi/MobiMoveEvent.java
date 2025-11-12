package Mobi;

import announcer.AbstractEvent;

public class MobiMoveEvent extends AbstractEvent{

	public MobiMoveEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void sentTo(Object target) {
		((MobilListener) target).MobilMoveEvent(this);
	}
}
