package com.klkode.diceroller.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AbilityScores {

    public static final int NUM_ABILITIES = 6;

    String[] abilityScoreDescs;
    int[] abilityScoreResults;

    //CONTRUCTORS
    public AbilityScores() {
        this.abilityScoreDescs = new String[NUM_ABILITIES];
        this.abilityScoreResults = new int[NUM_ABILITIES];
    }

    public AbilityScores(String[] abilityScoreDescs, int[] abilityScoreResults) {
        this.abilityScoreDescs = abilityScoreDescs;
        this.abilityScoreResults = abilityScoreResults;
    }

    public AbilityScores(@NotNull ArrayList<RollResult> scores){
        this.abilityScoreDescs = new String[NUM_ABILITIES];
        this.abilityScoreResults = new int[NUM_ABILITIES];

        if(scores.size() == NUM_ABILITIES){
            for (int i = 0; i < NUM_ABILITIES; i++){
                this.abilityScoreDescs[i] = scores.get(i).getDetails();
                this.abilityScoreResults[i] = scores.get(i).getResult();
            }
        }
    }

    //GETTERS AND SETTERS
    public String[] getAbilityScoreDescs() {
        return abilityScoreDescs;
    }

    public void setAbilityScoreDescs(String[] abilityScoreDescs) {
        this.abilityScoreDescs = abilityScoreDescs;
    }

    public int[] getAbilityScoreResults() {
        return abilityScoreResults;
    }

    public void setAbilityScoreResults(int[] abilityScoreResults) {
        this.abilityScoreResults = abilityScoreResults;
    }

    //Helper Methods
    //Set/Replace one ability score
    public void setOneAbility(int position, String desc, int score){
        if(position < NUM_ABILITIES && position >= 0){
            this.abilityScoreDescs[position] = desc;
            this.abilityScoreResults[position] = score;
        }
    }

    //Get the result of one ability score at given position
    public int getResultAt(int position){
        if(position < NUM_ABILITIES && position >=0){
            return abilityScoreResults[position];
        }
        return -1;
    }

    //Get the result of one ability score decription at given position
    public String getDescriptionAt(int position){
        if(position < NUM_ABILITIES && position >=0){
            return abilityScoreDescs[position];
        }
        return "";
    }

}
