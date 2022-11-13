package soccer.game.socket.payload;

public class StateMessage implements Payload {

    StateMessageHeader header;
    Object data;

    public StateMessage(StateMessageHeader header, Object data) {
        super();
        this.header = header;
        this.data = data;
    }

    @Override
    public StateMessageHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(StateMessageHeader header) {
        this.header = header;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;

    }
}
