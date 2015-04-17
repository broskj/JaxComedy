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
import android.widget.Toast;

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
    }

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlSettings), R.drawable.background);

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
                    Toast.makeText(Settings.this, "Notifications enabled!", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("notifications", false);
                    Toast.makeText(Settings.this, "Notifications disabled!", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
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
}