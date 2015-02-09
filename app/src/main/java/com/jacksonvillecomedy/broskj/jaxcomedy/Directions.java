package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by Kyle on 12/29/2014.
 *
 * NO LONGER BEING USED, REPLACED WITH MAP INTENT.
 */
public class Directions extends Activity {

    int screenWidth, screenHeight;
    final String addressURL = "https://www.google.com/maps/place/11000+Beach+Blvd,+Jacksonville,+FL+32246/";

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);
        System.out.println("directions created");

        final int screenWidth = getIntent().getExtras().getInt("screenWidth");
        final int screenHeight = getIntent().getExtras().getInt("screenHeight");

        manageActionBar();
        scaleBackground(screenWidth, screenHeight);

        /*
        creates a webview object and parses a URL string.  Enables GPS location for directions.
         */
        WebView webview = (WebView) findViewById(R.id.webview_directions);
        try {
            webview.setWebViewClient(new GeoWebViewClient());
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setGeolocationEnabled(true);
            webview.setWebChromeClient(new GeoWebChromeClient());
            webview.loadUrl(addressURL);
        } catch (Exception e) {
            System.out.println("Exception called during webview process");
            e.printStackTrace();
        }

    }//end onCreate

    @Override
    protected void onPause() {
        super.onPause();
        System.exit(0);
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

    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // When user clicks a hyperlink, load in the existing WebView
            view.loadUrl(url);
            return true;
        }
    }//end GeoWebViewClient

    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }//end GeoWebChromeClient

    /*
    creates ActionBar object, enables the home button, and resets title to ''
    */
    public void manageActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Directions");
    }//end manageActionBar

    /*
    scales background for performance
    */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleBackground(int screenWidth, int screenHeight) {
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rlDirections);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));
    }//end scaleBackground
}