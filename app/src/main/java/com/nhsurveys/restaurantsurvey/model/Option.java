package com.nhsurveys.restaurantsurvey.model;

public class Option {
    int opt_id;
    String opt_english, opt_hindi;

    public Option(int opt_id, String opt_english, String opt_hindi) {
        this.opt_id = opt_id;
        this.opt_english = opt_english;
        this.opt_hindi = opt_hindi;
    }

    public Option() {

    }

    public int getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(int opt_id) {
        this.opt_id = opt_id;
    }

    public String getOpt_english() {
        return opt_english;
    }

    public void setOpt_english(String opt_english) {
        this.opt_english = opt_english;
    }

    public String getOpt_hindi() {
        return opt_hindi;
    }

    public void setOpt_hindi(String opt_hindi) {
        this.opt_hindi = opt_hindi;
    }
}
