package soccer.app.entities.player;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class TeamAttributes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1423104701697951214L;

    @Min(1)
    @Max(99)
    @NotNull
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
