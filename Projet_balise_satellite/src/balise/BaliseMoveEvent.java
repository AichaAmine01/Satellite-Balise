package balise;

import announcer.AbstractEvent;

public class BaliseMoveEvent extends AbstractEvent {

    public BaliseMoveEvent(Object source) {
        super(source);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void sentTo(Object target) {
        ((BaliseListener) target).onBaliseMove(this);
    }
}
