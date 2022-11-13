package soccer.app.entities.formation;

import soccer.app.entities.EntityBase;
import soccer.app.entities.formation.utils.PositionsInitializer;
import soccer.models.positions.PlayingPosition;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Formation extends EntityBase implements Serializable {

    public static final int POSITIONS_NUMBER = 11;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Formations name = Formations.FOUR_FOUR_TWO;
    @Size(min = POSITIONS_NUMBER, max = POSITIONS_NUMBER)
    @OneToMany(mappedBy = "formation", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<FormationPosition> positionsList;

    public Formation() {

    }

    public Formation(Formations formationName) {
        initPositions();
        setupFormation(formationName);
    }

    public void changeFormation(Formations formationName) {
        setupFormation(formationName);
    }

    public void initPositions() {
        positionsList = new ArrayList<>();
        for (int i = 0; i < POSITIONS_NUMBER; i++) {
            positionsList.add(new FormationPosition(this));
        }
    }

    private void setupFormation(Formations formationName) {
        this.name = formationName;
        setUpPositions();
        initializePlayerPositions();
    }

    private void setUpPositions() {
        List<PlayingPosition> positionsUsed = FormationPositions.FORMATION_POSITIONS.get(name);
        if (positionsUsed.size() != 11) {
            throw new UnsupportedOperationException("Formation should have exactly 11 positions");
        }

        for (int i = 0; i < positionsUsed.size(); i++) {
            positionsList.get(i).setPosition(positionsUsed.get(i));
        }
    }

    private void initializePlayerPositions() {
        PositionsInitializer.initializePositionsBasedOnFormation(positionsList);
    }

    public Formations getName() {
        return name;
    }

    public void setName(Formations name) {
        this.name = name;
    }

    public List<FormationPosition> getPositionsList() {
        return positionsList;
    }

    @Override
    public String toString() {
        return "Formation{" +
                ", name=" + name +
                '}';
    }

    public List<PlayingPosition> getPlayingPositionsList() {
        List<PlayingPosition> playingPositionList = new ArrayList<>();
        for (FormationPosition formationPosition : positionsList) {
            playingPositionList.add(formationPosition.getPosition());
        }
        return playingPositionList;
    }
}
