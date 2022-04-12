package com.klkode.diceroller.controller;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.klkode.diceroller.adapters.AbilityScoreAdapter;
import com.klkode.diceroller.model.AbilityScores;
import com.klkode.diceroller.model.RollResult;
import com.klkode.diceroller.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CharacterAbilityRollActivity extends AppCompatActivity {

    private static final int DEFAULT_AMOUNT_POS = 1;
    private static final int DEFAULT_REROLL_POS = 1;

    private static final int NO_REROLL_ID = 0;
    private static final int REROLL_ID = 1;
    private static final int REROLL_ONCE_ID = 2;
    private static final int REROLL_ONE_ID = 3;

    ArrayList<String> amountDiceToRollOpts;
    ArrayList<String> reroll1sOpts;

    Spinner amountDiceDropdown;
    Spinner reroll1sOptionsDropdown;

    RecyclerView abilityScoresView;
    AbilityScoreAdapter abilityAdapter;

    //Random Generator
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_ability_roll);

        //Set Title
        setTitle(R.string.char_ability_name);

        //Decide if Orientation Lock needed
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        //Create the Random Generator
        random = new Random();

        //Populate options
        populateDiceAmountList();
        populateReroll1List();

        //Initialize dropdowns
        amountDiceDropdown = (Spinner)findViewById(R.id.charAbAmountDropDown);
        reroll1sOptionsDropdown = (Spinner)findViewById(R.id.charAbRerollDropDown);

        //Populate Dice Amount dropdown and set default position
        ArrayAdapter<String> amountDropdownAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, amountDiceToRollOpts);
        amountDiceDropdown.setAdapter(amountDropdownAdapter);
        amountDiceDropdown.setSelection(DEFAULT_AMOUNT_POS);

        //Populate Reroll 1s dropdown and set default position
        ArrayAdapter<String> rerollDropdownAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, reroll1sOpts);
        reroll1sOptionsDropdown.setAdapter(rerollDropdownAdapter);
        reroll1sOptionsDropdown.setSelection(DEFAULT_REROLL_POS);

        //Create RecycleView and Adapter
        abilityScoresView = (RecyclerView) findViewById(R.id.charAbRollView);
        abilityScoresView.setLayoutManager(new LinearLayoutManager(this));
        abilityScoresView.setHasFixedSize(true);
        abilityAdapter = new AbilityScoreAdapter(this);
        abilityScoresView.setAdapter(abilityAdapter);

    }



    //BUTTON METHODS
    //Method to roll the die the user has chosen
    public void rollCharAbilityScores(View view){
        //Get the selections from the Dropdowns
        int amountSelection = numberOfDiceToRoll(amountDiceDropdown.getSelectedItemPosition());
        int rerollSelection = rerollOptionId(reroll1sOptionsDropdown.getSelectedItemPosition());

        //Generate the Ability Score Dice Rolls
        ArrayList<RollResult> abilityScoreData = new ArrayList<RollResult>();
        for(int i = 0; i < AbilityScores.NUM_ABILITIES; i++) {
            abilityScoreData.add(rollAbility(amountSelection, rerollSelection));
        }

        //Create AbilityScoresObject
        AbilityScores characterRoll = new AbilityScores(abilityScoreData);

        //Add  it to the Ability Score View History
        abilityAdapter.setAbilityScores(characterRoll);
        abilityAdapter.notifyDataSetChanged();

    }

    //Method to close Activity
    public void back(View view){
        this.finish();
    }

    //HELPER METHODS
    //Roll one character ability score
    public RollResult rollAbility(int numDice, int rerollOption){
        //Create three components of a RollResult
        String statement = numDice + "d6";
        String details = "";
        int result = 0;

        //Create a holder for the rolls
        int[] rolls = new int[numDice];



        //Generate the rolls and details for the Always Reroll 1s Option
        if(rerollOption == REROLL_ID){
            rollD6No1s(rolls);
            details = rollDetails(rolls);

        }else{
            //Roll once and record the original details
            rollD6s(rolls);
            details = rollDetails(rolls);

            //Determine how many 1s there are
            int num1s = numOnes(rolls);

            //If the No Rerolling Option, nothing else needs to be done, otherwise
            //If there are 1s, reroll and replace the values and record the second set of details
            //Reroll All 1s Option
            if(num1s > 0 && rerollOption == REROLL_ONCE_ID){
                int[] rerolls = new int[num1s];
                rollD6s(rerolls);
                replaceOnes(rolls, rerolls);
                details += "\n" + rollDetails(rerolls);

            }
            //Reroll One 1 Option
            else if(num1s > 0 && rerollOption == REROLL_ONE_ID){
                int[] reroll = {random.nextInt(6) + 1};
                replaceOnes(rolls, reroll);
                details += "\n( " + reroll[0] + " )";

            }

        }

        //Determine the final result
        result = calculateResult(rolls);

        //Complete the RollResult Object and return it
        RollResult rollResult = new RollResult(statement, details, result);
        return rollResult;
    }

    //Populate an array of ints representing d6 rolls of the inputted size
    public void rollD6s(int[] rolls){
        for(int i = 0; i < rolls.length; i++){
            rolls[i] = random.nextInt(6) + 1;
        }
    }

    //Populate an array of ints representing d6 rolls of the inputted size, not allowing for a 1 roll result
    public void rollD6No1s(int rolls[]){
        for(int i = 0; i < rolls.length; i++){
            rolls[i] = random.nextInt(5) + 2;
        }
    }

    //Return the number of 1s in the array
    public int numOnes(int[] rolls){
        int total = 0;
        for(int i = 0; i < rolls.length; i++){
            if(rolls[i] == 1){
                total++;
            }
        }

        return total;
    }

    //For each value in rerolls, replace a 1 in rolls
    public void replaceOnes(int[] rolls, int[] rerolls){
        int replaceIndex = 0;
        for(int i = 0; (i < rolls.length && replaceIndex < rerolls.length); i++){
            if(rolls[i] == 1){
                rolls[i] = rerolls[replaceIndex];
                replaceIndex++;
            }
        }
    }

    //Create a details String for the inputted dice rolls
    public String rollDetails(int[] rolls){
        String details = "("; //Make beginning of details String

        //Add the rolls to the String with spacing
        for(int roll : rolls){
            details += " " + roll + ",";
        }

        //Get rid of trailing comma and finish the details String
        details = details.substring(0, (details.length() - 1));
        details += " )";

        //Finished so return
        return details;

    }

    //Get the three largest numbers in the array and return their sum
    public int calculateResult(int[] rolls){
        Arrays.sort(rolls);
        int size = rolls.length;
        return rolls[size - 1] + rolls[size - 2] + rolls[size - 3];

    }

    //Return the number of dice rolls needed based on the selected option
    public int numberOfDiceToRoll(int option){
        String selected = amountDiceToRollOpts.get(option);

        if(selected.contains("3")){ //Three Dice option
            return 3;
        }else if(selected.contains("4")){ //Four Dice option
            return 4;
        }else if(selected.contains("5")){ //Five Dice option
            return 5;
        }else if(selected.contains("6")){ //Six Dice option
            return 6;
        }else { //Something went wrong
            return 0;
        }
    }

    //Return the number of dice rolls needed based on the selected option
    public int rerollOptionId(int option){
        String selected = reroll1sOpts.get(option);

        if(selected.equals(getString(R.string.ones_opt_no_reroll))){ //No Rerolls option
            return NO_REROLL_ID;
        }else if(selected.equals(getString(R.string.ones_opt_reroll))){ //Always Reroll option
            return REROLL_ID;
        }else if(selected.equals(getString(R.string.ones_opt_reroll_once))){ //Reroll 1s once option
            return REROLL_ONCE_ID;
        }else if(selected.equals(getString(R.string.ones_opt_reroll_one))){ //Only one 1 rerolled option
            return REROLL_ONE_ID;
        }else { //Something went wrong
            return -1;
        }
    }



    //POPULATE METHODS
    //Populate the list to be used for the "amount of dice to roll" dropdown
    private void populateDiceAmountList(){
        amountDiceToRollOpts = new ArrayList<String>();
        amountDiceToRollOpts.add(getString(R.string.dice_opt_three));
        amountDiceToRollOpts.add(getString(R.string.dice_opt_four));
        amountDiceToRollOpts.add(getString(R.string.dice_opt_five));
        amountDiceToRollOpts.add(getString(R.string.dice_opt_six));
    }

    //Populate the list to be used for the "reroll 1s options" dropdown
    private void populateReroll1List(){
        reroll1sOpts = new ArrayList<String>();
        reroll1sOpts.add(getString(R.string.ones_opt_no_reroll));
        reroll1sOpts.add(getString(R.string.ones_opt_reroll));
        reroll1sOpts.add(getString(R.string.ones_opt_reroll_once));
        reroll1sOpts.add(getString(R.string.ones_opt_reroll_one));
    }

}
