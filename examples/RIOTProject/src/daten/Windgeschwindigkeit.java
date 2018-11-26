package daten;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Windgeschwindigkeit {

    private Integer geschwindigkeit;
    private Date zeitpunkt;

    public Date getZeitpunkt() {
        return zeitpunkt;
    }

    public String getReadableTimestamp() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        Calendar windTime = Calendar.getInstance();
        windTime.setTime(zeitpunkt);

        if(DateUtils.isSameDay(today, windTime)) {
            return "Heute, "+ windTime.get(Calendar.HOUR) + ":" + windTime.get(Calendar.MINUTE) + ":" + windTime.get(Calendar.SECOND);
        } else {
            return windTime.get(Calendar.DAY_OF_MONTH)+"."+windTime.get(Calendar.MONTH)+"."+windTime.get(Calendar.YEAR)+" - " + windTime.get(Calendar.HOUR) + ":" + windTime.get(Calendar.MINUTE) + ":" + windTime.get(Calendar.SECOND);
        }

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
