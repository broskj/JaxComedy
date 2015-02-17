package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Kyle on 12/29/2014.
 */

public class MainActivity extends Activity {

    SharedPreferences firstCheck;
    final int initialPointValue = 15;
    int screenWidth, screenHeight;
    static final String prefsPointValueName = "userPointValue",
            directionsURI = "geo:0,0?q=11000+beach+blvd+jacksonville+fl+32246",
            facebookURL = "https://www.facebook.com/ComedyClubOfJacksonville",
            twitterURL = "https://twitter.com/comedyclubofjax",
            showSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1Ax2-gUY33i_pRHZIwR8AULy6-nbnAbM8Qm5-CGISevc/gviz/tq",
            dealsSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1dnpODnbz6ME4RY5vNwrtAc6as3-uj2rK_IgtYszsvsM/gviz/tq",
            showsFilename = "shows.txt",
            dealsFilename = "deals.txt";
    Intent browserIntent;
    MyAdapter adapter;
    ListView listView;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    ArrayList<Show> shows;
    ArrayList<Offer> offers;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate called");

        declarations();
        manageActionBar();
        scaleImages();
        setListViewAdapter();
        checkFirstRun();

        /*
        place in AlarmManager method
         */
        downloadShowsAndDeals();
    }//end onCreate

    private void downloadShowsAndDeals() {
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                getShows();
                getDeals();
            } else {
                System.out.println("Network info not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void declarations() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        firstCheck = PreferenceManager.getDefaultSharedPreferences(this);
        shows = new ArrayList<>();
        offers = new ArrayList<>();

        /*
        creates the ListView adapter and populates the ListView using generateData()
         */
        adapter = new MyAdapter(this, generateData());
        listView = (ListView) findViewById(R.id.listview);

        /*
        creates a connectivitymanager and a networkinfo object.
         */
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

    }//end declarations

    public void setListViewAdapter() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                System.out.println("position is " + position);
                switch (position) {
                    case 0://this weekend
                        startActivity(new Intent(MainActivity.this, ThisWeekend.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putExtra("show", shows.get(0)));
                        System.out.println("this weekend clicked");
                        break;
                    case 1://calendar
                        startActivity(new Intent(MainActivity.this, Calendar.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putParcelableArrayListExtra("shows", shows));
                        System.out.println("calendar clicked");
                        break;
                    case 2://deals
                        startActivity(new Intent(MainActivity.this, Deals.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putParcelableArrayListExtra("offers", offers));
                        System.out.println("deals clicked");
                        break;
                    case 3://food and drink
                        startActivity(new Intent(MainActivity.this, FoodAndDrink.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                        System.out.println("food and drink clicked");
                        break;
                    case 4://groups and parties
                        startActivity(new Intent(MainActivity.this, GroupsAndParties.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putParcelableArrayListExtra("shows", shows));
                        System.out.println("groups and parties clicked");
                        break;
                    default:
                        System.out.println("Something went wrong");
                }
            }
        });
    }//end setListViewAdapter

    public void checkFirstRun() {
        /*
        first run check. If first run, sets reward point value to 15.
         */
        if (!firstCheck.getBoolean("firstRun", false)) {
            System.out.println("first run statement entered");
            SharedPreferences spPointValue = getSharedPreferences(prefsPointValueName, MODE_PRIVATE);
            SharedPreferences.Editor editor = spPointValue.edit();
            editor.putInt("pointValue", initialPointValue);
            editor.commit();

            downloadShowsAndDeals();

            editor = firstCheck.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
        }
    }//end checkFirstRun

    /*
    populates the ListView on activity_main.xml with an icon and title.
     */
    private ArrayList<Model> generateData() {
        ArrayList<Model> models = new ArrayList<>();
        models.add(new Model(R.drawable.ic_action_event, "This Weekend"));
        models.add(new Model(R.drawable.ic_action_go_to_today, "Upcoming Shows"));
        models.add(new Model(R.drawable.ic_action_important_gold, "Rewards and Offers"));
        models.add(new Model(R.drawable.ic_action_view_as_list, "Food and Drink"));
        models.add(new Model(R.drawable.ic_action_group, "Groups and Parties"));

        return models;
    }

    /*
    enables overflow menu icon, even with presence of hardware menu key
     */
    public void addMenuButtonFunctionality() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            // presumably, not relevant
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        System.out.println("onCreateOptionsMenu called");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        System.out.println("onOptionsItemSelected called");

        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        final int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, Settings.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                System.out.println("settings clicked");
                return true;
            case R.id.action_directions:
                Intent directionsIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(directionsURI));
                if (directionsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(directionsIntent);
                }
                //startActivity(new Intent(MainActivity.this, Directions.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                System.out.println("directions clicked");
                return true;
            case R.id.action_about_us:
                startActivity(new Intent(MainActivity.this, AboutUs.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                System.out.println("about us clicked");
                return true;
            case R.id.action_contact:
                startActivity(new Intent(MainActivity.this, Contact.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                System.out.println("contact clicked");
                return true;
            case R.id.action_now_hiring:
                startActivity(new Intent(MainActivity.this, NowHiring.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                System.out.println("now hiring clicked");
                return true;
            case R.id.action_report_a_bug:
                startActivity(new Intent(MainActivity.this, ReportABug.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                System.out.println("report a bug clicked");
            default:
                return super.onOptionsItemSelected(item);
        }
    }//end onOptionsItemSelected

    /*
    creates ActionBar object and resets title to ''
    adds menu functionality
    */
    public void manageActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Jax Comedy");
        addMenuButtonFunctionality();
    }//end manageActionBar

    /*
    scales images for performance
    */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleImages() {
        //background
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearLayoutMain);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));

        //comedy club logo
        ImageView myImage = (ImageView) findViewById(R.id.ivLogo);
        Bitmap bitmapIVLogo = BitmapFactory.decodeResource(getResources(), R.drawable.comedyclublogo2);
        Bitmap resizedBitmapIVLogo = Bitmap.createScaledBitmap(bitmapIVLogo, screenWidth, screenHeight / 2, true);
        myImage.setImageBitmap(resizedBitmapIVLogo);
    }//end scaleBackground

    public void onFacebookClick(View view) {
    /*
    handles the click method for the facebook button.  opens an internet intent for the
      comedy club facebook.
     */
        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURL));
        startActivity(browserIntent);
    }//onFacebookClick

    public void onTwitterClick(View view) {
    /*
    handles the click method for the twitter button.  opens an internet intent for the
      comedy club twitter.
     */
        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterURL));
        startActivity(browserIntent);
    }//end onTwitterClick

    public void getShows() {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                processShowsJson(object);
            }
        }).execute(showSpreadsheetURL);
    }//end getShows


    public void getDeals() {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                processDealsJson(object);
            }
        }).execute(dealsSpreadsheetURL);
    }

    private void processShowsJson(JSONObject object) {
        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int i = 0; i < rows.length(); ++i) {
                JSONObject row = rows.getJSONObject(i);
                JSONArray columns = row.getJSONArray("c");

                String showDate = columns.getJSONObject(0).getString("v");
                int showTime = columns.getJSONObject(1).getInt("v");
                String comedian = columns.getJSONObject(2).getString("v");
                String description = columns.getJSONObject(3).getString("v");
                String videoURL = columns.getJSONObject(4).getString("v");

                Show show = new Show(comedian, description, showDate, showTime, videoURL);
                shows.add(show);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//end processShowsJson

    private void processDealsJson(JSONObject object) {
        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int i = 0; i < rows.length(); ++i) {
                JSONObject row = rows.getJSONObject(i);
                JSONArray columns = row.getJSONArray("c");

                String offerTitle = columns.getJSONObject(0).getString("v");
                String offerDescription = columns.getJSONObject(1).getString("v");
                int pointValue = columns.getJSONObject(2).getInt("v");

                Offer offer = new Offer(pointValue, offerTitle, offerDescription);
                offers.add(offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }//end processShowsJson

}