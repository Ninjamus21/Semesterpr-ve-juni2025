package storage;

import model.Heat;
import model.Hold;
import model.Kapsejlads;
import model.Resultat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Storage {
    private static final ArrayList<Kapsejlads> kapsejlads = new ArrayList<>();
    private static final ArrayList<Hold> holds = new ArrayList<>();

    public static void initStorage() {
        Kapsejlads kapsejlads1 = new Kapsejlads("Kapsejlads 1991", LocalDate.of(1991,5,10),50);
        Kapsejlads kapsejlads2 = new Kapsejlads("Kapsejlads 2003", LocalDate.of(2003,5,2),10000);
        Kapsejlads kapsejlads3 = new Kapsejlads("Kapsejlads 2011", LocalDate.of(2011,4,29),20000);
        Kapsejlads kapsejlads4 = new Kapsejlads("Kapsejlads 2025", LocalDate.of(2025,4,25),30000);
        Kapsejlads kapsejlads5 = new Kapsejlads("Kapsejlads 2026", LocalDate.of(2026,4,24),0);

        addKapsejlads(kapsejlads1);
        addKapsejlads(kapsejlads2);
        addKapsejlads(kapsejlads3);
        addKapsejlads(kapsejlads4);
        addKapsejlads(kapsejlads5);

        Heat heatfirst2025 = new Heat(1, LocalTime.of(14,30),false,kapsejlads4);
        Heat heatSecond2025 = new Heat(2, LocalTime.of(15,30),false,kapsejlads4);

        Hold hold1 = new Hold("Umbilicus","Medical school AU");
        Hold hold2 = new Hold("politologisk forening", "AU (Statskundskab)");
        Hold hold3 = new Hold("Profus", "VIA (Campus C)");
        Hold hold4 = new Hold("Studenterlauget", "AU");
        Hold hold5 = new Hold("FUT", "AU (Fysik)");
        Hold hold6 = new Hold("Apollonia", "AU (Odentologi)");

        addHold(hold1);
        addHold(hold2);
        addHold(hold3);
        addHold(hold4);
        addHold(hold5);
        addHold(hold6);

        Resultat resultat1 = new Resultat(1,207,heatfirst2025);
        resultat1.setHold(hold1);
        Resultat resultat2 = new Resultat(2,215,heatfirst2025);
        resultat2.setHold(hold2);
        Resultat resultat3 = new Resultat(3,198,heatfirst2025);
        resultat3.setHold(hold3);
        Resultat resultat4 = new Resultat(4,228,heatfirst2025);
        resultat4.setHold(hold4);
        Resultat resultat5 = new Resultat(2,203,heatSecond2025);
        resultat5.setHold(hold5);
        Resultat resultat6 = new Resultat(4,190,heatSecond2025);
        resultat6.setHold(hold6);
    }

    public static ArrayList<Kapsejlads> getKapsejlads() {
        return kapsejlads;
    }

    public static void addKapsejlads(Kapsejlads kapsejlad) {
        if (!kapsejlads.contains(kapsejlad)) {
            kapsejlads.add(kapsejlad);
        }
    }

    public static ArrayList<Hold> getHolds(){
        return holds;
    }

    public static void addHold(Hold hold) {
        if (!holds.contains(hold)){
            holds.add(hold);
        }
    }
}
