package com.klkode.diceroller.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.klkode.diceroller.adapters.RollHistoryAdapter;
import com.klkode.diceroller.R;
import com.klkode.diceroller.model.RollResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> diceTypes;
    ArrayList<Integer> diceImgs;

    private static final int DEFAULT_DROPDOWN_POS = 1;
    private static final int DEFAULT_AMOUNT = 1;
    private static final int DEFAULT_MOD = 0;
    private static final int DEFAULT_DIE_IMG_ID = R.drawable.d6_green;
    private static final int EMPTY_OTHER = -2;

    //Editable Fields
    ImageView dieImg;
    EditText amountTxt;
    Spinner diceTypeDropDown;
    TextView otherTypeLbl;
    EditText otherTypeTxt;
    RadioButton posRBtn;
    RadioButton negRBtn;
    EditText modifierTxt;

    //Roll History Variables
    RecyclerView rollHistoryView;
    RollHistoryAdapter adapter;
    ArrayList<RollResult> rollHistoryList;

    //Random Generator
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Decide if Orientation Lock needed
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        //Hide Keyboard
        setupUI(findViewById(R.id.mainRollLayout));

        //Create the Random Generator
        random = new Random();

        //Create empty History List
        rollHistoryList = new ArrayList<RollResult>();

        //Populate the possible ids for Dice Images
        populateImageIDs();

        //Initialize image and data fields
        dieImg = (ImageView) findViewById(R.id.diceImg);
        amountTxt = (EditText) findViewById(R.id.amountTxt);
        diceTypeDropDown = (Spinner) findViewById(R.id.diceTypeDropDown);
        otherTypeLbl = (TextView) findViewById(R.id.otherDiceLbl);


        otherTypeTxt =  (EditText) findViewById(R.id.otherDiceTypeTxt);
        posRBtn = (RadioButton) findViewById(R.id.positiveRBtn);
        //negRBtn = (RadioButton) findViewById(R.id.negativeRBtn);
        modifierTxt = (EditText) findViewById(R.id.modifierTxt);


        //Populate the dice type dropdown
        populateDiceType();
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, diceTypes);
        diceTypeDropDown.setAdapter(dropdownAdapter);

        //Set up Dropdown Listener
        diceTypeDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Check if the user selected an option that isn't the OTHER option
                if (position < (diceTypes.size() - 1)) {
                    //If the user is switching from the OTHER choice, hide and disable the other EditText
                    if (otherTypeTxt.isEnabled()) {
                        otherTypeLbl.setVisibility(View.GONE);
                        otherTypeTxt.setVisibility(View.GONE);
                        otherTypeTxt.setEnabled(false);
                    }
                } else {
                    //If the user selected OTHER, enable and show the other EditView
                    otherTypeTxt.setEnabled(true);
                    otherTypeTxt.setVisibility(View.VISIBLE);
                    otherTypeLbl.setVisibility(View.VISIBLE);
                }

                //Change the dice type image on new type selection
                dieImg.setImageResource(diceImgs.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //DO NOTHING
            }
        });

        //Set Defaults
        dieImg.setImageResource(DEFAULT_DIE_IMG_ID);
        amountTxt.setText(Integer.toString(DEFAULT_AMOUNT));
        diceTypeDropDown.setSelection(DEFAULT_DROPDOWN_POS);
        modifierTxt.setText(Integer.toString(DEFAULT_MOD));

        //Set Edit Text to Clear when touched
        clearEditViewsOnFocus();

        //Create RecycleView and Adapter
        rollHistoryView = (RecyclerView) findViewById(R.id.rollHistoryView);
        rollHistoryView.setHasFixedSize(false);
        rollHistoryView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RollHistoryAdapter(this, rollHistoryList);
        rollHistoryView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //MENU METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menuAdvDis) {
            Intent intent = new Intent(getApplicationContext(), AdvantageDisadvantageActivity.class);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.menuCharRoll) {
            Intent intent = new Intent(getApplicationContext(), CharacterAbilityRollActivity.class);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.menuMultiType) {
            Intent intent = new Intent(getApplicationContext(), MultiDiceRollActivity.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    //BUTTON METHODS
    //Method to roll the die the user has chosen
    public void rollDie(View view) {
        //Check that amount has been entered or give ERR
        if (amountTxt.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.err_missing_amount, Toast.LENGTH_SHORT).show();

        }
        //Check that modifier has been entered or give ERR
        else if (modifierTxt.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.err_missing_modifier, Toast.LENGTH_SHORT).show();

        } else {
            //Get easy input data
            int amount = Integer.parseInt(amountTxt.getText().toString());
            boolean isPositiveMod = posRBtn.isChecked();
            int modifier = Integer.parseInt(modifierTxt.getText().toString());

            //Determine dice type
            int dieType;
            if (diceTypeDropDown.getSelectedItemPosition() == (diceTypes.size() - 1)) {
                //The user chose a unique dice type
                //Check to make sure that the field isn't empty
                String dieTypeStr = otherTypeTxt.getText().toString();
                if (!dieTypeStr.isEmpty()) {
                    dieType = Integer.parseInt(otherTypeTxt.getText().toString());
                } else {
                    dieType = EMPTY_OTHER;
                }
            } else {
                //The user chose from the provided types
                //Get the selected item text
                String dropdownSelection = diceTypes.get(diceTypeDropDown.getSelectedItemPosition());
                //Remove the leading d and convert to an int
                dieType = Integer.parseInt(dropdownSelection.substring(1));

            }

            if (dieType > 0 && amount != 0) {
                //Generate the Role Result
                RollResult result = generateRollResult(amount, dieType, isPositiveMod, modifier);

                //Add  it to the Roll History
                rollHistoryList.add(0, result);
                adapter.notifyDataSetChanged();

            } else if (amount == 0) {
                Toast.makeText(getApplicationContext(), R.string.err_zero_die_amount, Toast.LENGTH_SHORT).show();
            } else if (dieType == 0) {
                Toast.makeText(getApplicationContext(), R.string.err_zero_die_type, Toast.LENGTH_SHORT).show();
            } else if (dieType == EMPTY_OTHER) {
                Toast.makeText(getApplicationContext(), R.string.err_empty_die_type, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.err_invalid_die_type, Toast.LENGTH_SHORT).show();
            }
        }

    }

    //Method to clear the results from the Dice Results History RecycleView
    public void clearHistory(View view) {
        rollHistoryList.clear();
        adapter.notifyDataSetChanged();

    }

    //HELPER METHODS
    //Create the values for the dice type dropdown
    private void populateDiceType() {
        diceTypes = new ArrayList<String>();
        diceTypes.add("d4");
        diceTypes.add("d6");
        diceTypes.add("d8");
        diceTypes.add("d10");
        diceTypes.add("d12");
        diceTypes.add("d20");
        diceTypes.add("d100");
        diceTypes.add("d2");
        diceTypes.add(getString(R.string.other_caps));
    }

    //Save the image references for the dice images in the same order as the dice type dropdown
    private void populateImageIDs() {
        diceImgs = new ArrayList<Integer>();
        diceImgs.add(R.drawable.d4_green);
        diceImgs.add(R.drawable.d6_green);
        diceImgs.add(R.drawable.d8_green);
        diceImgs.add(R.drawable.d10_green);
        diceImgs.add(R.drawable.d12_green);
        diceImgs.add(R.drawable.d20_green);
        diceImgs.add(R.drawable.d100_green);
        diceImgs.add(R.drawable.d2_green);
        diceImgs.add(R.drawable.d_other_green);
    }

    //Create the Roll Result
    @NotNull
    private RollResult generateRollResult(int amount, int diceType, boolean isPosMod, int modifier) {
        //Create the RollResult Statement
        String statement = createRollStatement(amount, diceType, isPosMod, modifier);

        //Generate the rolls
        int[] diceRolls = rollDice(amount, diceType);

        //Generate the RollResult Details
        String details = createRollDetails(diceRolls, isPosMod, modifier);

        //Generate the RollResult Result
        int result = createRollResult(diceRolls, isPosMod, modifier);

        //Create the finished Roll Result and return it
        return new RollResult(statement, details, result);
    }

    //Create the statement component of a RollResult
    private String createRollStatement(int amount, int diceType, boolean isPosMod, int modifier) {
        //Create the AMT# d TYPE# Base
        String statement = amount + "d" + diceType;

        //Add the modifier based on whether is it positive or negative
        //Ignore adding it if there is no modifier
        String modifierDesc = "";
        if (modifier != 0 && isPosMod) {
            modifierDesc = " + " + Integer.toString(modifier);
        } else if (modifier != 0) {
            modifierDesc = " - " + Integer.toString(modifier);
        }
        statement += modifierDesc;

        //Return the finished statement
        return statement;
    }

    //Create an int array that contains the values of each dice roll
    private int[] rollDice(int amount, int diceType) {
        //Create empty array
        int[] results = new int[amount];

        //Populate array with random numbers of the dice type
        for (int i = 0; i < amount; i++) {
            results[i] = random.nextInt(diceType) + 1;
        }

        //Return populated array
        return results;
    }

    //Create the details component of a RollResult
    private String createRollDetails(@NotNull int[] diceRolls, boolean isPosMod, int modifier) {
        //Create start of details
        String details = "( ";

        //Add all of the dice rolls to the details String with spacing
        for (int i = 0; i < diceRolls.length; i++) {
            details += diceRolls[i];
            if (i != diceRolls.length - 1) {
                details += ", ";
            } else {
                details += " )";
            }
        }

        //Add the modifier text if there is a modifier
        if (modifier != 0 && isPosMod) {
            details += " + " + modifier;
        } else if (modifier != 0) {
            details += " - " + modifier;
        }

        //Return the finished details String
        return details;
    }

    //Create the result component of a RollResult
    private int createRollResult(@NotNull int[] diceRolls, boolean isPosMod, int modifier) {
        //Create base for the result
        int result = 0;

        //Add all of the dice rolls together
        for (int roll : diceRolls) {
            result += roll;
        }

        //Add/Subtract the modifier
        if (isPosMod) {
            result += modifier;
        } else if (modifier != 0) {
            result -= modifier;
        }

        //Return the final number
        return result;
    }


    //CLEAR EDITTEXT HELPERS
    //On Focus
    public void clearEditViewsOnFocus() {
        amountTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    amountTxt.getText().clear();
                }
            }
        });
        otherTypeTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    otherTypeTxt.getText().clear();
                }
            }
        });
        modifierTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    modifierTxt.getText().clear();
                }
            }
        });
    }

    //On Click
    public void clearEditViewsOnClick() {
        amountTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountTxt.getText().clear();
            }
        });
        otherTypeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherTypeTxt.getText().clear();
            }
        });
        modifierTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifierTxt.getText().clear();
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
                    hideKeyboard(MainActivity.this);
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
