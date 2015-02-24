package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Kyle on 2/5/2015.
 */
public class AddRewardPoints extends Activity {

    final int password = 1137;
    int screenWidth, screenHeight, pointValue;
    SharedPreferences spPointValue;
    EditText etAddPointsPassword, etNumTickets;
    static final String prefsPointValueName = "userPointValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reward_points);
        System.out.println("RedeemOffer created");

        declarations();

    }//end onCreate

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = spPointValue.edit();
        editor.putInt("pointValue", pointValue);
        editor.commit();
    }//end onStop

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlAddRewardPoints), R.drawable.background);

        loadSharedPreferences();
        etAddPointsPassword = (EditText) findViewById(R.id.etAddPointsPassword);
        etNumTickets = (EditText) findViewById(R.id.etNumTickets);
    }//end declarations

    public void loadSharedPreferences() {
        /*
        initializes SharedPreferences object and gets its value; assigns to int pointValue.
            this is just the reward point value, needs to be saved to device and shared
            between activities.
         */
        spPointValue = getSharedPreferences(prefsPointValueName, MODE_PRIVATE);
        pointValue = spPointValue.getInt("pointValue", -1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } //end onOptionsItemSelected

    public void onAddPointsClick(View view) {
        /*
        checks for correct password in the edittext field.
            if correct, adds 5*(num tickets) to point value.  a toast is shown to
              confirm, and the activity finishes.
            if incorrect, resets the password field and gives it focus.  a toast is
              shown to notify the incorrect password, and no points are rewarded.
         */
        if (Integer.parseInt(etAddPointsPassword.getText().toString()) == password) {
            String numTickets = Integer.toString(Integer.parseInt(etNumTickets.getText().toString()) * 5);
            pointValue += Integer.parseInt(numTickets);
            Toast.makeText(this, numTickets + " points successfully added.", Toast.LENGTH_SHORT).show();

            this.finish();
        } else {
            etAddPointsPassword.setText("");
            etAddPointsPassword.hasFocus();
            Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
        }
    }//end onAddPointsClick

}
