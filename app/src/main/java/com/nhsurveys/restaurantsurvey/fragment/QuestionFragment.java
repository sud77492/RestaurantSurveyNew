package com.nhsurveys.restaurantsurvey.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.nhsurveys.restaurantsurvey.R;
import com.nhsurveys.restaurantsurvey.activity.MainActivity;
import com.nhsurveys.restaurantsurvey.model.Option;
import com.nhsurveys.restaurantsurvey.model.Question;
import com.nhsurveys.restaurantsurvey.model.Response;
import com.nhsurveys.restaurantsurvey.utils.AppConfigTags;
import com.nhsurveys.restaurantsurvey.utils.UserDetailsPref;
import com.nhsurveys.restaurantsurvey.utils.Utils;

public class QuestionFragment extends Fragment {
    LinearLayout ll1;
    LinearLayout ll2;
    TextView tvQuestion;
    TextView tvll1One;
    TextView tvll1Two;
    TextView tvll1Three;
    TextView tvll1Four;
    TextView tvll1Five;
    TextView tvll2One;
    TextView tvll2Two;
    TextView tvll2Three;
    ArrayList<Question> questionList = new ArrayList<>();
    ArrayList<Option> optionList = new ArrayList<>();
    ArrayList<Response> responseList = new ArrayList<>();
    UserDetailsPref userDetailsPref;
    String response = "";
    int index = 0;
    String answer = "";

