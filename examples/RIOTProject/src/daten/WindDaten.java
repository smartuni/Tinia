package daten;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public abstract class WindDaten {
    private Date zeitpunkt;

    public WindDaten(Date zeitpunkt) {
        this.zeitpunkt = zeitpunkt;
    }

    public Date getZeitpunkt() {
        return zeitpunkt;
    }

    public String getReadableTimestamp() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        Calendar windTime = Calendar.getInstance();
        windTime.setTime(zeitpunkt);
        if (DateUtils.isSameDay(today, windTime)) {
            return "Heute, " + String.format("%02d:%02d:%02d", windTime.get(Calendar.HOUR), windTime.get(Calendar.MINUTE), windTime.get(Calendar.SECOND));
        } else {
            return String.format("%02d.%02d.%04d", windTime.get(Calendar.DAY_OF_MONTH), windTime.get(Calendar.MONTH), windTime.get(Calendar.YEAR)) + " - " + String.format("%02d:%02d:%02d", windTime.get(Calendar.HOUR), windTime.get(Calendar.MINUTE), windTime.get(Calendar.SECOND));
        }
    }
}
