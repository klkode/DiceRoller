package com.klkode.diceroller.model;

public class AdDisvantageRoll {

    String detailsHigh;
    int resultHigh;
    String detailsLow;
    int resultLow;
    boolean isAdvantageRoll;

    //CONSTRUCTORS

    public AdDisvantageRoll() {
        this.detailsHigh = "";
        this.resultHigh = -1;
        this.detailsLow = "";
        this.resultLow = -1;
        this.isAdvantageRoll = true;
    }

    public AdDisvantageRoll(String detailsHigh, int resultHigh, String detailsLow, int resultLow, boolean isAdvantageRoll) {
        this.detailsHigh = detailsHigh;
        this.resultHigh = resultHigh;
        this.detailsLow = detailsLow;
        this.resultLow = resultLow;
        this.isAdvantageRoll = isAdvantageRoll;
    }

    //GETTERS AND SETTERS
    public String getDetailsHigh() {
        return detailsHigh;
    }

    public void setDetailsHigh(String detailsHigh) {
        this.detailsHigh = detailsHigh;
    }

    public int getResultHigh() {
        return resultHigh;
    }

    public void setResultHigh(int resultHigh) {
        this.resultHigh = resultHigh;
    }

    public String getDetailsLow() {
        return detailsLow;
    }

    public void setDetailsLow(String detailsLow) {
        this.detailsLow = detailsLow;
    }

    public int getResultLow() {
        return resultLow;
    }

    public void setResultLow(int resultLow) {
        this.resultLow = resultLow;
    }

    public boolean isAdvantageRoll() {
        return isAdvantageRoll;
    }

    public void setAdvantageRoll(boolean advantageRoll) {
        isAdvantageRoll = advantageRoll;
    }
}
