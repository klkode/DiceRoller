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

import com.klkode.diceroller.adapters.AdDisvantageRollAdapter;
import com.klkode.diceroller.model.AdDisvantageRoll;
import com.klkode.diceroller.model.RollResult;
import com.klkode.diceroller.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class AdvantageDisadvantageActivity extends AppCompatActivity {

    private static final int DEFAULT_MOD = 0;
    private static final int DICE_TYPE = 20;

    //Editable Fields
    RadioButton posRBtn;
    RadioButton negRBtn;
    EditText modifierTxt;
    RadioButton advantageRBtn;
    RadioButton disadvantageRBtn;

    //Roll History Variables
    RecyclerView rollHistoryView;
    AdDisvantageRollAdapter adapter;
    ArrayList<AdDisvantageRoll> rollHistoryList;

    //Random Generator
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advantage_disadvantage);

        //Set Title
        setTitle(R.string.advantage_disadvantage_name);

        //Decide if Orientation Lock needed
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        //Create the Random Generator
        random = new Random();

        //Create empty History List
        rollHistoryList = new ArrayList<AdDisvantageRoll>();

        //Hide Keyboard
        setupUI(findViewById(R.id.advantageRollLayout));

        //Initialize image and data fields
        posRBtn = (RadioButton) findViewById(R.id.advDisPositiveRBtn);
        negRBtn = (RadioButton) findViewById(R.id.advDisNegativeRBtn);
        modifierTxt = (EditText) findViewById(R.id.advDisModifierTxt);
        advantageRBtn = (RadioButton) findViewById(R.id.advDisAdvantageRBtn);
        disadvantageRBtn = (RadioButton) findViewById(R.id.advDisDisadvantageRBtn);

        //Set Defaults
        modifierTxt.setText(Integer.toString(DEFAULT_MOD));

        //Set Edit Text to Clear when touched
        clearEditViewsOnFocus();

        //Create RecycleView and Adapter
        rollHistoryView = (RecyclerView) findViewById(R.id.advDisRollHistoryView);
        rollHistoryView.setHasFixedSize(true);
        rollHistoryView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdDisvantageRollAdapter(this, rollHistoryList);
        rollHistoryView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //BUTTON METHODS
    //Method to roll the die the user has chosen
    public void rollDoubleDice(View view){
        //Check that modifier has been entered or give ERR
        if(modifierTxt.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.err_missing_modifier, Toast.LENGTH_SHORT).show();

        }else {
            //Get data
            boolean isPositiveMod = posRBtn.isChecked();
            int modifier = Integer.parseInt(modifierTxt.getText().toString());
            boolean isAdvantage = advantageRBtn.isChecked();

            //Make two rolls
            RollResult result1 = generateRollResult(isPositiveMod, modifier);
            RollResult result2 = generateRollResult(isPositiveMod, modifier);

            //Save the rolls in an AdDisvantageRoll (order by higher result number)
            AdDisvantageRoll adDisvantageRoll;
            if(result1.getResult() >= result2.getResult()) {
                adDisvantageRoll= new AdDisvantageRoll(result1.getDetails(), result1.getResult(),
                        result2.getDetails(), result2.getResult(), isAdvantage);
            }else{
                adDisvantageRoll= new AdDisvantageRoll(result2.getDetails(), result2.getResult(),
                        result1.getDetails(), result1.getResult(), isAdvantage);
            }

            //Add  it to the Roll History
            rollHistoryList.add(0, adDisvantageRoll);
            adapter.notifyDataSetChanged();

        }

    }

    //Method to clear the results from the Dice Results History RecycleView
    public void clearAdDisvantageHistory(View view){
        rollHistoryList.clear();
        adapter.notifyDataSetChanged();

    }

    //Method to close Activity
    public void back(View view){
        this.finish();
    }

    //HELPER METHOD
    private RollResult generateRollResult(boolean isPosMod, int modifier){
        //Generate the roll
        int roll = random.nextInt(DICE_TYPE) + 1;

        //Create the RollResult Statement, Details, and Result
        String statement = "1d20 ";
        String details = "( " + roll + " ) ";
        int result = roll;
        if(isPosMod){
            statement += "+ " + modifier;
            details += "+ " + modifier;
            result += modifier;
        }else{
            statement += "- " + modifier;
            details += "- " + modifier;
            result -= modifier;
        }

        //Create the finished Roll Result and return it
        return new RollResult(statement, details, result);
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
                    hideKeyboard(AdvantageDisadvantageActivity.this);
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

    //CLEAR EDITTEXT HELPER
    //On Focus
    public void clearEditViewsOnFocus(){
        modifierTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    modifierTxt.getText().clear();
                }
            }
        });
    }

}
