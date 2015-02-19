package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

/**
 * Created by Kyle on 12/29/2014.
 */
public class Settings extends Activity {

    int screenWidth, screenHeight;
    boolean notificationsIsChecked;
    Switch swSettings;
    SharedPreferences spNotifications;
    SharedPreferences.Editor editor;
    static final String prefsNotificationToggle = "notificationToggle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        System.out.println("settings created");

        declarations();
        manageActionBar();
        scaleImages();
    }

    public void declarations() {
        screenWidth = getIntent().getExtras().getInt("screenWidth");
        screenHeight = getIntent().getExtras().getInt("screenHeight");

        swSettings = (Switch) findViewById(R.id.swSettings);

        spNotifications = getSharedPreferences(prefsNotificationToggle, MODE_PRIVATE);
        editor = spNotifications.edit();

        notificationsIsChecked = spNotifications.getBoolean("notifications", false);
        swSettings.setChecked(notificationsIsChecked);

        swSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("notifications", true);
                } else {
                    editor.putBoolean("notifications", false);
                }
                editor.commit();
            }
        });

    }//end declarations

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        enables home button in actionbar
         */
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } //end onOptionsItemSelected

    public void manageActionBar() {
        /*
        creates ActionBar object, enables the home button, and resets title to ''
        */
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");
    }//end manageActionBar

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleImages() {
        /*
        scales background and building image for performance
        */
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rlSettings);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));

    }//end scaleBackground
}