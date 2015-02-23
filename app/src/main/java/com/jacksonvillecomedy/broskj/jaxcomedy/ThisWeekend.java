package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyle on 12/29/2014.
 *
 */
public class ThisWeekend extends Activity {

    int screenWidth, screenHeight;
    ArrayList<Show> shows;
    Show show;
    String videoID;
    Uri videoUri;
    TextView tvHeadliner, tvInfo;
    SharedPreferences spDimensions;
    final static String prefsDimensions = "dimensions";

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.this_weekend);
        System.out.println("this_weekend created");

        declarations();
        scaleImages();
        manageActionBar();
    }//end onCreate

    public void declarations(){
        spDimensions = getSharedPreferences(prefsDimensions, MODE_PRIVATE);
        screenWidth = spDimensions.getInt("screenWidth", -1);
        screenHeight = spDimensions.getInt("screenHeight", -1);

        shows = getIntent().getExtras().getParcelableArrayList("shows");
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getShowTime() == 0) {
                show = shows.get(i);
                break;
            }
        }

        videoID = show.getVideoID();
        videoUri = Uri.parse("http://www.youtube.com/watch?v=" + videoID);

        tvHeadliner = (TextView) findViewById(R.id.tvThisWeekendHeadliner);
        tvHeadliner.setText(tvHeadliner.getText().toString() + show.getComedian());
        tvInfo = (TextView) findViewById(R.id.tvThisWeekendInfo);
        tvInfo.setText(show.getDescription());

    }//end declarations


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        enables home button in actionbar
         */
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        actionBar.setTitle("This Weekend");
    }//end manageActionBar

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleImages() {
        /*
        scales background and building image for performance
        */
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rlThisWeekend);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));

    }//end scaleBackground

    public void onImageClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, videoUri));
    }//end onImageClick

    public void onReserveSeatsClick(View view) {
        startActivity(new Intent(this, Reserve.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putExtra("groupPosition", 0).putParcelableArrayListExtra("shows", shows));
    }
}