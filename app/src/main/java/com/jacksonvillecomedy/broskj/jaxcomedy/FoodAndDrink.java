package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Steve on 12/29/2014.
 */
public class FoodAndDrink extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_and_drink);
        System.out.println("food and drink created");
    }
}