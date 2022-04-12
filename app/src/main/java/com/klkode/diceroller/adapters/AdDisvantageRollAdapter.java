package com.klkode.diceroller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klkode.diceroller.model.AdDisvantageRoll;
import com.klkode.diceroller.R;

import java.util.ArrayList;

public class AdDisvantageRollAdapter extends RecyclerView.Adapter<AdDisvantageRollAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AdDisvantageRoll> rollHistory;

    //Constructor
    public AdDisvantageRollAdapter(Context context, ArrayList<AdDisvantageRoll> rollHistory) {
        this.context = context;
        this.rollHistory = rollHistory;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //Create a ViewHolder for the Dice Roll Row
        View view = LayoutInflater.from(context).inflate(R.layout.dice_roll_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Check that there is a history
        if(rollHistory != null && rollHistory.size()!= 0){
            //Get the Roll Result
            AdDisvantageRoll rollsResult = rollHistory.get(position);

            //Populate the text fields
            viewHolder.highRollDetailTxt.setText(rollsResult.getDetailsHigh().trim());
            viewHolder.lowRollDetailTxt.setText(rollsResult.getDetailsLow().trim());
            if(rollsResult.isAdvantageRoll()) {
                viewHolder.rollResultTxt.setText(Integer.toString(rollsResult.getResultHigh()).trim());
                viewHolder.rollResultTxt.setTextColor(ContextCompat.getColor(context, R.color.colorAdvantageTxt));
            }else{
                viewHolder.rollResultTxt.setText(Integer.toString(rollsResult.getResultLow()).trim());
                viewHolder.rollResultTxt.setTextColor(ContextCompat.getColor(context, R.color.colorDisavantageTxt));
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return rollHistory.size();
    }

    //Create the View Holder for the Adapter to use
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView highRollDetailTxt;
        TextView lowRollDetailTxt;
        TextView rollResultTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            highRollDetailTxt = (TextView) itemView.findViewById(R.id.rollDesc1Txt);
            lowRollDetailTxt = (TextView) itemView.findViewById(R.id.rollDesc2Txt);
            rollResultTxt = (TextView) itemView.findViewById(R.id.rollResult1Txt);

        }
    }
}
