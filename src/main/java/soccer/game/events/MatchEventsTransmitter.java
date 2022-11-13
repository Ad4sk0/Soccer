package soccer.game.events;

import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class MatchEventsTransmitter {

    private final Logger logger = Logger.getLogger(getClass());

    private List<MatchEventsListener> listeners = new ArrayList<>();

    public void receiveEvent(Event event) {
        logger.debug("New event occurred: " + event);
        transmitEventToListeners(event);
    }

    public void addListener(MatchEventsListener listener) {
        if (listeners.contains(listener)) {
            logger.error("Trying to add the same listener multiple times. Ignoring. " + listener);
        }
        listeners.add(listener);
    }

    public void removeListener(MatchEventsListener listener) {
        listeners.remove(listener);
    }

    private void transmitEventToListeners(Event event) {
        for (MatchEventsListener listener : listeners) {
            listener.receiveEvent(event);
        }
    }

    public List<MatchEventsListener> getListeners() {
        return listeners;
    }
}
