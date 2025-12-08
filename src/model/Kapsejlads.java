package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Kapsejlads {
    private String titel;
    private LocalDate dato;
    private int tilskuere;
    private ArrayList<Heat> heats = new ArrayList<>();

    public Kapsejlads(String titel, LocalDate dato, int tilskuere) {
        this.titel = titel;
        this.dato = dato;
        this.tilskuere = tilskuere;
    }

    public String getTitel() {
        return titel;
    }

    public LocalDate getDato() {
        return dato;
    }

    public int getTilskuere() {
        return tilskuere;
    }

    public Heat createHeat(int nummer, LocalTime tid, boolean finale) {
        Heat heat = new Heat(nummer, tid, finale, this);
        heats.add(heat);
        return heat;
    }

    public void removeHeat(Heat heat) {
        if (heats.contains(heat)) {
            heats.remove(heat);
        }
    }

    public int dageCountDown() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(this.dato)) return 0;
        return this.dato.getDayOfYear() - today.getDayOfYear();
        //return (int)ChronoUnit.DAYS.between(today,dato);
    }

    public ArrayList<Resultat> indLedendeResultater() {
        ArrayList<Resultat> resultats = new ArrayList<>();
        for (Heat heat : heats) {
            if (heat == null) continue;
            if (heat.isFinale()) continue; // skips finale heats entirely
            var heatResults = heat.getResultats();
            if (heatResults == null) continue;
            for (Resultat resultat : heatResults) {
                if (resultat != null) {
                    resultats.add(resultat);
                }
            }
        }
        return resultats;
    }

    public Hold[] beregnFinalister() {
        ArrayList<Hold> finalister = new ArrayList<>();

        // 1) fastest hold form the each non-finale heat
        for (Heat heat : heats) {
            if (heat == null || heat.isFinale()) continue;
            Resultat[] heatResults = heat.getResultats();
            if (heatResults == null || heatResults.length == 0) continue;
            Resultat best = null;
            for (Resultat heatResult : heatResults) {
                if (heatResult == null || heatResult.getHold() == null) continue;
                if (best == null || heatResult.getSekunder() < best.getSekunder()) {
                    best = heatResult;
                }
            }

            // add the hold to finalister
            if (best != null) {
                Hold h = best.getHold();
                if (!finalister.contains(h)) {
                    finalister.add(h);
                }
            }
        }
        // 2) the single overall fastest across all non-finale heats
        ArrayList<Resultat> allIndledende = indLedendeResultater();
        Resultat overallBest = null;
        for (Resultat resultat : allIndledende) {
            if (resultat == null || resultat.getHold() == null) continue;
            if (overallBest == null || resultat.getSekunder() < overallBest.getSekunder()) {
                overallBest = resultat;
            }
        }
        if (overallBest != null) {
            Hold fastestHold = overallBest.getHold();
            if (!finalister.contains(fastestHold)) {
                finalister.add(fastestHold);
            }
        }
        // return an array with correct size
        Hold[] resultat = new Hold[finalister.size()];
        return finalister.toArray(resultat);
    }
    public ArrayList<Heat> getHeats(){
        return heats;
    }
    @Override
    public String toString(){
        return titel + " (" + dato +")";
    }
}
