package model;

import java.util.ArrayList;

public class Hold {
    private String navn;
    private String institution;
    private ArrayList<Resultat> resultats = new ArrayList<>();

    public Hold(String navn, String institution) {
        this.navn = navn;
        this.institution = institution;
    }

    public String getNavn() {
        return navn;
    }

    public String getInstitution() {
        return institution;
    }

    public void addResultat(Resultat resultat){
        if (!resultats.contains(resultat)){
            resultats.add(resultat);
            resultat.setHold(this);
        }
    }
    public void removeResultat(Resultat resultat){
        if (resultats.contains(resultat)){
            resultats.remove(resultat);
            resultat.setHold(null);
        }
    }
    @Override
    public String toString(){
        return navn + " (" + institution + ")";
    }
}
