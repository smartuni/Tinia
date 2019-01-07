package daten;

import java.util.Date;

public class Windrichtung extends WindDaten {

    private Integer windrichtung;
    private String readableWindrichtung;
    private String readableZeitpunkt;

    public void setReadableWindrichtung(int windrichtung) {
        switch (windrichtung) {
            case 1:
                readableWindrichtung = "Norden";
                break;
            case 2:
                readableWindrichtung = "Nordosten";
                break;
            case 3:
                readableWindrichtung = "Osten";
                break;
            case 4:
                readableWindrichtung = "Südosten";
                break;
            case 5:
                readableWindrichtung = "Süden";
                break;
            case 6:
                readableWindrichtung = "Südwesten";
                break;
            case 7:
                readableWindrichtung = "Westen";
                break;
            case 8:
                readableWindrichtung = "Nordwesten";
                break;
            default: readableWindrichtung = null;
        }
    }

    public String getReadableWindrichtung() {
        return readableWindrichtung;
    }

    public String getReadableZeitpunkt() {
        return readableZeitpunkt;
    }

    public Integer getWindrichtung() {
        return windrichtung;
    }

    public void setReadableZeitpunkt(String readableZeitpunkt) {
        this.readableZeitpunkt = readableZeitpunkt;
    }

    public Windrichtung(Integer windrichtung, Date zeitpunkt) {
        super(zeitpunkt);
        this.windrichtung = windrichtung;
        setReadableWindrichtung(windrichtung);
        setReadableZeitpunkt(getReadableTimestamp());
    }


    @Override
    public String toString() {
        return "Windrichtung: " + getReadableWindrichtung() + ", Datum: " + getZeitpunkt();
    }
}
