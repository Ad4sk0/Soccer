package soccer.game.socket.payload;

public interface Payload {

    StateMessageHeader getHeader();

    void setHeader(StateMessageHeader header);

    Object getData();

    void setData(Object data);

}
