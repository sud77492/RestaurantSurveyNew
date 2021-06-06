package com.nhsurveys.restaurantsurvey.model;

import java.util.ArrayList;

public class Question {
    int ques_id;
    String ques_english, ques_hindi;
    ArrayList<Option>optionList = new ArrayList<>();


    public Question(int ques_id, String ques_english, String ques_hindi, ArrayList<Option>optionList) {
        this.ques_id = ques_id;
        this.ques_english = ques_english;
        this.ques_hindi = ques_hindi;
        this.optionList = optionList;

    }

    public Question() {

    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public String getQues_english() {
        return ques_english;
    }

    public void setQues_english(String ques_english) {
        this.ques_english = ques_english;
    }

    public String getQues_hindi() {
        return ques_hindi;
    }

    public void setQues_hindi(String ques_hindi) {
        this.ques_hindi = ques_hindi;
    }

    public ArrayList<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(ArrayList<Option> optionList) {
        this.optionList = optionList;
    }

    public void addQuestionOption (Option option) {
        this.optionList.add(option);
    }

    public ArrayList<Option> getQuestionOptionList () {
        return optionList;
    }

}
