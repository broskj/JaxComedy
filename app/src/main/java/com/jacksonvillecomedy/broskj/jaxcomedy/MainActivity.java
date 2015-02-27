package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kyle on 12/29/2014.
 */

public class MainActivity extends Activity {

    SharedPreferences firstCheck,
            spPointValue,
            spNotifications,
            spSpreadsheets;
    SharedPreferences.Editor editor;
    final int initialPointValue = 15;
    int screenWidth, screenHeight;
    static final String prefsPointValueName = "userPointValue",
            prefsNotificationToggle = "notificationToggle",
            prefsSpreadsheets = "spreadsheets",
            directionsURI = "geo:0,0?q=11000+Beach+Blvd+Jacksonville+Fl+32246",
            facebookURL = "https://www.facebook.com/ComedyClubOfJacksonville",
            twitterURL = "https://twitter.com/comedyclubofjax",
            showSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1Ax2-gUY33i_pRHZIwR8AULy6-nbnAbM8Qm5-CGISevc/gviz/tq",
            dealsSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1dnpODnbz6ME4RY5vNwrtAc6as3-uj2rK_IgtYszsvsM/gviz/tq";
    Intent browserIntent, alarmIntent/*, updateIntent*/;
    MyAdapter adapter;
    ListView listView;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    ArrayList<Show> shows;
    ArrayList<Offer> offers;
    DateFormat df;
    Date today;
    java.util.Calendar calendar, updateCalendar;
    AlarmManager alarmManager, updateAlarmManager;
    PendingIntent pendingIntent, updatePendingIntent;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate called");

        declarations();
        addMenuButtonFunctionality();
        setListViewAdapter();
        checkFirstRun();
        checkForPastShows();

    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((LinearLayout) findViewById(R.id.linearLayoutMain), R.drawable.background);
        manager.scaleImage((ImageView) findViewById(R.id.ivLogo), R.drawable.comedyclublogo2, 1, .5);

        firstCheck = PreferenceManager.getDefaultSharedPreferences(this);
        shows = new ArrayList<>();
        offers = new ArrayList<>();

        spSpreadsheets = getSharedPreferences(prefsSpreadsheets, MODE_PRIVATE);

        /*
        creates the ListView adapter and populates the ListView using generateData()
         */
        adapter = new MyAdapter(this, generateData());
        listView = (ListView) findViewById(R.id.listview);

        /*
        simpledateformat used to compare dates from spreadsheet
        to current date
         */
        df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        today = new Date();
        today.setTime(System.currentTimeMillis() - 1000 * 60 * 60 * 24);

        spNotifications = getSharedPreferences(prefsNotificationToggle, MODE_PRIVATE);

        /*
        alarmmanager and notification declarations
         */
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        calendar = java.util.Calendar.getInstance();

