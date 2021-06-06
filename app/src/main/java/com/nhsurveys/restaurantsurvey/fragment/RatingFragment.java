package com.nhsurveys.restaurantsurvey.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.nhsurveys.restaurantsurvey.R;
import com.nhsurveys.restaurantsurvey.activity.MainActivity;
import com.nhsurveys.restaurantsurvey.model.Response;
import com.nhsurveys.restaurantsurvey.utils.AppConfigTags;
import com.nhsurveys.restaurantsurvey.utils.AppConfigURL;
import com.nhsurveys.restaurantsurvey.utils.Constants;
import com.nhsurveys.restaurantsurvey.utils.NetworkConnection;
import com.nhsurveys.restaurantsurvey.utils.UserDetailsPref;
import com.nhsurveys.restaurantsurvey.utils.Utils;

public class RatingFragment extends Fragment {
    RatingBar rbRating;
    EditText etComment;
    TextView tvSubmitSurvey;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;
    ArrayList<Response>responseList = new ArrayList<>();
    String answer = "";
    String answer_ids = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rating, container, false);
        responseList = getArguments().getParcelableArrayList("response_list");
        answer = getArguments().getString(AppConfigTags.ANSWER);
        initView(v);
        initData();
        initListener();
        return v;
    }


    private void initListener() {
        tvSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResponseToServer();
            }
        });

        etComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        for(int i = 0; i < responseList.size(); i++){
            if(i == 0) {
                answer_ids = String.valueOf(responseList.get(i).getResponse_id());
            }else{
                answer_ids = answer_ids + "," + String.valueOf(responseList.get(i).getResponse_id());
            }
        }
    }

    private void initView(View v) {
        rbRating = (RatingBar)v.findViewById(R.id.rbRating);
        etComment = (EditText)v.findViewById(R.id.etComment);
        tvSubmitSurvey = (TextView)v.findViewById(R.id.tvSubmitSurvey);
    }

    private void sendResponseToServer () {
        if (NetworkConnection.isNetworkAvailable(getActivity())) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SUBMIT_RESPONSE, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_SUBMIT_RESPONSE,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);

                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if(!error) {
                                        Utils.showToast(getActivity(), message, true);
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                                    } else {
                                        Utils.showToast(getActivity(), message, true);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showToast(getActivity(), "API ERROR", true);
                                    //Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                //Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showToast(getActivity(), "API ERROR", true);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss();
                            //swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // swipeRefreshLayout.setRefreshing (false);
                            progressDialog.dismiss();
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showToast(getActivity(), "API ERROR", true);
                            //Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {


                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    Utils.showLog(Log.ERROR, "ANSWER_ID", answer_ids, true);
                    Utils.showLog(Log.ERROR, "ANSWER_RATING", answer, true);
                    params.put(AppConfigTags.NAME, userDetailsPref.getStringPref(getActivity(), UserDetailsPref.CUSTOMER_NAME));
                    params.put(AppConfigTags.MOBILE, userDetailsPref.getStringPref(getActivity(), UserDetailsPref.CUSTOMER_MOBILE));
                    params.put(AppConfigTags.ANSWER_IDS, answer_ids);
                    params.put(AppConfigTags.RESPONSE_RATING, answer);
                    params.put(AppConfigTags.RATING, String.valueOf(rbRating.getRating()));
                    params.put(AppConfigTags.COMMENT, etComment.getText().toString());
                    params.put(AppConfigTags.DEVICE_ID, Settings.Secure.getString(getActivity().getContentResolver(),
                            Settings.Secure.ANDROID_ID));
                    params.put(AppConfigTags.DEVICE_NAME, Utils.getDeviceInfo(getActivity()));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref(getActivity(), UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog(Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            /*Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });*/
            Utils.showToast(getActivity(), "API ERROR", true);
        }
    }


}