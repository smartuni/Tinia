package daten;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Daten {

    private ArrayList<Windgeschwindigkeit> windGeschwindigkeiten = new ArrayList<>();
    private ArrayList<Windrichtung> windrichtungen = new ArrayList<>();

    public Daten() {
        createDummyDaten();
    }

    public void addWindgeschwindigkeit(Windgeschwindigkeit windgeschwindigkeit) {
        this.windGeschwindigkeiten.add(windgeschwindigkeit);
    }

    public void addWindrichtung(Windrichtung windrichtung) {
        this.windrichtungen.add(windrichtung);
    }

    public ArrayList<Windgeschwindigkeit> getWindGeschwindigkeiten() {
        return windGeschwindigkeiten;
    }

    public ArrayList<Windrichtung> getWindrichtungen() {return windrichtungen;}


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

            int randomNum = ran.nextInt(8) + 1;
            windrichtungen.add(new Windrichtung(randomNum,currentDatePlusi));

        }
    }
}
