package com.nhsurveys.restaurantsurvey.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.nhsurveys.restaurantsurvey.R;
import com.nhsurveys.restaurantsurvey.utils.AppConfigTags;
import com.nhsurveys.restaurantsurvey.utils.AppConfigURL;
import com.nhsurveys.restaurantsurvey.utils.Constants;
import com.nhsurveys.restaurantsurvey.utils.NetworkConnection;
import com.nhsurveys.restaurantsurvey.utils.UserDetailsPref;
import com.nhsurveys.restaurantsurvey.utils.Utils;

public class LoginActivity extends AppCompatActivity{
    EditText etUsername;
    EditText etPassword;
    Button btLogin;
    ProgressDialog progressDialog;
    UserDetailsPref userDetailsPref;

    @Override 
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
    }


    private void initData() {
        progressDialog = new ProgressDialog(this);
        userDetailsPref = UserDetailsPref.getInstance();
    }
    
    private void initListener() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etUsername.getText().toString().length() == 0 && etPassword.getText().toString().length() == 0){
                    Utils.showToast(LoginActivity.this, "Please fill all the fields", true);
                }else{
                    if(etUsername.getText().toString().length() == 0){
                        Utils.showToast(LoginActivity.this, "Please Enter Username", true);
                    }else if(etPassword.getText().toString().length() == 0){
                        Utils.showToast(LoginActivity.this, "Please Enter Password", true);
                    }else{
                        loginRequestToServer();
                    }
                }

            }
        });

        /*etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });*/
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void loginRequestToServer () {
        if (NetworkConnection.isNetworkAvailable(LoginActivity.this)) {
            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_LOGIN, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.POST, AppConfigURL.URL_LOGIN,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);

                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.LANGUAGE_TYPE, Constants.lang_english);
                                        userDetailsPref.putIntPref(LoginActivity.this, UserDetailsPref.USER_ID, jsonObj.getInt(AppConfigTags.USER_ID));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_NAME, jsonObj.getString(AppConfigTags.USER_NAME));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_EMAIL, jsonObj.getString(AppConfigTags.USER_EMAIL));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_MOBILE, jsonObj.getString(AppConfigTags.USER_MOBILE));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_USERNAME, jsonObj.getString(AppConfigTags.USER_USERNAME));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_LOGIN_KEY, jsonObj.getString(AppConfigTags.USER_LOGIN_KEY));
                                        userDetailsPref.putStringPref(LoginActivity.this, UserDetailsPref.USER_RESTAURANT_NAME, jsonObj.getString(AppConfigTags.USER_RESTAURANT_NAME));
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showToast(LoginActivity.this, message, true);
                                        //Utils.showSnackBar(MainActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Utils.showToast(LoginActivity.this, "API ERROR", true);
                                    //Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                //Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showToast(LoginActivity.this, "API ERROR", true);
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
                            Utils.showToast(LoginActivity.this, "API ERROR", true);
                            //Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {


                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put(AppConfigTags.USER_TYPE, "1");
                    params.put(AppConfigTags.USER_NAME, etUsername.getText().toString());
                    params.put(AppConfigTags.USER_PASSWORD, etPassword.getText().toString());
                    params.put(AppConfigTags.DEVICE_ID, Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                            Settings.Secure.ANDROID_ID));
                    params.put(AppConfigTags.DEVICE_NAME, Utils.getDeviceInfo(LoginActivity.this));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(AppConfigTags.HEADER_API_KEY, Constants.api_key);
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
            Utils.showToast(LoginActivity.this, "API ERROR", true);
        }
    }
}
