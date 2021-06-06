package com.nhsurveys.restaurantsurvey.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nhsurveys.restaurantsurvey.R;
import com.nhsurveys.restaurantsurvey.fragment.StartSurveyFragment;
import com.nhsurveys.restaurantsurvey.model.Option;
import com.nhsurveys.restaurantsurvey.model.Question;
import com.nhsurveys.restaurantsurvey.utils.AppConfigTags;
import com.nhsurveys.restaurantsurvey.utils.AppConfigURL;
import com.nhsurveys.restaurantsurvey.utils.Constants;
import com.nhsurveys.restaurantsurvey.utils.NetworkConnection;
import com.nhsurveys.restaurantsurvey.utils.UserDetailsPref;
import com.nhsurveys.restaurantsurvey.utils.Utils;

public class MainActivity extends AppCompatActivity {
    CoordinatorLayout clMain;
    TextView tvEnglish;
    TextView tvHindi;
    TextView tvRestaurantName;
    ProgressDialog progressDialog;
    ArrayList<Question> questionList = new ArrayList<>();
    ArrayList<Option> optionList = new ArrayList<>();
    UserDetailsPref userDetailsPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(userDetailsPref.getIntPref(MainActivity.this, UserDetailsPref.USER_ID) == 0){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initData() {
        progressDialog = new ProgressDialog(MainActivity.this);
        userDetailsPref = UserDetailsPref.getInstance();
        switch (userDetailsPref.getStringPref(MainActivity.this, UserDetailsPref.LANGUAGE_TYPE)){
            case "english" :
                tvEnglish.setBackgroundColor(getResources().getColor(R.color.button_green));
                tvEnglish.setTextColor(getResources().getColor(R.color.white));
                tvHindi.setBackgroundColor(getResources().getColor(R.color.white));
                tvHindi.setTextColor(getResources().getColor(R.color.black));
                userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.LANGUAGE_TYPE, Constants.lang_english);
                break;

            case "hindi" :
                tvHindi.setBackgroundColor(getResources().getColor(R.color.button_green));
                tvHindi.setTextColor(getResources().getColor(R.color.white));
                tvEnglish.setBackgroundColor(getResources().getColor(R.color.white));
                tvEnglish.setTextColor(getResources().getColor(R.color.black));
                userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.LANGUAGE_TYPE, Constants.lang_hindi);
        }
        fragmentFunction();
        if(userDetailsPref.getIntPref(MainActivity.this, UserDetailsPref.USER_ID) == 0){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        tvRestaurantName.setText(userDetailsPref.getStringPref(MainActivity.this, UserDetailsPref.USER_RESTAURANT_NAME));
        if(!userDetailsPref.getStringPref(MainActivity.this, UserDetailsPref.LOGIN_CHECK).equalsIgnoreCase("LOGIN")){
            initApplication();
        }
    }

    private void fragmentFunction() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StartSurveyFragment f1 = new StartSurveyFragment();
        fragmentTransaction.add(R.id.fragment_switch, f1, "fragment1");
        fragmentTransaction.commit();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void initListener() {
        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvEnglish.setBackgroundColor(getResources().getColor(R.color.button_green));
                tvEnglish.setTextColor(getResources().getColor(R.color.white));
                tvHindi.setBackgroundColor(getResources().getColor(R.color.white));
                tvHindi.setTextColor(getResources().getColor(R.color.black));
                userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.LANGUAGE_TYPE, Constants.lang_english);
            }
        });

        tvHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHindi.setBackgroundColor(getResources().getColor(R.color.button_green));
                tvHindi.setTextColor(getResources().getColor(R.color.white));
                tvEnglish.setBackgroundColor(getResources().getColor(R.color.white));
                tvEnglish.setTextColor(getResources().getColor(R.color.black));
                userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.LANGUAGE_TYPE, Constants.lang_hindi);
            }
        });
    }

    private void initView() {
        clMain = (CoordinatorLayout)findViewById(R.id.clMain);
        tvEnglish = (TextView)findViewById(R.id.tvEnglish);
        tvHindi = (TextView)findViewById(R.id.tvHindi);
        tvRestaurantName = (TextView)findViewById(R.id.tvRestaurantName);
    }

    private void initApplication() {
        if (NetworkConnection.isNetworkAvailable(MainActivity.this)) {
//            Utils.showProgressDialog(progressDialog, getResources().getString(R.string.progress_dialog_text_please_wait), true);
            //Utils.showLog(Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_INIT, true);
            StringRequest strRequest1 = new StringRequest(Request.Method.GET, AppConfigURL.URL_INIT,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utils.showLog(Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);

                            if (response != null) {
                                userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.RESPONSE, response);
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
                                    String message = jsonObj.getString(AppConfigTags.MESSAGE);
                                    if (!error) {
                                        JSONArray jsonArrayQuestion = jsonObj.getJSONArray(AppConfigTags.QUESTIONS);
                                        if(jsonArrayQuestion.length() > 0){
                                            userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.LOGIN_CHECK, "LOGIN");
                                        }
                                    } else {
                                        Utils.showSnackBar(MainActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    //progressDialog.dismiss();
                                } catch (Exception e) {
                                    //progressDialog.dismiss();
                                    Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                                Utils.showLog(Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            //progressDialog.dismiss();
                            //swipeRefreshLayout.setRefreshing (false);
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // swipeRefreshLayout.setRefreshing (false);
                            //progressDialog.dismiss();
                            Utils.showLog(Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString(), true);
                            Utils.showSnackBar(MainActivity.this, clMain, getResources().getString(R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_dismiss), null);
                        }
                    }) {

                    @Override
                    public Map<String, String> getHeaders () throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                        params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref(MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                        Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                        return params;
                    }
            };
            Utils.sendRequest(strRequest1, 60);
        } else {
            Utils.showSnackBar(this, clMain, getResources().getString(R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources().getString(R.string.snackbar_action_go_to_settings), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
            });
        }
    }

    public void hideLanguage(int type){
        tvEnglish = (TextView)findViewById(R.id.tvEnglish);
        tvHindi = (TextView)findViewById(R.id.tvHindi);
        if (type == 1) {
            tvEnglish.setVisibility(View.GONE);
            tvHindi.setVisibility(View.GONE);
        }else{
            tvEnglish.setVisibility(View.VISIBLE);
            tvHindi.setVisibility(View.VISIBLE);
        }
    }

    public static String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    @Override
    public void onBackPressed () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .content (getResources().getText(R.string.dialog_text_quit_application))
                .positiveColor (getResources ().getColor (R.color.colorAccent))
                .contentColor (getResources ().getColor (R.color.colorAccent))
                .negativeColor (getResources ().getColor (R.color.colorAccent))
                .canceledOnTouchOutside (true)
                .cancelable (true)
                .positiveText (R.string.dialog_action_yes)
                .negativeText (R.string.dialog_action_no)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish ();
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();

        dialog.show ();
    }

}
