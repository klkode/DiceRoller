package com.klkode.diceroller.controller;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.klkode.diceroller.adapters.RollHistoryAdapter;
import com.klkode.diceroller.model.RollResult;
import com.klkode.diceroller.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class MultiDiceRollActivity extends AppCompatActivity {

    private static final int DEFAULT_AMOUNT = 0;
    private static final int EMPTY_OTHER = -2;

    //Editable Fields
    EditText amountd2Txt;
    EditText amountd4Txt;
    EditText amountd6Txt;
    EditText amountd8Txt;
    EditText amountd10Txt;
    EditText amountd12Txt;
    EditText amountd20Txt;
    EditText amountd100Txt;
    RadioButton posRBtn;
    RadioButton negRBtn;
    EditText modifierTxt;
    EditText otherTypeAmountTxt;
    EditText otherTypeTypeTxt;

    //Roll History Variables
    RecyclerView rollHistoryView;
    RollHistoryAdapter adapter;
    ArrayList<RollResult> rollHistoryList;

    //Random Generator
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_dice_roll);

        //Set Title
        setTitle(R.string.multi_name);

        //Decide if Orientation Lock needed
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        //Create the Random Generator
        random = new Random();

        //Create empty History List
        rollHistoryList = new ArrayList<RollResult>();

        //Hide Keyboard
        setupUI(findViewById(R.id.multiRollLayout));

        //Initialize image and data fields
        amountd2Txt = (EditText) findViewById(R.id.multid2Txt);
        amountd4Txt = (EditText) findViewById(R.id.multid4Txt);
        amountd6Txt = (EditText) findViewById(R.id.multid6Txt);
        amountd8Txt = (EditText) findViewById(R.id.multid8Txt);
        amountd10Txt = (EditText) findViewById(R.id.multid10Txt);
        amountd12Txt = (EditText) findViewById(R.id.multid12Txt);
        amountd20Txt = (EditText) findViewById(R.id.multid20Txt);
        amountd100Txt = (EditText) findViewById(R.id.multid100Txt);
        posRBtn = (RadioButton) findViewById(R.id.multiPlusRBtn);
        negRBtn = (RadioButton) findViewById(R.id.multiMinusRBtn);
        modifierTxt = (EditText) findViewById(R.id.multiModifierTxt);
        otherTypeAmountTxt = (EditText) findViewById(R.id.multiOtherAmountTxt);
        otherTypeTypeTxt = (EditText) findViewById(R.id.multiDOtherTxt);

        //Set Edit Text to Clear when touched
        clearEditViewsOnFocus();

        //Set Edit Text field defaults
        setInputDefaults();

        //Create RecycleView and Adapter
        rollHistoryView = (RecyclerView) findViewById(R.id.multiRollHistoryView);
        rollHistoryView.setHasFixedSize(true);
        rollHistoryView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RollHistoryAdapter(this, rollHistoryList);
        rollHistoryView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //BUTTON METHODS
    //Method to roll the die the user has chosen
    public void rollMultiDice(View view){
        //Fill in default values if any Amount Fields were deleted and left empty
        fillEmptyFields();

        //Check that modifier has been entered or give ERR
        if(checkFields()){
            //Get data
            boolean isPositiveMod = posRBtn.isChecked();
            int modifier = Integer.parseInt(modifierTxt.getText().toString());
            int d2Amt = Integer.parseInt(amountd2Txt.getText().toString());
            int d4Amt = Integer.parseInt(amountd4Txt.getText().toString());
            int d6Amt = Integer.parseInt(amountd6Txt.getText().toString());
            int d8Amt = Integer.parseInt(amountd8Txt.getText().toString());
            int d10Amt = Integer.parseInt(amountd10Txt.getText().toString());
            int d12Amt = Integer.parseInt(amountd12Txt.getText().toString());
            int d20Amt = Integer.parseInt(amountd20Txt.getText().toString());
            int d100Amt = Integer.parseInt(amountd100Txt.getText().toString());
            int dOtherAmt = Integer.parseInt(otherTypeAmountTxt.getText().toString());
            int dOtherType = EMPTY_OTHER;
            if(dOtherAmt != 0){
                dOtherType = Integer.parseInt(otherTypeTypeTxt.getText().toString());
            }

            //Generate the rolls and Create the Roll Result
            RollResult rollResult = new RollResult();
            if(d2Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d2Amt, 2));
            }
            if(d4Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d4Amt, 4));
            }
            if(d6Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d6Amt, 6));
            }
            if(d8Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d8Amt, 8));
            }
            if(d10Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d10Amt, 10));
            }
            if(d12Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d12Amt, 12));
            }
            if(d20Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d20Amt, 20));
            }
            if(d100Amt > 0) {
                rollResult.mergeRolls(generateRollResult(d100Amt, 100));
            }
            if(dOtherAmt > 0 && dOtherType != EMPTY_OTHER) {
                rollResult.mergeRolls(generateRollResult(dOtherAmt, dOtherType));
            }

            //Add the modifiers to the roll result components
            String modifierDesc = createModifierString(isPositiveMod, modifier);
            if(!modifierDesc.isEmpty()){
                rollResult.addToStatement(modifierDesc);
                rollResult.addToDetails(modifierDesc);

                if(isPositiveMod){
                    rollResult.addToResult(modifier);
                }else{
                    rollResult.addToResult((-1*modifier));
                }
            }


            //Add  it to the Roll History
            rollHistoryList.add(0, rollResult);
            adapter.notifyDataSetChanged();

        }

    }

    //Set all Edit Text fields to their default value
    public void resetMulti(View view){
        setInputDefaults();
    }

    //Method to clear the results from the Dice Results History RecycleView
    public void clearMultiHistory(View view){
        rollHistoryList.clear();
        adapter.notifyDataSetChanged();

    }

    //Method to close Activity
    public void back(View view){
        this.finish();
    }

    //IMG BUTTON METHODS
    //Increase the d2 amount by 1
    public void increase2D(View v){
        String currVal = amountd2Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd2Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd2Txt.setText(Integer.toString(value));
        }
    }

    //Increase the d4 amount by 1
    public void increase4D(View v){
        String currVal = amountd4Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd4Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd4Txt.setText(Integer.toString(value));
        }
    }

    //Increase the d6 amount by 1
    public void increase6D(View v){
        String currVal = amountd6Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd6Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd6Txt.setText(Integer.toString(value));
        }
    }

    //Increase the d8 amount by 1
    public void increase8D(View v){
        String currVal = amountd8Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd8Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd8Txt.setText(Integer.toString(value));
        }
    }

    //Increase the d10 amount by 1
    public void increase10D(View v){
        String currVal = amountd10Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd10Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd10Txt.setText(Integer.toString(value));
        }
    }

    //Increase the d12 amount by 1
    public void increase12D(View v){
        String currVal = amountd12Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd12Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd12Txt.setText(Integer.toString(value));
        }
    }

    //Increase the d20 amount by 1
    public void increase20D(View v){
        String currVal = amountd20Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd20Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd20Txt.setText(Integer.toString(value));
        }
    }

    //Increase the d100 amount by 1
    public void increase100D(View v){
        String currVal = amountd100Txt.getText().toString();
        if(currVal.isEmpty()){
            amountd100Txt.setText(Integer.toString(DEFAULT_AMOUNT + 1));
        }else {
            int value = Integer.parseInt(currVal);
            value++;
            amountd100Txt.setText(Integer.toString(value));
        }
    }

    //HELPER METHODS
    //Set the default value of the Edit Text fields
    public void setInputDefaults(){
        amountd2Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        amountd4Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        amountd6Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        amountd8Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        amountd10Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        amountd12Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        amountd20Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        amountd100Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        modifierTxt.setText(Integer.toString(DEFAULT_AMOUNT));
        otherTypeAmountTxt.setText(Integer.toString(DEFAULT_AMOUNT));
        otherTypeTypeTxt.getText().clear();
    }

    //If the user has deleted any of the fields and left them empty, put back their default value
    public void fillEmptyFields(){
        if(amountd2Txt.getText().toString().isEmpty()){
            amountd2Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(amountd4Txt.getText().toString().isEmpty()){
            amountd4Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(amountd6Txt.getText().toString().isEmpty()){
            amountd6Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(amountd8Txt.getText().toString().isEmpty()){
            amountd8Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(amountd10Txt.getText().toString().isEmpty()){
            amountd10Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(amountd12Txt.getText().toString().isEmpty()){
            amountd12Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(amountd20Txt.getText().toString().isEmpty()){
            amountd20Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(amountd100Txt.getText().toString().isEmpty()){
            amountd100Txt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
        if(otherTypeAmountTxt.getText().toString().isEmpty()){
            otherTypeAmountTxt.setText(Integer.toString(DEFAULT_AMOUNT));
        }
    }

    //Check the inputs and produce an error if something is missing or incorrect
    public boolean checkFields(){
        //Make sure that if the user has accidentally delete fields, set them to zero
        fillEmptyFields();

        //Check that if the dOther field is empty, that the amount is zero, otherwise err
        if(otherTypeTypeTxt.getText().toString().isEmpty()){
            if (Integer.parseInt(otherTypeAmountTxt.getText().toString()) != 0){
                Toast.makeText(getApplicationContext(), R.string.err_empty_die_type, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            //Make sure the user hasn't chosen 0 for the value of the dOther die
            if (Integer.parseInt(otherTypeTypeTxt.getText().toString()) == 0){
                Toast.makeText(getApplicationContext(), R.string.err_zero_die_type, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //Check that there is at least one die to roll
        if(!selectionMade()){
            Toast.makeText(getApplicationContext(), R.string.err_no_roll_selected, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Check that the modifier field is not empty
        if(modifierTxt.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.err_missing_modifier, Toast.LENGTH_SHORT).show();
            return false;
        }

        //If made it to the end, no errors
        return true;
    }

    //Check that at least one field is non-zero
    public boolean selectionMade() {
        if(Integer.parseInt(amountd2Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(amountd4Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(amountd6Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(amountd8Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(amountd10Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(amountd12Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(amountd20Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(amountd100Txt.getText().toString()) != 0){
            return true;
        }else if(Integer.parseInt(otherTypeAmountTxt.getText().toString()) != 0){
            return true;
        }

        //If made it to the end, then no dice were selected to roll
        return false;
    }

    //Create the Roll Result
    @NotNull
    private RollResult generateRollResult(int amount, int diceType){
        //Create the RollResult Statement
        String statement = createRollStatement(amount, diceType);

        //Generate the rolls
        int[] diceRolls = rollDice(amount, diceType);

        //Generate the RollResult Details
        String details = createRollDetails(diceRolls);

        //Generate the RollResult Result
        int result = createRollResult(diceRolls);

        //Create the finished Roll Result and return it
        return new RollResult(statement, details, result);
    }

    //Create the statement component of a RollResult without modifiers
    private String createRollStatement(int amount, int diceType){
        //Create the AMT# d TYPE# Base
        String statement = amount + "d" + diceType;

        //Return the finished statement
        return statement;
    }

//    //Create the statement component of a RollResult with modifiers
//    private String createRollStatement(int amount, int diceType, boolean isPosMod, int modifier){
//        //Create the AMT# d TYPE# Base
//        String statement = amount + "d" + diceType;
//
//        //Add the modifier based on whether is it positive or negative
//        //Ignore adding it if there is no modifier
//        String modifierDesc = "";
//        if(modifier != 0 && isPosMod){
//            modifierDesc = " + " + Integer.toString(modifier);
//        }else if(modifier != 0){
//            modifierDesc = " - " + Integer.toString(modifier);
//        }
//        statement += modifierDesc;
//
//        //Return the finished statement
//        return statement;
//    }

    //Create an int array that contains the values of each dice roll
    private int[] rollDice(int amount, int diceType) {
        //Create empty array
        int[] results = new int[amount];

        //Populate array with random numbers of the dice type
        for (int i = 0; i < amount; i++){
            results[i] = random.nextInt(diceType) + 1;
        }

        //Return populated array
        return results;
    }

    //Create the details component of a RollResult without modifiers
    private String createRollDetails(@NotNull int[] diceRolls) {
        //Create start of details
        String details = "( ";

        //Add all of the dice rolls to the details String with spacing
        for(int i = 0; i < diceRolls.length; i++){
            details += diceRolls[i];
            if(i != diceRolls.length - 1){
                details += ", ";
            }else {
                details += " )";
            }
        }

        //Return the finished details String
        return details;
    }

//    //Create the details component of a RollResult with modifiers
//    private String createRollDetails(@NotNull int[] diceRolls, boolean isPosMod, int modifier) {
//        //Create start of details
//        String details = "( ";
//
//        //Add all of the dice rolls to the details String with spacing
//        for(int i = 0; i < diceRolls.length; i++){
//            details += diceRolls[i];
//            if(i != diceRolls.length - 1){
//                details += ", ";
//            }else {
//                details += " )";
//            }
//        }
//
//        //Add the modifier text if there is a modifier
//        if(modifier != 0 && isPosMod){
//            details += " + " + modifier;
//        }else if(modifier != 0){
//            details += " - " + modifier;
//        }
//
//        //Return the finished details String
//        return details;
//    }

    //Create the result component of a RollResult without modifiers
    private int createRollResult(@NotNull int[] diceRolls) {
        //Create base for the result
        int result = 0;

        //Add all of the dice rolls together
        for(int roll : diceRolls){
            result += roll;
        }

        //Return the final number
        return result;
    }

//    //Create the result component of a RollResult with modifiers
//    private int createRollResult(@NotNull int[] diceRolls, boolean isPosMod, int modifier) {
//        //Create base for the result
//        int result = 0;
//
//        //Add all of the dice rolls together
//        for(int roll : diceRolls){
//            result += roll;
//        }
//
//        //Add/Subtract the modifier
//        if(isPosMod){
//            result += modifier;
//        }else if(modifier != 0){
//            result -= modifier;
//        }
//
//        //Return the final number
//        return result;
//    }

    //Create the string of the modifier to add to the final roll statement
    private String createModifierString(boolean isPosMod, int modifier){
        String modifierDesc = "";
        if(modifier != 0 && isPosMod){
            modifierDesc = " + " + Integer.toString(modifier);
        }else if(modifier != 0){
            modifierDesc = " - " + Integer.toString(modifier);
        }
        return modifierDesc;
    }


    //CLEAR EDITTEXT HELPERS
    //On Focus
    public void clearEditViewsOnFocus(){
        amountd2Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd2Txt.getText().clear();
                }
            }
        });
        amountd4Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd4Txt.getText().clear();
                }
            }
        });
        amountd6Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd6Txt.getText().clear();
                }
            }
        });
        amountd8Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd8Txt.getText().clear();
                }
            }
        });
        amountd10Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd10Txt.getText().clear();
                }
            }
        });
        amountd12Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd12Txt.getText().clear();
                }
            }
        });
        amountd20Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd20Txt.getText().clear();
                }
            }
        });
        amountd100Txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    amountd100Txt.getText().clear();
                }
            }
        });
        modifierTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    modifierTxt.getText().clear();
                }
            }
        });
        otherTypeAmountTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    otherTypeAmountTxt.getText().clear();
                }
            }
        });
        otherTypeTypeTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    otherTypeTypeTxt.getText().clear();
                }
            }
        });
    }

    //KEYBOARD HIDING METHODS From StackOverflow
    public static void hideKeyboard(@NotNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(MultiDiceRollActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}
