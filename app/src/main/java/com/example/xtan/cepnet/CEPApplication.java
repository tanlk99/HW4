package com.example.xtan.cepnet;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by xtan on 5/16/2015.
 */

public class CEPApplication extends Application {
    @Override
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "EbFVRZjrszX6BbfFrInhSIewwfpd8VJl6NpJ8HK7", "4PKOktJgGoiaR67h8AIH19uFQbzv745oGSm1qQwr");
    }
}
