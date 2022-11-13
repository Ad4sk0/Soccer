package soccer.game.socket.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.game.match.GameMatch;
import soccer.game.socket.payload.data.MatchState;
import soccer.game.socket.payload.data.MatchStateInitial;
import soccer.game.socket.payload.data.MatchStateTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.wildfly.common.Assert.assertTrue;

class StateMessageTest {

    StateMessage stateMessage;

    Object data;

    @BeforeEach
    void setUp() {
        data = mock(MatchStateInitial.class);
        stateMessage = new StateMessage(StateMessageHeader.MATCH_STATE_INITIAL_HEADER, data);
    }

    @Test
    void shouldComposeStateMessage() {
        assertEquals(StateMessageHeader.MATCH_STATE_INITIAL_HEADER, stateMessage.getHeader());
        assertEquals(data, stateMessage.getData());
        stateMessage.setHeader(StateMessageHeader.MATCH_STATE_HEADER);
        data = mock(MatchState.class);
        stateMessage.setData(data);
        assertEquals(StateMessageHeader.MATCH_STATE_HEADER, stateMessage.getHeader());
        assertEquals(data, stateMessage.getData());
    }

    @Test
    void shouldSerializeStateMessage() throws JsonProcessingException {
        ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        GameMatch gameMatch = MatchStateTestUtils.getGameMatchMock();
        MatchStateInitial matchStateInitial = new MatchStateInitial(gameMatch);
        stateMessage = new StateMessage(StateMessageHeader.MATCH_STATE_INITIAL_HEADER, matchStateInitial);
        String serializedState = objectMapper.writeValueAsString(stateMessage);
        assertTrue(serializedState.length() > 0);
    }


}