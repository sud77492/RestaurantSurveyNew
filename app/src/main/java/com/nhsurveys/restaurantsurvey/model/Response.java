package com.nhsurveys.restaurantsurvey.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class Response implements Parcelable{
    int question_id, response, response_id;

    public Response(int question_id, int response, int response_id) {
        this.question_id = question_id;
        this.response = response;
        this.response_id = response_id;
    }

    public int getResponse_id() {
        return response_id;
    }

    public void setResponse_id(int response_id) {
        this.response_id = response_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(question_id);
        parcel.writeInt(response);
    }
}
