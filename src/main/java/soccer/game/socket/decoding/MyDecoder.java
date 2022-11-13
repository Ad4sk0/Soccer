package soccer.game.socket.decoding;

import org.jboss.logging.Logger;
import soccer.game.socket.payload.Payload;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;


public class MyDecoder implements Decoder.Text<Payload> {

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
    public Payload decode(String s) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(s, Payload.class);
        } catch (Exception e) {
            logger.error("Unable to deserialize: " + s);
            logger.error(e);
            return null;
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