    boolean clicked = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_question, container,false);
        initView(v);
        initData();
        initListener();
        return v;

    }

    private void initView(View v) {
        ll1 = (LinearLayout)v.findViewById(R.id.ll1);
        ll2 = (LinearLayout)v.findViewById(R.id.ll2);
        tvQuestion = (TextView)v.findViewById(R.id.tvQuestion);
        tvll1One = (TextView)v.findViewById(R.id.tvll1One);
        tvll1Two = (TextView)v.findViewById(R.id.tvll1Two);
        tvll1Three = (TextView)v.findViewById(R.id.tvll1Three);
        tvll1Four = (TextView)v.findViewById(R.id.tvll1Four);
        tvll1Five = (TextView)v.findViewById(R.id.tvll1Five);
        tvll2One = (TextView)v.findViewById(R.id.tvll2One);
        tvll2Two = (TextView)v.findViewById(R.id.tvll2Two);
        tvll2Three = (TextView)v.findViewById(R.id.tvll2Three);
    }

    private void initData() {
        ((MainActivity) getActivity()).hideLanguage(1);
        userDetailsPref = UserDetailsPref.getInstance();
        response = userDetailsPref.getStringPref(getActivity(), UserDetailsPref.RESPONSE);
        questionList.clear();
        try {
            JSONObject jsonObj = new JSONObject(response);
            boolean error = jsonObj.getBoolean(AppConfigTags.ERROR);
            String message = jsonObj.getString(AppConfigTags.MESSAGE);
            if (!error) {
                JSONArray jsonArrayQuestion = jsonObj.getJSONArray(AppConfigTags.QUESTIONS);
                for (int i = 0; i < jsonArrayQuestion.length(); i++) {
                    JSONObject jsonObjQuestion = jsonArrayQuestion.getJSONObject(i);
                    Question question = new Question();
                    question.setQues_id(jsonObjQuestion.getInt(AppConfigTags.QUESTION_ID));
                    question.setQues_english(jsonObjQuestion.getString(AppConfigTags.QUESTION_ENGLISH));
                    question.setQues_hindi(jsonObjQuestion.getString(AppConfigTags.QUESTION_HINDI));
                    //question.setQues_hindi(new String(jsonObjQuestion.getString(AppConfigTags.QUESTION_HINDI).getBytes("ISO-8859-1"), "UTF-8"));
                    optionList.clear();
                    JSONArray jsonArrayOption = jsonObjQuestion.getJSONArray(AppConfigTags.OPTIONS);
                    for(int j=0; j < jsonArrayOption.length(); j++){
                        JSONObject jsonObjectOption = jsonArrayOption.getJSONObject(j);
                        Option option = new Option();
                        /*optionList.add(new Option(jsonObjectOption.getInt(AppConfigTags.OPTION_ID),
                                jsonObjectOption.getString(AppConfigTags.OPTION_ENGLISH),
                                jsonObjectOption.getString(AppConfigTags.OPTION_HINDI)
                        ));*/
                        option.setOpt_id(jsonObjectOption.getInt(AppConfigTags.OPTION_ID));
                        option.setOpt_english(jsonObjectOption.getString(AppConfigTags.OPTION_ENGLISH));
                        option.setOpt_hindi(jsonObjectOption.getString(AppConfigTags.OPTION_HINDI));
                        question.addQuestionOption(option);
                        //Utils.showLog(Log.ERROR, "Option2", jsonObjectOption.getString(AppConfigTags.OPTION_ENGLISH), true);
                    }

                    //question.setOptionList(optionList);
                    questionList.add(question);
                }
                for(int k = 0; k < questionList.size(); k++){
                    Utils.showLog(Log.ERROR, "Question", questionList.get(k).getQues_english(), true);
                    for(int l = 0; l < questionList.get(k).getOptionList().size(); l++){
                        Utils.showLog(Log.ERROR, "option", questionList.get(k).getQuestionOptionList().get(l).getOpt_english(), true);
                    }
                }
                questionChange(0);
            } else {
                Utils.showToast(getActivity(), message, true);
            }
        } catch (Exception e) {
            Utils.showToast(getActivity(), "api error", true);
            e.printStackTrace();
        }
    }

    private void initListener() {
        tvll1One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 5, questionList.get(index).getQuestionOptionList().get(0).getOpt_id()));
                        sendToRatingActivity(5);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 5, questionList.get(index - 1).getQuestionOptionList().get(0).getOpt_id()));
                        questionChange(5);
                    }
                }
            }
        });

        tvll1Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 4, questionList.get(index).getQuestionOptionList().get(1).getOpt_id()));
                        sendToRatingActivity(4);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 4, questionList.get(index - 1).getQuestionOptionList().get(1).getOpt_id()));
                        questionChange(4);
                    }
                }
            }
        });

        tvll1Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 3, questionList.get(index).getQuestionOptionList().get(2).getOpt_id()));
                        sendToRatingActivity(3);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 3, questionList.get(index - 1).getQuestionOptionList().get(2).getOpt_id()));
                        questionChange(3);
                    }
                }
            }
        });

        tvll1Four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 2, questionList.get(index).getQuestionOptionList().get(3).getOpt_id()));
                        sendToRatingActivity(2);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 2, questionList.get(index - 1).getQuestionOptionList().get(3).getOpt_id()));
                        questionChange(2);
                    }
                }
            }
        });

        tvll1Five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 1, questionList.get(index).getQuestionOptionList().get(4).getOpt_id()));
                        sendToRatingActivity(5);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 1, questionList.get(index - 1).getQuestionOptionList().get(4).getOpt_id()));
                        questionChange(5);
                    }
                }
            }
        });

        tvll2One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 5, questionList.get(index).getQuestionOptionList().get(0).getOpt_id()));
                        sendToRatingActivity(5);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 5, questionList.get(index - 1).getQuestionOptionList().get(0).getOpt_id()));
                        questionChange(5);
                    }
                }
            }
        });

        tvll2Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 3, questionList.get(index).getQuestionOptionList().get(1).getOpt_id()));
                        sendToRatingActivity(3);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 3, questionList.get(index - 1).getQuestionOptionList().get(1).getOpt_id()));
                        questionChange(3);
                    }
                }

            }
        });

        tvll2Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked) {
                    clicked = true;
                    if (questionList.size() == index + 1) {
                        responseList.add(new Response(index, 1, questionList.get(index).getQuestionOptionList().get(2).getOpt_id()));
                        sendToRatingActivity(1);
                    } else {
                        index = index + 1;
                        responseList.add(new Response(index, 1, questionList.get(index - 1).getQuestionOptionList().get(2).getOpt_id()));
                        questionChange(1);
                    }
                }
            }
        });

    }

    private void sendToRatingActivity(int selected_response) {
        answer = answer + "," + String.valueOf(selected_response);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("response_list", responseList);
        bundle.putString(AppConfigTags.ANSWER, answer);
        RatingFragment ratingFragment = new RatingFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        ratingFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_switch, ratingFragment);
        fragmentTransaction.commit();
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void questionChange(int selected_response){
        Utils.showLog(Log.ERROR, "Index",""+ index, true);
        if(index == 1){
            answer = String.valueOf(selected_response);
        }else{
            answer = answer + "," + String.valueOf(selected_response);
        }
        clicked = false;
        Utils.showLog(Log.ERROR, "selected_response", ""+questionList.get(selected_response).getOptionList().size(), true);
        switch (questionList.get(index).getOptionList().size()){
            case 3 :
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                Utils.showLog(Log.ERROR, "option", questionList.get(index).getQuestionOptionList().get(0).getOpt_english(), true);
                switch (userDetailsPref.getStringPref(getActivity(), UserDetailsPref.LANGUAGE_TYPE)){
                    case "english":
                        tvll2One.setText(questionList.get(index).getQuestionOptionList().get(0).getOpt_english());
                        tvll2Two.setText(questionList.get(index).getQuestionOptionList().get(1).getOpt_english());
                        tvll2Three.setText(questionList.get(index).getQuestionOptionList().get(2).getOpt_english());
                        tvQuestion.setText(questionList.get(index).getQues_english());
                        Utils.showLog(Log.ERROR, "3-English", questionList.get(index).getQues_english(), true);
                        break;

                    case "hindi":
                        tvll2One.setText(questionList.get(index).getQuestionOptionList().get(0).getOpt_hindi());
                        tvll2Two.setText(questionList.get(index).getQuestionOptionList().get(1).getOpt_hindi());
                        tvll2Three.setText(questionList.get(index).getQuestionOptionList().get(2).getOpt_hindi());
                        tvQuestion.setText(questionList.get(index).getQues_hindi());
                        Utils.showLog(Log.ERROR, "3-Hindi", questionList.get(index).getQues_hindi(), true);
                        break;
                }
                break;

            case 5 :
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);
                switch (userDetailsPref.getStringPref(getActivity(), UserDetailsPref.LANGUAGE_TYPE)){
                    case "english":
                        tvll1One.setText(questionList.get(index).getQuestionOptionList().get(0).getOpt_english());
                        tvll1Two.setText(questionList.get(index).getQuestionOptionList().get(1).getOpt_english());
                        tvll1Three.setText(questionList.get(index).getQuestionOptionList().get(2).getOpt_english());
                        tvll1Four.setText(questionList.get(index).getQuestionOptionList().get(3).getOpt_english());
                        tvll1Five.setText(questionList.get(index).getQuestionOptionList().get(4).getOpt_english());
                        tvQuestion.setText(questionList.get(index).getQues_english());
                        Utils.showLog(Log.ERROR, "5-English", questionList.get(index).getQues_english(), true);
                        break;

                    case "hindi":
                        tvll1One.setText(questionList.get(index).getQuestionOptionList().get(0).getOpt_hindi());
                        tvll1Two.setText(questionList.get(index).getQuestionOptionList().get(1).getOpt_hindi());
                        tvll1Three.setText(questionList.get(index).getQuestionOptionList().get(2).getOpt_hindi());
                        tvll1Four.setText(questionList.get(index).getQuestionOptionList().get(3).getOpt_hindi());
                        tvll1Five.setText(questionList.get(index).getQuestionOptionList().get(4).getOpt_hindi());
                        tvQuestion.setText(questionList.get(index).getQues_hindi());
                        Utils.showLog(Log.ERROR, "5-hindi", questionList.get(index).getQues_hindi(), true);
                        break;
                }
                break;
        }

    }
}
