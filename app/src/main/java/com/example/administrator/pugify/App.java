package com.example.administrator.pugify;

import android.app.Application;
import android.os.Bundle;

import com.parse.Parse;
/**
 * Created by Administrator on 01/12/2015.
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "8dnDctuu132tyDnx8arN8cMSg84srHv1UAl8edge", "oU4kFsHPhFuA7tN8NNricnt0YlwxtjRLpufZrS6O");
    }
}
