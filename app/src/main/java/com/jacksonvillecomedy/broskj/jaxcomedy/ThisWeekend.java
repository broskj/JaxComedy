package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kyle on 12/29/2014.
 */
public class ThisWeekend extends Activity {

    int screenWidth, screenHeight;
    Show show;
    String videoID;
    TextView tvHeadliner, tvInfo;

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
        screenWidth = getIntent().getExtras().getInt("screenWidth");
        screenHeight = getIntent().getExtras().getInt("screenHeight");

        show = getIntent().getExtras().getParcelable("show");
        videoID = show.getVideoID();

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
        Toast.makeText(this, "Image was clicked, going to video", Toast.LENGTH_SHORT).show();
        //start youtube intent
    }//end onImageClick
}