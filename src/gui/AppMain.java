package gui;

import controller.Controller;
import javafx.application.Application;
import model.Kapsejlads;
import storage.Storage;

import java.util.Arrays;

public class AppMain {
    public static void main(String[] args) {
        Storage.initStorage();
        System.out.println(Controller.tilskuerGennemsnit());
        Kapsejlads k1 = Storage.getKapsejlads().get(3);
        System.out.println(Arrays.toString(k1.beregnFinalister()));
        Application.launch(Gui.class);
    }
}
