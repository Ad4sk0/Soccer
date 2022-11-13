package soccer.game.socket.decoding;

import org.jboss.logging.Logger;
import soccer.game.socket.payload.Payload;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MyEncoder implements Encoder.Text<Payload> {

    @Inject
    Logger logger;

    @Override
    public void init(EndpointConfig config) {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(Payload payload) throws EncodeException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(payload, Payload.class);
        } catch (Exception e) {
            logger.error("Unable to serialize: " + payload);
            logger.error(e);
            return null;
        }
    }
}
