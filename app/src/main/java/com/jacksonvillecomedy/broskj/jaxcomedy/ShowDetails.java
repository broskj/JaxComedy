package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyle on 3/22/2015.
 */
public class ShowDetails extends Activity {

    ArrayList<Show> shows;
    int position;
    String videoID;
    Uri videoUri;
    TextView tvHeader, tvHeadliner, tvInfo;

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.this_weekend);
        System.out.println("show details created");

        declarations();
    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlThisWeekend), R.drawable.background);

        shows = getIntent().getExtras().getParcelableArrayList("shows");
        position = getIntent().getExtras().getInt("position");

        getActionBar().setTitle(shows.get(position).getComedian());

        videoID = shows.get(position).getVideoID();
        videoUri = Uri.parse("http://www.youtube.com/watch?v=" + videoID);

        tvHeader = (TextView) findViewById(R.id.tvThisWeekendHeader);
        tvHeader.setText("Headlining on " + shows.get(position).getShowDate() + " at the Comedy Club of Jacksonville:");
        tvHeadliner = (TextView) findViewById(R.id.tvThisWeekendHeadliner);
        tvHeadliner.setText(tvHeadliner.getText().toString() + shows.get(position).getComedian());
        tvInfo = (TextView) findViewById(R.id.tvThisWeekendInfo);
        tvInfo.setText(shows.get(position).getDescription());

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

    public void onImageClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, videoUri));
    }//end onImageClick

    public void onReserveSeatsClick(View view) {
        startActivity(new Intent(this, Reserve.class).putExtra("groupPosition", position).putParcelableArrayListExtra("shows", shows));
    }

}//end class ShowDetails
