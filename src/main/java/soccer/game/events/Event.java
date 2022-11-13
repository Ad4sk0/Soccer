package soccer.game.events;

import java.util.Objects;

public class Event {

    EventTypes eventType;

    public Event(EventTypes eventType) {
        this.eventType = eventType;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "Event [eventType=" + eventType + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventType == event.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType);
    }
}
