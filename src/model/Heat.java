package model;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;

public class Heat {
    private int nummer;
    private LocalTime tidspunkt;
    private boolean finale;
    private Resultat[] resultats = new Resultat[3];
    private Kapsejlads kapsejlads;

    public Heat(int nummer, LocalTime tidspunkt, boolean finale, Kapsejlads kapsejlads) {
        this.nummer = nummer;
        this.tidspunkt = tidspunkt;
        this.finale = finale;
        this.kapsejlads = kapsejlads;
    }

    public int getNummer() {
        return nummer;
    }

    public LocalTime getTidspunkt() {
        return tidspunkt;
    }

    public boolean isFinale() {
        return finale;
    }
    public Resultat[] getResultats(){
        return resultats;
    }

    public Resultat createResultat(int bane, int sekunder){
        if (bane < 1 || bane > resultats.length) {
            throw new IllegalArgumentException("Bane must be between 1 and " + resultats.length);
        }
        int idx = bane - 1;
        if (resultats[idx] != null) {
            throw new IllegalStateException("Resultat for bane " + bane + " already exists");
        }
        Resultat resultat = new Resultat(bane, sekunder, this);
        resultats[idx] = resultat;
        return resultat;
    }
    public Resultat createResultatMedValidering(int bane, int sekunder){
        // 1
        // 2
        // 3
    }
    public void removeResultat(Resultat resultat){
        for (int i = 0; i < resultats.length; i++) {
            if (resultats[i] == resultat) {
                resultats[i] = null;
                resultat.setHold(null);
                return;
            }
        }
    }
    public Resultat beregnVinderResultat(){
        int bedsteTid = Integer.MAX_VALUE;
        Resultat vinder = null;
        for (Resultat resultat : resultats) {
            if (resultat.getSekunder() > bedsteTid){
                bedsteTid = resultat.getSekunder();
                vinder = resultat;
            }
        }
        return vinder;
    }
}
