package com.blesh.demo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.blesh.sdk.BleshSdk;
import com.blesh.sdk.core.models.SdkConfiguration;

public class DemoApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BleshSdk.configure(
                this,
                new SdkConfiguration.Builder()
                        .testMode(false)
                        .build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