        /*
        updateAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        updateIntent = new Intent(MainActivity.this, Update.class);
        updatePendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        updateCalendar = java.util.Calendar.getInstance();
        */
    }//end declarations

    public void manageNotifications() {
            calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.FRIDAY);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 8);
            calendar.set(java.util.Calendar.MINUTE, 15);

            /*
            sets alarm manager to go off at 8:15 in the morning every 7 days on Thursday
            for testing, starts at 815 every morning starting wednesday
             */
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24/*1000 * 60 * 60 * 24 * 7*/, pendingIntent);
    }//end manageNotifications

    public void updateSpreadsheets() {
        /*
        not currently being used, will revisit later.
         */
        updateCalendar.setTimeInMillis(System.currentTimeMillis());
        updateCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        updateCalendar.set(java.util.Calendar.MINUTE, 0);

        updateAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateCalendar.getTimeInMillis(), 1000 * 60 * 60 * 24, updatePendingIntent);
    }//end updateSpreadsheets

    public void checkForPastShows() {
        try {
            for (int i = 0; i < shows.size(); i++) {
                if (today.after(df.parse(shows.get(i).getShowDate())))
                    shows.remove(i);
                for (int j = 0; j < shows.size(); j++) {
                    if (shows.get(i).equals(shows.get(j)) && i != j)
                        shows.remove(j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListViewAdapter() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                switch (position) {
                    case 0://this weekend
                        if (!shows.isEmpty())
                            startActivity(new Intent(MainActivity.this, ThisWeekend.class).putParcelableArrayListExtra("shows", shows));
                        else {
                            getShows();
                            startActivity(new Intent(MainActivity.this, Calendar.class).putParcelableArrayListExtra("shows", shows));
                        }
                        System.out.println("this weekend clicked");
                        break;
                    case 1://calendar
                        if (!shows.isEmpty())
                            startActivity(new Intent(MainActivity.this, Calendar.class).putParcelableArrayListExtra("shows", shows));
                        else {
                            getShows();
                            startActivity(new Intent(MainActivity.this, Calendar.class).putParcelableArrayListExtra("shows", shows));
                        }
                        System.out.println("calendar clicked");
                        break;
                    case 2://rewards and offers
                        if (!offers.isEmpty())
                            startActivity(new Intent(MainActivity.this, Deals.class).putParcelableArrayListExtra("offers", offers));
                        else {
                            getDeals();
                            startActivity(new Intent(MainActivity.this, Deals.class).putParcelableArrayListExtra("offers", offers));
                        }
                        System.out.println("deals clicked");
                        break;
                    case 3://food and drink
                        startActivity(new Intent(MainActivity.this, FoodAndDrink.class));
                        System.out.println("food and drink clicked");
                        break;
                    case 4://groups and parties
                        startActivity(new Intent(MainActivity.this, GroupsAndParties.class).putParcelableArrayListExtra("shows", shows));
                        System.out.println("groups and parties clicked");
                        break;
                }
            }
        });
    }//end setListViewAdapter

    public void checkFirstRun() {
        /*
        first run check. If first run, sets reward point value to 15, sets notifications
        to true, manages notifications.
         */
        getShows();
        getDeals();

        if (!firstCheck.getBoolean("firstRun", false)) {
            spPointValue = getSharedPreferences(prefsPointValueName, MODE_MULTI_PROCESS);
            editor = spPointValue.edit();
            editor.putInt("pointValue", initialPointValue);
            editor.apply();

            editor = spNotifications.edit();
            editor.putBoolean("notifications", true);
            editor.apply();

            editor = firstCheck.edit();
            editor.putBoolean("firstRun", true);
            editor.apply();

            manageNotifications();
            //updateSpreadsheets();
        } else {
            try {
                processShowsJson(new JSONObject(spSpreadsheets.getString("showsSpreadsheet", "")));
                processDealsJson(new JSONObject(spSpreadsheets.getString("dealsSpreadsheet", "")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//end checkFirstRun

    private ArrayList<Model> generateData() {
        /*
        populates the ListView on activity_main.xml with an icon and title.
         */
        ArrayList<Model> models = new ArrayList<>();
        models.add(new Model(R.drawable.ic_action_event, "This Weekend"));
        models.add(new Model(R.drawable.ic_action_go_to_today, "Upcoming Shows"));
        models.add(new Model(R.drawable.ic_action_important_gold, "Rewards and Offers"));
        models.add(new Model(R.drawable.ic_action_view_as_list, "Food and Drinks"));
        models.add(new Model(R.drawable.ic_action_group, "Groups and Parties"));

        return models;
    }

    public void addMenuButtonFunctionality() {
        /*
        enables overflow menu icon, even with presence of hardware menu key
         */
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("onOptionsItemSelected called");

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, Settings.class));
                System.out.println("settings clicked");
                return true;
            case R.id.action_directions:
                Intent directionsIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(directionsURI));
                if (directionsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(directionsIntent);
                }
                System.out.println("directions clicked");
                return true;
            case R.id.action_about_us:
                startActivity(new Intent(MainActivity.this, AboutUs.class));
                System.out.println("about us clicked");
                return true;
            case R.id.action_contact:
                startActivity(new Intent(MainActivity.this, Contact.class));
                System.out.println("contact clicked");
                return true;
            case R.id.action_now_hiring:
                startActivity(new Intent(MainActivity.this, NowHiring.class));
                System.out.println("now hiring clicked");
                return true;
            case R.id.action_report_a_bug:
                startActivity(new Intent(MainActivity.this, ReportABug.class));
                System.out.println("report a bug clicked");
            default:
                return super.onOptionsItemSelected(item);
        }
    }//end onOptionsItemSelected

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
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask(this, new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        if (!object.toString().equals(spSpreadsheets.getString("showsSpreadsheet", ""))) {
                            System.out.println("shows spreadsheet is the same");
                            processShowsJson(object);
                            editor = spSpreadsheets.edit();
                            editor.putString("showsSpreadsheet", object.toString());
                            editor.apply();
                        }
                    }
                }).execute(showSpreadsheetURL);
            } else {
                Toast.makeText(this, "Network not available.", Toast.LENGTH_SHORT).show();
                System.out.println("Network info not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end getShows

    public void getDeals() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask(this, new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        if (!object.toString().equals(spSpreadsheets.getString("dealsSpreadsheet", ""))) {
                            System.out.println("deals spreadsheet is the same");
                            processDealsJson(object);
                            editor = spSpreadsheets.edit();
                            editor.putString("dealsSpreadsheet", object.toString());
                            editor.apply();
                        }
                    }
                }).execute(dealsSpreadsheetURL);
            } else {
                Toast.makeText(this, "Network not available.", Toast.LENGTH_SHORT).show();
                System.out.println("Network info not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }//end processDealsJson
}