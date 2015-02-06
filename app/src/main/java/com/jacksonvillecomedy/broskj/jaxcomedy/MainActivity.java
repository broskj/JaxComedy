package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends Activity {

    SharedPreferences firstCheck;
    final int initialPointValue = 150;
    static final String prefsPointValueName = "userPointValue";
    Show show;
    Show[] shows;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate called");

        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        final int screenHeight = getResources().getDisplayMetrics().heightPixels;

        /*
        first run check. If first run, sets reward point value to 150.
         */
        firstCheck = PreferenceManager.getDefaultSharedPreferences(this);
        if(!firstCheck.getBoolean("firstRun", false)){
            System.out.println("first run statement entered");
            SharedPreferences spPointValue = getSharedPreferences(prefsPointValueName, MODE_PRIVATE);
            SharedPreferences.Editor editor = spPointValue.edit();
            editor.putInt("pointValue", initialPointValue);
            editor.commit();

            editor = firstCheck.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
        }

        manageActionBar();
        scaleBackground(screenWidth, screenHeight);

        /*
        creates the ListView adapter and populates the ListView using generateData()
         */
        MyAdapter adapter = new MyAdapter(this, generateData());
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        /*
        scale the comedyclublogo imageview programmatically to improve performance
         */
        ImageView myImage = (ImageView) findViewById(R.id.ivLogo);
        Bitmap bitmapIVLogo = BitmapFactory.decodeResource(getResources(), R.drawable.comedyclublogo2);
        Bitmap resizedBitmapIVLogo = Bitmap.createScaledBitmap(bitmapIVLogo, screenWidth, screenHeight / 2, true);
        myImage.setImageBitmap(resizedBitmapIVLogo);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                System.out.println("position is " + position);
                switch (position) {
                    case 0://this weekend
                        startActivity(new Intent(MainActivity.this, ThisWeekend.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                        System.out.println("this weekend clicked");
                        break;
                    case 1://calendar
                        startActivity(new Intent(MainActivity.this, Calendar.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                        System.out.println("calendar clicked");
                        break;
                    case 2://deals
                        startActivity(new Intent(MainActivity.this, Deals.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                        System.out.println("deals clicked");
                        break;
                    case 3://food and drink
                        startActivity(new Intent(MainActivity.this, FoodAndDrink.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                        System.out.println("food and drink clicked");
                        break;
                    case 4://groups and parties
                        startActivity(new Intent(MainActivity.this, GroupsAndParties.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
                        System.out.println("groups and parties clicked");
                        break;
                    default:
                        System.out.println("Something went wrong");
                }
            }
        });
    }

    /*
    populates the ListView on activity_main.xml with an icon and title.
     */
    private ArrayList<Model> generateData() {
        ArrayList<Model> models = new ArrayList<Model>();
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
                startActivity(new Intent(MainActivity.this, Directions.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
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
    scales background for performance
    */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleBackground(int screenWidth, int screenHeight) {
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearLayoutMain);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));
    }//end scaleBackground
}
