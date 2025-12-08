package controller;

import model.Heat;
import model.Hold;
import model.Kapsejlads;
import model.Resultat;
import storage.Storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    // factory / query wrappers to hide Storage from GUI
    public static List<Kapsejlads> getKapsejladser() {
        return Storage.getKapsejlads();
    }

    public static List<Hold> getHolds() {
        return Storage.getHolds();
    }

    // create or return existing kapsejlads
    public static Kapsejlads createKapsejlads(String titel, LocalDate dato, int tilskuere) {
        for (Kapsejlads k : Storage.getKapsejlads()) {
            if (k.getTitel().equals(titel) && k.getDato().equals(dato)) {
                return k;
            }
        }
        Kapsejlads kapsejlads = new Kapsejlads(titel, dato, tilskuere);
        Storage.addKapsejlads(kapsejlads);
        return kapsejlads;
    }

    // create heat through controller to keep association consistent
    public static Heat createHeat(Kapsejlads kapsejlads, int nummer, LocalTime tid, boolean finale) {
        if (kapsejlads == null) {
            throw new IllegalArgumentException("Kapsejlads must not be null");
        }
        return kapsejlads.createHeat(nummer, tid, finale);
    }

    // validated creation of a resultat (preferred)
    public static Resultat createResultatValidated(Heat heat, Hold hold, int bane, int sekunder) {
        if (heat == null) {
            throw new IllegalArgumentException("Heat must not be null");
        }
        if (hold == null) {
            throw new IllegalArgumentException("Hold must not be null");
        }
        // delegate validation to domain (Heat.createResultatMedValidering)
        return heat.createResultatMedValidering(hold, bane, sekunder);
    }

    // legacy/basic creation (no hold)
    public static Resultat createResultat(Heat heat, int bane, int sekunder) {
        if (heat == null) {
            throw new IllegalArgumentException("Heat must not be null");
        }
        return heat.createResultat(bane, sekunder);
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
        for (Kapsejlads kapsejlad : kapsejladser) {
            if (kapsejlad.getTilskuere() > 0) {
                totalTilskuere += kapsejlad.getTilskuere();
                count++;
            }
        }
        if (count == 0) return 0;
        return totalTilskuere / count;
    }
}
