package daten;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Daten {

    private ArrayList<Windgeschwindigkeit> windGeschwindigkeiten = new ArrayList<>();

    public Daten() {
        createDummyDaten();
    }

    public void addWindgeschwindigkeit(Windgeschwindigkeit windgeschwindigkeit) {
        this.windGeschwindigkeiten.add(windgeschwindigkeit);
        System.out.println("Länge Array: " + windGeschwindigkeiten.size());
    }

    public ArrayList<Windgeschwindigkeit> getWindGeschwindigkeiten() {
        return windGeschwindigkeiten;
    }

    private void createDummyDaten() {

        Date currentDate = new Date();

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        int i;
        for (i=0;i<10;i++) {
            // manipulate date
            c.add(Calendar.YEAR, i);
            c.add(Calendar.MONTH, i);
            c.add(Calendar.DATE, i); //same with c.add(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.HOUR, i);
            c.add(Calendar.MINUTE, i);
            c.add(Calendar.SECOND, i);

            // convert calendar to date
            Date currentDatePlusi = c.getTime();

            windGeschwindigkeiten.add(new Windgeschwindigkeit(5+i, currentDatePlusi  ));
        }
    }
}
