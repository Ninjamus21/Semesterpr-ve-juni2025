package gui;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Heat;
import model.Hold;
import model.Kapsejlads;
import model.Resultat;
import storage.Storage;

import java.time.LocalTime;

public class Gui extends Application {
    public void start(Stage stage) throws Exception {
        stage.setTitle("CounterUpDown.Gui tester");
        GridPane pane = new GridPane();
        initContent(pane);
        Scene scene = new Scene(pane, 800,400);
        stage.setScene(scene);
        stage.show();
    }
    Controller controller = new Controller();

    private ListView<Kapsejlads> listviewKapsejladser = new ListView();
    private ListView<Heat> listviewHeats = new ListView<>();
    private TextArea textAreaResultsForHeat = new TextArea();

    // opret heat section
    private TextField textFieldnummer = new TextField();
    private TextField textFieldTidspunkt = new TextField();
    private RadioButton radioButtonFinale = new RadioButton();

    // opret Resultat section
    private ComboBox comboBoxInstitution = new ComboBox();
    private TextField textFieldBaneNummer = new TextField();
    private TextField textFieldTidSekunder = new TextField();

    private void initContent(GridPane pane) {

        SectionVBox kapsejladserVBox = new SectionVBox("Kapsejladser");
        kapsejladserVBox.addNode(listviewKapsejladser);
        Controller.getKapsejladser().forEach(kapsejlads -> listviewKapsejladser.getItems().add(kapsejlads));
        pane.add(kapsejladserVBox, 0, 0);

        SectionVBox heatVBox = new SectionVBox("Heats");
        heatVBox.addNode(listviewHeats);
        pane.add(heatVBox, 1, 0);

        SectionVBox resultatVBox = new SectionVBox("Resultater");
        pane.add(resultatVBox, 2, 0);
        resultatVBox.addNode(textAreaResultsForHeat);

        SectionVBox opretHeatVbox = new SectionVBox("Opret Heat");
        pane.add(opretHeatVbox, 1, 1);
        opretHeatVbox.addLabeledNode("Indtast heat nummer", textFieldnummer);
        opretHeatVbox.addLabeledNode("Indtast tidspunkt", textFieldTidspunkt);
        opretHeatVbox.addLabeledNode("Finale", radioButtonFinale);
        Button btnOpretHeat = new Button("Opret Heat");
        opretHeatVbox.getChildren().add(btnOpretHeat);

        SectionVBox opretResultat = new SectionVBox("Opret resultat");
        pane.add(opretResultat, 2, 1);
        opretResultat.addNode(comboBoxInstitution);
        opretResultat.addLabeledNode("Bane Nummer", textFieldBaneNummer);
        opretResultat.addLabeledNode("Tid i sekunder", textFieldTidSekunder);
        Button btnOpretResultat = new Button("Opret Resultat");
        opretResultat.getChildren().add(btnOpretResultat);

        // end of design -- now to action section

        btnOpretHeat.setOnAction(e -> {
            opretHeat();
            // the newly created heat should appear in the listviewHeats
            listviewHeats.getItems().clear();
            Kapsejlads selectedKapsejlads = listviewKapsejladser.getSelectionModel().getSelectedItem();
            if (selectedKapsejlads != null) {
                selectedKapsejlads.getHeats().forEach(heat -> listviewHeats.getItems().add(heat));
            }
        });

        btnOpretResultat.setOnAction(e -> {
            try {
            opretResultat(); // may throw validation exceptions
            // the newly created Result should appear in the listviewResults
            textAreaResultsForHeat.clear();
            Heat selectedHeat = listviewHeats.getSelectionModel().getSelectedItem();
            if (selectedHeat != null) {
                StringBuilder sb = new StringBuilder();
                for (Resultat resultat : selectedHeat.getResultats()) {
                    if (resultat != null) {
                        sb.append("bane ").append(resultat.getBane()).append(": ");
                        if (resultat.getHold() != null) sb.append(resultat.getHold().getNavn()).append(" - ");
                        sb.append(resultat.getSekunder()).append("s\n");
                    }
                }
                textAreaResultsForHeat.setText(sb.toString());
            }
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING, ex.getMessage());
                alert.showAndWait();
            }
        });

        //populate heats when a kapsejlads is selected
        listviewKapsejladser.getSelectionModel().selectedItemProperty().addListener((obs, oldk, newK) -> {
            listviewHeats.getItems().clear();
            textAreaResultsForHeat.clear();
            if (newK != null) {
                newK.getHeats().forEach(heat -> listviewHeats.getItems().add(heat));
            }
        });

        //show results when a heat is selected
        listviewHeats.getSelectionModel().selectedItemProperty().addListener((obs, oldH, newH) -> {
            textAreaResultsForHeat.clear();
            if (newH != null) {
                StringBuilder sb = new StringBuilder();
                for (Resultat resultat : newH.getResultats()) {
                    if (resultat != null) {
                        sb.append("bane ").append(resultat.getBane()).append(": ");
                        if (resultat.getHold() != null) sb.append(resultat.getHold().getNavn()).append(" - ");
                        sb.append(resultat.getSekunder()).append("s\n");
                    }
                }
                textAreaResultsForHeat.setText(sb.toString());
            }
        });
        // populate combobox
        comboBoxInstitution.getItems().addAll(Controller.getHolds());
    }

    public Heat opretHeat() {
        Kapsejlads kap = listviewKapsejladser.getSelectionModel().getSelectedItem();
        if (kap == null) throw new IllegalArgumentException("Vælg en kapsejlads først");

        int nummer;
        try {
            nummer = Integer.parseInt(textFieldnummer.getText().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ugyldigt heat nummer");
        }

        LocalTime tidspunkt = textFieldTidspunkt.getText().isBlank()
                ? LocalTime.now()
                : LocalTime.parse(textFieldTidspunkt.getText().trim());

        boolean finale = radioButtonFinale.isSelected();

        return Controller.createHeat(kap, nummer, tidspunkt, finale);
    }

    private Resultat opretResultat() {
        // get selected heat and hold
        Heat selectedHeat = listviewHeats.getSelectionModel().getSelectedItem();
        Hold selectedHold = (Hold) comboBoxInstitution.getSelectionModel().getSelectedItem();

        if (selectedHeat == null) {
            throw new IllegalArgumentException("vælg Heat først");
        }
        if (selectedHold == null) {
            throw new IllegalArgumentException("intast et hold som resultat skal modtage resultatet");
        }

        int bane;
        int sekunder;
        try {
            bane = Integer.parseInt(textFieldBaneNummer.getText().trim());
            sekunder = Integer.parseInt(textFieldTidSekunder.getText().trim());

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Bane og sekunder skal være et tal" + ex);
        }

        Resultat resultat = Controller.createResultatValidated(selectedHeat, selectedHold, bane, sekunder);
        return resultat;
    }

}
