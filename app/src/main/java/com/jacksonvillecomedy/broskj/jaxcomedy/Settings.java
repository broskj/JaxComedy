package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Kyle on 12/29/2014.
 */
public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        System.out.println("settings created");

        final int screenWidth = getIntent().getExtras().getInt("screenWidth");
        final int screenHeight = getIntent().getExtras().getInt("screenHeight");

    }
}