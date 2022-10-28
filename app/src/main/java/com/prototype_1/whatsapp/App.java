package com.prototype_1.whatsapp;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)    // to initialize the Parse before registering into the database that enables accessibility to Parse Class
                .applicationId(getString(R.string.back4app_app_ID))
                .server(getString(R.string.back4app_server_url))
                .clientKey(getString(R.string.back4app_client_key))
                .build());
    }
}
