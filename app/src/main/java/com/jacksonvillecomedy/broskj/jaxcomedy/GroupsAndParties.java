package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by Kyle on 12/29/2014.
 */
public class GroupsAndParties extends Activity {

    TextView tvMain;
    int screenWidth, screenHeight;
    ArrayList<Show> shows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_and_parties);
        System.out.println("groups and parties created");

        declarations();
    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlGroupsAndParties), R.drawable.background);

        shows = new ArrayList<>();
        shows = getIntent().getExtras().getParcelableArrayList("shows");
        tvMain = (TextView) (findViewById(R.id.tvGroupsAndPartiesMain));
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

    public void onReserveSeatsClick(View view) {
        if (!shows.isEmpty())
            startActivity(new Intent(this, com.jacksonvillecomedy.broskj.jaxcomedy.Calendar.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putParcelableArrayListExtra("shows", shows));
        else {
            Toast.makeText(this, "Cannot connect to server, try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    public void onMoreInfoClick(View view) {
        String message = "Hello,\n\nI'd like to request group information for an event.\n\nThank you.";
        startActivity(new Intent(this, Contact.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putExtra("message", message));
        System.out.println("more info clicked from groups and parties");
    }
}