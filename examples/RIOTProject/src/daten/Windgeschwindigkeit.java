package daten;

import java.util.Date;

public class Windgeschwindigkeit extends WindDaten {

    private Integer geschwindigkeit;

    public Integer getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public Windgeschwindigkeit(Integer geschwindigkeit, Date zeitpunkt) {
        super(zeitpunkt);
        this.geschwindigkeit = geschwindigkeit;
    }

    @Override
    public String toString() {
        return "Windgeschwindigkeit: " + geschwindigkeit + ", Datum: " + getZeitpunkt();
    }
}
