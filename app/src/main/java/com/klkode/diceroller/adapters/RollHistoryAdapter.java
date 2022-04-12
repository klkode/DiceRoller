package com.klkode.diceroller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klkode.diceroller.R;
import com.klkode.diceroller.model.RollResult;

import java.util.ArrayList;

public class RollHistoryAdapter extends RecyclerView.Adapter<RollHistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RollResult> rollHistory;

    //Constructor
    public RollHistoryAdapter(Context context, ArrayList<RollResult> rollHistory) {
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
            RollResult rollResult = rollHistory.get(position);

            //Populate the text fields
            viewHolder.rollStatementTxt.setText(rollResult.getStatement().trim());
            viewHolder.rollDetailsTxt.setText(rollResult.getDetails().trim());
            viewHolder.rollResultTxt.setText(Integer.toString(rollResult.getResult()).trim());
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
        TextView rollStatementTxt;
        TextView rollDetailsTxt;
        TextView rollResultTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            rollStatementTxt = (TextView) itemView.findViewById(R.id.rollDesc1Txt);
            rollDetailsTxt = (TextView) itemView.findViewById(R.id.rollDesc2Txt);
            rollResultTxt = (TextView) itemView.findViewById(R.id.rollResult1Txt);

        }
    }
}
