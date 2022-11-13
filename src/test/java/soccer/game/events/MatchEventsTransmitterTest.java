package soccer.game.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MatchEventsTransmitterTest {

    @Mock
    MatchEventsHandler matchEventsHandler;

    MatchEventsTransmitter matchEventsTransmitter;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        matchEventsTransmitter = new MatchEventsTransmitter();
        doNothing().when(matchEventsHandler).receiveEvent(isA(Event.class));
    }

    @Test
    void shouldRegisterAndRemoveListeners() {
        MatchEventsListener listener1 = mock(MatchEventsHandler.class);
        MatchEventsListener listener2 = mock(MatchEventsHandler.class);
        assertEquals(0, matchEventsTransmitter.getListeners().size());
        matchEventsTransmitter.addListener(listener1);
        assertEquals(1, matchEventsTransmitter.getListeners().size());
        matchEventsTransmitter.addListener(listener2);
        assertEquals(2, matchEventsTransmitter.getListeners().size());
        matchEventsTransmitter.removeListener(listener1);
        assertEquals(1, matchEventsTransmitter.getListeners().size());
        matchEventsTransmitter.removeListener(listener2);
        assertEquals(0, matchEventsTransmitter.getListeners().size());
    }

    @Test
    void shouldTransmitEvent() {
        matchEventsTransmitter.addListener(matchEventsHandler);
        Event event = new Event(EventTypes.GOAL);
        matchEventsTransmitter.receiveEvent(event);
        verify(matchEventsHandler).receiveEvent(event);
    }
}