package soccer.game.socket.payload.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.game.match.GameMatch;

import static org.wildfly.common.Assert.assertTrue;

class MatchStateTest {

    GameMatch gameMatch;

    @BeforeEach
    void setUp() {
        gameMatch = MatchStateTestUtils.getGameMatchMock();
    }

    @Test
    void shouldSerializeMatchState() throws JsonProcessingException {
        ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        MatchState matchState = new MatchState(gameMatch);
        String serializedState = objectMapper.writeValueAsString(matchState);
        assertTrue(serializedState.length() > 0);
    }
}