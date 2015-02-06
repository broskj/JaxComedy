package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Steve on 12/29/2014.
 */
public class ThisWeekend extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.this_weekend);
        System.out.println("this weekend created");
    }
}