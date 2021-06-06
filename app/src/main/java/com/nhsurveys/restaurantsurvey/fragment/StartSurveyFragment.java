package com.nhsurveys.restaurantsurvey.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.nhsurveys.restaurantsurvey.R;
import com.nhsurveys.restaurantsurvey.utils.UserDetailsPref;

public class StartSurveyFragment extends Fragment {

    EditText etName;
    EditText etMobile;
    Button btStartSurvey;
    UserDetailsPref userDetailsPref;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start_survey, container,false);
        initView(v);
        initData();
        initListener();
        return v;



    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
    }

    private void initListener() {
        btStartSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDetailsPref.putStringPref(getActivity(), UserDetailsPref.CUSTOMER_NAME, etName.getText().toString());
                userDetailsPref.putStringPref(getActivity(), UserDetailsPref.CUSTOMER_MOBILE, etMobile.getText().toString());
                Fragment myfragment;
                myfragment = new QuestionFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_switch, myfragment);
                fragmentTransaction.commit();
                getActivity().overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        etMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void initView(View v) {
        etName = (EditText)v.findViewById(R.id.etName);
        etMobile = (EditText)v.findViewById(R.id.etMobile);
        btStartSurvey = (Button)v.findViewById(R.id.btStartSurvey);
    }
}
