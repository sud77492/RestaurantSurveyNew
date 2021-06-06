package com.nhsurveys.restaurantsurvey.utils;

public class AppConfigURL {
    //public static String BASE_URL = "http://restaurantsurvey.000webhostapp.com/api/";
    //public static String BASE_URL = "http://10.0.2.2/restaurant/api/";
    public static String BASE_URL = "https://nhsurveys.000webhostapp.com/nhsurveys/api/";
    //public static String BASE_URL = "http://nhsurveys.com/api/";
    public static String URL_INIT = BASE_URL + "v1/init";
    public static String URL_SUBMIT_RESPONSE = BASE_URL + "v1/submit_response";
    public static String URL_LOGIN = BASE_URL + "v1/login";
    public static String URL_FORGOT_PASSWORD = BASE_URL + "v1/forgotPassword";
    public static String URL_UPDATE_PROFILE = BASE_URL + "v1/update";
    public static String URL_UPDATE_POINTS = BASE_URL + "v1/updatePoints";
    public static String URL_QUERY = BASE_URL + "v1/query";
}