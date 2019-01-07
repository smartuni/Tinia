package daten;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Windrichtung {

    private Integer windrichtung;
    private Date zeitpunkt;
    private String readableWindrichtung;
    private String readableZeitpunkt;

    public Date getZeitpunkt() {
        return zeitpunkt;
    }

    public String getReadableTimestamp() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Calendar directionTime = Calendar.getInstance();
        directionTime.setTime(zeitpunkt);
        if(DateUtils.isSameDay(today, directionTime)) {
            return "Heute, "+ directionTime.get(Calendar.HOUR) + ":" + directionTime.get(Calendar.MINUTE) + ":" + directionTime.get(Calendar.SECOND);
        } else {
            return directionTime.get(Calendar.DAY_OF_MONTH)+"."+directionTime.get(Calendar.MONTH)+"."+directionTime.get(Calendar.YEAR)+" - " + directionTime.get(Calendar.HOUR) + ":" + directionTime.get(Calendar.MINUTE) + ":" + directionTime.get(Calendar.SECOND);
        }
    }

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
        this.windrichtung = windrichtung;
        this.zeitpunkt = zeitpunkt;
        setReadableWindrichtung(windrichtung);
        setReadableZeitpunkt(getReadableTimestamp());
    }


    @Override
    public String toString() {
       return "Windrichtung: " + getReadableWindrichtung() + ", Datum: " + zeitpunkt;
    }
}
