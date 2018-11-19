package gui.daten;

import java.util.ArrayList;

public class Daten {

    ArrayList<Windgeschwindigkeit> windGeschwindigkeiten = new ArrayList<>();

    public Daten() {}

    public void addWindgeschwindigkeit(Windgeschwindigkeit windgeschwindigkeit) {
        this.windGeschwindigkeiten.add(windgeschwindigkeit);
    }

    public ArrayList<Windgeschwindigkeit> getWindGeschwindigkeiten() {
        return windGeschwindigkeiten;
    }
}
