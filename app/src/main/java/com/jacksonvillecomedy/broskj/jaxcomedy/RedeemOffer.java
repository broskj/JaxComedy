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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Steve on 2/4/2015.
 */
public class RedeemOffer extends Activity {

    final int password = 1137;
    int screenWidth, screenHeight, pointValue;
    TextView tvRedeemInfo;
    EditText etRedeemPassword;
    SharedPreferences spPointValue;
    String redeemInfo;
    static final String prefsPointValueName = "userPointValue";
    Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_offer);
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
        etRedeemPassword = (EditText) findViewById(R.id.etRedeemPassword);
        spPointValue = getSharedPreferences(prefsPointValueName, MODE_PRIVATE);
        pointValue = spPointValue.getInt("pointValue", -1);
        /*
        gets the offer details from Deals.java
         */
        offer = new Offer();
        offer.setPointValue(getIntent().getExtras().getInt("pointValue"));
        offer.setOfferTitle(getIntent().getExtras().getString("offerTitle"));
        redeemInfo = "Confirm offer for \'" + offer.getOfferTitle() + "\' for " + offer.getPointValue() +
                " points.  Present your device to either the host or your server for them to confirm.\n";
        tvRedeemInfo = (TextView) findViewById(R.id.tvRedeemInfo);
        tvRedeemInfo.setText(redeemInfo);

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

    /*
    creates ActionBar object, enables the home button, and resets title
    */
    public void manageActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Redeem Offer");
    }//end manageActionBar

    /*
    scales background for performance
    */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleBackground(int screenWidth, int screenHeight) {
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rlRedeemOffer);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));
    }//end scaleBackground

    public void onRedeemClick(View view) {
        if(Integer.parseInt(etRedeemPassword.getText().toString()) == password){
            pointValue -= offer.getPointValue();
            Toast.makeText(this, "Offer redeemed!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
            etRedeemPassword.setText("");
            etRedeemPassword.hasFocus();
            Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
        }
    }//end onRedeemClick

}
