package gui;

import controller.Controller;
import storage.Storage;

public class AppMain {
    public static void main(String[] args) {
        Storage.initStorage();
        System.out.println(Controller.tilskuerGennemsnit());
    }
}
