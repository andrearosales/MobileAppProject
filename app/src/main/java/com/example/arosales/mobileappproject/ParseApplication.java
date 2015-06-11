package com.example.arosales.mobileappproject;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by POLI on 5/11/2015.
 */
public class ParseApplication extends Application
{
    public static final String APPLICATION_ID = "qT7ozC6SpBUGiaKxHQwZHRyNfT0GX2xECCVsJYyv";
    public static final String CLIENT_KEY = "jLWrQSMm4uC97tkzwPZLeJ3000GQS8cDm65uXQnc";

    @Override
    public void onCreate() {
        super.onCreate();

        // initialization
        Parse.enableLocalDatastore(this);
        Parse.initialize(getApplicationContext(), APPLICATION_ID, CLIENT_KEY);

    }
}