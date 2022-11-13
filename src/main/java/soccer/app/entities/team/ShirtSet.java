package soccer.app.entities.team;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import soccer.app.entities.EntityBase;
import soccer.utils.ColorConverter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.Serializable;

@Entity
public class ShirtSet extends EntityBase implements Serializable {

    @NotNull
    private Color shirtColor;
    @NotNull
    private Color numberColor;
    @NotNull
    private Color goalkeeperShirtColor;
    @NotNull
    private Color goalkeeperNumberColor;
    @ManyToOne
    @JsonIgnore
    private Team team;

    public ShirtSet() {
    }

    public ShirtSet(Color shirtColor, Color numberColor, Color goalkeeperShirtColor, Color goalkeeperNumberColor) {
        this.shirtColor = shirtColor;
        this.numberColor = numberColor;
        this.goalkeeperShirtColor = goalkeeperShirtColor;
        this.goalkeeperNumberColor = goalkeeperNumberColor;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @JsonGetter("shirtColor")
    public String getShirtColorAsHexString() {
        return ColorConverter.convertColorToHexString(shirtColor);
    }

    @JsonSetter("shirtColor")
    public void setShirtColorFromHexString(String shirtColor) {
        this.shirtColor = ColorConverter.convertHexStringToColor(shirtColor);
    }

    public Color getShirtColor() {
        return shirtColor;
    }

    public void setShirtColor(Color shirtColor) {
        this.shirtColor = shirtColor;
    }

    @JsonGetter("numberColor")
    public String getNumberColorAsHexString() {
        return ColorConverter.convertColorToHexString(numberColor);
    }

    @JsonSetter("numberColor")
    public void setNumberColorFromHexString(String numberColor) {
        this.numberColor = ColorConverter.convertHexStringToColor(numberColor);
    }

    public Color getNumberColor() {
        return numberColor;
    }

    public void setNumberColor(Color numberColor) {
        this.numberColor = numberColor;
    }

    @JsonGetter("goalkeeperShirtColor")
    public String getGoalkeeperShirtColorAsHexString() {
        return ColorConverter.convertColorToHexString(goalkeeperShirtColor);
    }

    @JsonSetter("goalkeeperShirtColor")
    public void setGoalkeeperShirtColorFromHexString(String goalkeeperShirtColor) {
        this.goalkeeperShirtColor = ColorConverter.convertHexStringToColor(goalkeeperShirtColor);
    }

    public Color getGoalkeeperShirtColor() {
        return goalkeeperShirtColor;
    }

    public void setGoalkeeperShirtColor(Color goalkeeperShirtColor) {
        this.goalkeeperShirtColor = goalkeeperShirtColor;
    }

    @JsonGetter("goalkeeperNumberColor")
    public String getGoalkeeperNumberColorAsHexString() {
        return ColorConverter.convertColorToHexString(goalkeeperNumberColor);
    }

    @JsonSetter("goalkeeperNumberColor")
    public void setGoalkeeperNumberColorFromHexString(String goalkeeperNumberColor) {
        this.goalkeeperNumberColor = ColorConverter.convertHexStringToColor(goalkeeperNumberColor);
    }

    public Color getGoalkeeperNumberColor() {
        return goalkeeperNumberColor;
    }

    public void setGoalkeeperNumberColor(Color goalkeeperNumberColor) {
        this.goalkeeperNumberColor = goalkeeperNumberColor;
    }

    @Override
    public String toString() {
        return "{" +
                "shirtColor=" + shirtColor +
                ", numberColor=" + numberColor +
                ", goalkeeperShirtColor=" + goalkeeperShirtColor +
                ", goalkeeperNumberColor=" + goalkeeperNumberColor +
                '}';
    }
}
