package gui.daten;

import java.util.Date;

public class Windgeschwindigkeit {

    private Integer geschwindigkeit;
    private Date zeitpunkt;

    public Date getZeitpunkt() {
        return zeitpunkt;
    }

    public Integer getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public Windgeschwindigkeit(Integer geschwindigkeit, Date zeitpunkt) {
        this.geschwindigkeit = geschwindigkeit;
        this.zeitpunkt = zeitpunkt;
    }

    @Override
    public String toString() {
       return "Windgeschwindigkeit: " + geschwindigkeit + ", Datum: " + zeitpunkt;
    }
}
