package soccer.game.socket.payload.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.game.match.GameMatch;

import static org.wildfly.common.Assert.assertTrue;

class MatchStateInitialTest {

    GameMatch gameMatch;

    @BeforeEach
    void setUp() {
        gameMatch = MatchStateTestUtils.getGameMatchMock();
    }

    @Test
    void shouldSerializeMatchInitialState() throws JsonProcessingException {
        ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        MatchStateInitial matchStateInitial = new MatchStateInitial(gameMatch);
        String serializedState = objectMapper.writeValueAsString(matchStateInitial);
        assertTrue(serializedState.length() > 0);
    }
}