package model;

public class Resultat {
    private int bane;
    private int sekunder;
    private Hold hold;
    private Heat heat;

    public Resultat(int bane, int sekunder, Heat heat) {
        this.bane = bane;
        this.sekunder = sekunder;
        this.heat = heat;
        this.setHold(hold);
    }

    public int getBane() {
        return bane;
    }

    public int getSekunder() {
        return sekunder;
    }
    public Heat getHeat(){
        return heat;
    }

    public void setHold(Hold hold) {
        if (this.hold != hold){
            Hold oldhold = this.hold;
            if (oldhold != null){
                oldhold.removeResultat(this);
            }
            this.hold = hold;
            if (hold != null){
                hold.addResultat(this);
            }
        }
    }
    public String visTid(){
        int minutter = sekunder / 60;
        int resterendeSekunder = sekunder % 60;
        return minutter + ":" + resterendeSekunder;
    }

    public Hold getHold() {
        return hold;
    }
}
