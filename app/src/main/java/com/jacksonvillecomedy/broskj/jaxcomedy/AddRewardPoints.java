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
 * Created by Steve on 2/5/2015.
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
        manageActionBar();
        scaleBackground(screenWidth, screenHeight);

    }//end onCreate

    @Override
    public void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = spPointValue.edit();
        editor.putInt("pointValue", pointValue);
        editor.commit();
    }//end onStop

    public void declarations(){
        screenWidth = getIntent().getExtras().getInt("screenWidth");
        screenHeight = getIntent().getExtras().getInt("screenHeight");
        spPointValue = getSharedPreferences(prefsPointValueName, MODE_PRIVATE);
        pointValue = spPointValue.getInt("pointValue", -1);
        etAddPointsPassword = (EditText) findViewById(R.id.etAddPointsPassword);
        etNumTickets = (EditText) findViewById(R.id.etNumTickets);

    }//end declarations

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

    /*
    creates ActionBar object, enables the home button, and resets title
    */
    public void manageActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Reward Points");
    }//end manageActionBar

    /*
    scales background for performance
    */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleBackground(int screenWidth, int screenHeight) {
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rlAddRewardPoints);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));
    }//end scaleBackground

    public void onAddPointsClick(View view) {

        if(Integer.parseInt(etAddPointsPassword.getText().toString()) == password){
            String numTickets = Integer.toString(Integer.parseInt(etNumTickets.getText().toString())*5);
            pointValue += Integer.parseInt(numTickets);
            Toast.makeText(this, numTickets + " points successfully added.", Toast.LENGTH_SHORT).show();

            this.finish();
        }
        else{
            etAddPointsPassword.setText("");
            etAddPointsPassword.hasFocus();
            Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
        }
    }//end onAddPointsClick

}
