package controller;

import model.Heat;
import model.Hold;
import model.Kapsejlads;
import model.Resultat;
import storage.Storage;

import java.time.LocalDate;
import java.time.LocalTime;

public class Controller {
    public static Heat createHeat(int nummer, LocalTime tid, boolean finale, Kapsejlads kapsejlads) {
        if (kapsejlads == null) {
            throw new IllegalArgumentException("kapsejlads must not be null");
        }
        Storage.addKapsejlads(kapsejlads);
        return kapsejlads.createHeat(nummer, tid, finale);
    }

    public static Kapsejlads createKapsejlads(String titel, LocalDate dato, int tilskuere) {
        //could make a fail safe to see if the kapsejlads is already made by checking name;
        for (Kapsejlads k : Storage.getKapsejlads()) {
            if (k.getTitel().equals(titel) && k.getDato().equals(dato)) {
                return k;
            }
        }
        Kapsejlads kapsejlads = new Kapsejlads(titel, dato, tilskuere);
        Storage.addKapsejlads(kapsejlads);
        return kapsejlads;
    }

    public static Resultat createResultat(int bane, int sekunder, Heat heat) {
        if (heat == null) {
            throw new IllegalArgumentException("Heat must not be null");
        }
        return heat.createResultat(bane, sekunder);
    }

    public static Resultat createResultat(int bane, int sekunder, Heat heat, Hold hold) {
        Resultat resultat = createResultat(bane, sekunder, heat);
        if (hold != null) {
            hold.addResultat(resultat);
        }
        return resultat;
    }

    public static Hold createHold(String navn, String institution) {
        for (Hold h : Storage.getHolds()) {
            if (h.getNavn().equals(navn) && h.getInstitution().equals(institution)) {
                return h;
            }
        }
        Hold hold = new Hold(navn, institution);
        Storage.addHold(hold);
        return hold;
    }

    public static int tilskuerGennemsnit() {
        var kapsejladser = Storage.getKapsejlads();
        int totalTilskuere = 0;
        int count = 0;
        for (Kapsejlads kapsejlad : Storage.getKapsejlads()) {
            if (kapsejlad.getTilskuere() > 0) {
                totalTilskuere += kapsejlad.getTilskuere();
                count++;
            }
        }
        if (count == 0){
            return 0;
        }
        return totalTilskuere / count;
    }
}
