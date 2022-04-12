package com.klkode.diceroller.model;

public class RollResult {

    String statement;
    String details;
    int result;

    //CONSTRUCTORS
    public RollResult() {
        this.statement = "";
        this.details = "";
        this.result = 0;
    }

    public RollResult(String statement, String details, int result) {
        this.statement = statement;
        this.details = details;
        this.result = result;
    }

    //GETTERS AND SETTERS
    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    //METHODS
    public void addToStatement(String statementAdd){
        this.statement += statementAdd;
    }

    public void addToDetails(String detailsAdd){
        this.details += detailsAdd;
    }

    public void addToResult(int addition){
        this.result += addition;
    }

    public boolean isUnpopulated(){
        if(this.statement.isEmpty() && this.details.isEmpty() && this.result == 0){
            return true;
        }else {
            return false;
        }
    }

    public void mergeRolls(RollResult toMerge){
        //Combine Statements
        if(this.statement.isEmpty()){
            this.statement = toMerge.getStatement();
        }else{
            this.statement += " + " + toMerge.getStatement();
        }

        //Combine Details
        if(this.details.isEmpty()){
            this.details = toMerge.getDetails();
        }else{
            this.details += " " + toMerge.getDetails();
        }

        //Combine Results
        this.result += toMerge.getResult();
    }

}
