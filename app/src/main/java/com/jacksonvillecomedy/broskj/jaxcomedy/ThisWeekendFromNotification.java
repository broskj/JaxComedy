package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kyle on 3/2/2015.
 * <p/>
 * This class acts as a replica of ThisWeekend, except the user navigates to this class instead from
 * the weekly notification.  Additionally, reward points are incremented here.
 */
public class ThisWeekendFromNotification extends Activity {
    int screenWidth, screenHeight;
    ArrayList<Show> shows;
    Show show;
    String videoID;
    Uri videoUri;
    TextView tvHeadliner, tvInfo;
    SharedPreferences spPointValue;
    boolean fromNotification = false;

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.this_weekend);
        System.out.println("this_weekend created");

        declarations();
    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlThisWeekend), R.drawable.background);

        shows = getIntent().getExtras().getParcelableArrayList("shows");

        int nextRegularShowIndex = 0, i = 0;
        while (shows.get(i).getShowTime() != 0) {
            i++;
            nextRegularShowIndex = i;
        }
        show = shows.get(nextRegularShowIndex);

        videoID = show.getVideoID();
        videoUri = Uri.parse("http://www.youtube.com/watch?v=" + videoID);

        tvHeadliner = (TextView) findViewById(R.id.tvThisWeekendHeadliner);
        tvHeadliner.setText(tvHeadliner.getText().toString() + show.getComedian());
        tvInfo = (TextView) findViewById(R.id.tvThisWeekendInfo);
        tvInfo.setText(show.getDescription());

        spPointValue = getSharedPreferences("userPointValue", MODE_MULTI_PROCESS);

        SharedPreferences.Editor editor = spPointValue.edit();
        int currentPoints = spPointValue.getInt("pointValue", 0);
        editor.putInt("pointValue", currentPoints + 2);
        editor.apply();

        if (spPointValue.getInt("pointValue", 0) == currentPoints + 2)
            Toast.makeText(this, "2 Reward Points added!", Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }//end onKeyDown

    public void onImageClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, videoUri));
    }//end onImageClick

    public void onReserveSeatsClick(View view) {
        startActivity(new Intent(this, Reserve.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putExtra("groupPosition", 0).putParcelableArrayListExtra("shows", shows));
    }
}

