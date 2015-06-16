package com.example.arosales.mobileappproject;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

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

        /*
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });*/

    }
}