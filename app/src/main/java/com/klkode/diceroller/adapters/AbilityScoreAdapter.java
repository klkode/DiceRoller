package com.klkode.diceroller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klkode.diceroller.model.AbilityScores;
import com.klkode.diceroller.R;

public class AbilityScoreAdapter extends RecyclerView.Adapter<AbilityScoreAdapter.ViewHolder> {

    private Context context;
    private AbilityScores abilityScores;

    //Constructor
    public AbilityScoreAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //Create a ViewHolder for the Ability Score View
        View view = LayoutInflater.from(context).inflate(R.layout.character_abilities_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Check that there is a history
        if(abilityScores != null){
            //Populate the text fields
            //Score 1
            viewHolder.rollDetail1Txt.setText(abilityScores.getDescriptionAt(0).trim());
            viewHolder.rollResult1Txt.setText(Integer.toString(abilityScores.getResultAt(0)).trim());

            //Score 2
            viewHolder.rollDetail2Txt.setText(abilityScores.getDescriptionAt(1).trim());
            viewHolder.rollResult2Txt.setText(Integer.toString(abilityScores.getResultAt(1)).trim());

            //Score 3
            viewHolder.rollDetail3Txt.setText(abilityScores.getDescriptionAt(2).trim());
            viewHolder.rollResult3Txt.setText(Integer.toString(abilityScores.getResultAt(2)).trim());

            //Score 4
            viewHolder.rollDetail4Txt.setText(abilityScores.getDescriptionAt(3).trim());
            viewHolder.rollResult4Txt.setText(Integer.toString(abilityScores.getResultAt(3)).trim());

            //Score 5
            viewHolder.rollDetail5Txt.setText(abilityScores.getDescriptionAt(4).trim());
            viewHolder.rollResult5Txt.setText(Integer.toString(abilityScores.getResultAt(4)).trim());

            //Score 6
            viewHolder.rollDetail6Txt.setText(abilityScores.getDescriptionAt(5).trim());
            viewHolder.rollResult6Txt.setText(Integer.toString(abilityScores.getResultAt(5)).trim());

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(abilityScores == null){
            return 0;
        }else{
            return 1;
        }
    }

    //Set the data for the Ability Scores
    public void setAbilityScores(AbilityScores abilityScores){
        this.abilityScores = abilityScores;
        notifyDataSetChanged();
    }

    //Create the View Holder for the Adapter to use
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rollDetail1Txt;
        TextView rollResult1Txt;
        TextView rollDetail2Txt;
        TextView rollResult2Txt;
        TextView rollDetail3Txt;
        TextView rollResult3Txt;
        TextView rollDetail4Txt;
        TextView rollResult4Txt;
        TextView rollDetail5Txt;
        TextView rollResult5Txt;
        TextView rollDetail6Txt;
        TextView rollResult6Txt;

        public ViewHolder(View itemView) {
            super(itemView);
            rollDetail1Txt = (TextView) itemView.findViewById(R.id.abilityDesc1Txt);
            rollResult1Txt = (TextView) itemView.findViewById(R.id.abilityResult1Txt);
            rollDetail2Txt = (TextView) itemView.findViewById(R.id.abilityDesc2Txt);
            rollResult2Txt = (TextView) itemView.findViewById(R.id.abilityResult2Txt);
            rollDetail3Txt = (TextView) itemView.findViewById(R.id.abilityDesc3Txt);
            rollResult3Txt = (TextView) itemView.findViewById(R.id.abilityResult3Txt);
            rollDetail4Txt = (TextView) itemView.findViewById(R.id.abilityDesc4Txt);
            rollResult4Txt = (TextView) itemView.findViewById(R.id.abilityResult4Txt);
            rollDetail5Txt = (TextView) itemView.findViewById(R.id.abilityDesc5Txt);
            rollResult5Txt = (TextView) itemView.findViewById(R.id.abilityResult5Txt);
            rollDetail6Txt = (TextView) itemView.findViewById(R.id.abilityDesc6Txt);
            rollResult6Txt = (TextView) itemView.findViewById(R.id.abilityResult6Txt);

        }
    }
}