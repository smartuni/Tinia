package daten;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Daten {

    private ArrayList<Windgeschwindigkeit> windGeschwindigkeiten = new ArrayList<>();

    public Daten() {
        createDummyDaten();
    }

    public void addWindgeschwindigkeit(Windgeschwindigkeit windgeschwindigkeit) {
        this.windGeschwindigkeiten.add(windgeschwindigkeit);
    }

    public ArrayList<Windgeschwindigkeit> getWindGeschwindigkeiten() {
        return windGeschwindigkeiten;
    }


    private void createDummyDaten() {
        Date currentDate = new Date();

        int i;
        for (i = 0; i < 10; i++) {
            // manipulate date
            // convert date to calendar
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);

            c.add(Calendar.YEAR, 0);
            c.add(Calendar.MONTH, 0);
            c.add(Calendar.DATE, -20); //same with c.add(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.HOUR, -1);
            c.add(Calendar.MINUTE, i);
            c.add(Calendar.SECOND, i);

            // convert calendar to date
            Date currentDatePlusi = c.getTime();
            Random ran = new Random();
            int randomNumber = 5 + ran.nextInt(50 - 5 + 1);
            windGeschwindigkeiten.add(new Windgeschwindigkeit(randomNumber, currentDatePlusi));

        }
    }
}
