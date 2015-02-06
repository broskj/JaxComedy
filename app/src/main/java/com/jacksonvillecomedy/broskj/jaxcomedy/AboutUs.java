package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AboutUs extends Activity {

    AboutUsExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        System.out.println("about us created");

        final int screenWidth = getIntent().getExtras().getInt("screenWidth");
        final int screenHeight = getIntent().getExtras().getInt("screenHeight");

        expListView = (ExpandableListView) (findViewById(R.id.elvAboutUs));

        manageActionBar();
        scaleBackground(screenWidth, screenHeight);
        prepareListData();

        listAdapter = new AboutUsExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < expListView.getExpandableListAdapter().getGroupCount(); i++) {
                    if (i != groupPosition)
                        expListView.collapseGroup(i);
                }
            }
        });

        /*
        converts front_of_building to bitmap for
        performance and sets as image
         */
        ImageView myImage1 = (ImageView) findViewById(R.id.ivAboutUs);
        Bitmap bitmapAboutUs = BitmapFactory.decodeResource(getResources(), R.drawable.front_of_building);
        Bitmap resizedBitmapAboutUs = Bitmap.createScaledBitmap(bitmapAboutUs, screenWidth, screenHeight / 4, true);
        myImage1.setImageBitmap(resizedBitmapAboutUs);

    } //end onCreate

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

    /*
    creates ActionBar object, enables the home button, and resets title to ''
    */
    public void manageActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("About Us");
    }//end manageActionBar

    /*
    scales background for performance
    */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleBackground(int screenWidth, int screenHeight) {
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rlAboutUs);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));

    }//end scaleBackground

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        List<String> aboutTheClub = new ArrayList<>();
        List<String> moreRoom = new ArrayList<>();
        List<String> bestSeats = new ArrayList<>();
        List<String> noTwoItemMinimum = new ArrayList<>();
        List<String> onlineTickets = new ArrayList<>();
        List<String> appExclusives = new ArrayList<>();
        List<String> savingLives = new ArrayList<>();

        listDataHeader.add("About the Club");
        listDataHeader.add("More Room");
        listDataHeader.add("Best Seats");
        listDataHeader.add("No Two Item Minimum");
        listDataHeader.add("Online Tickets");
        listDataHeader.add("App Exclusives");
        listDataHeader.add("Saving Lives");

        aboutTheClub.add(getResources().getString(R.string.about_the_club));
        moreRoom.add(getResources().getString(R.string.more_room));
        bestSeats.add(getResources().getString(R.string.best_seats));
        noTwoItemMinimum.add(getResources().getString(R.string.no_two_item_minimum));
        onlineTickets.add(getResources().getString(R.string.online_tickets));
        appExclusives.add(getResources().getString(R.string.app_exclusives));
        savingLives.add(getResources().getString(R.string.saving_lives));

        listDataChild.put(listDataHeader.get(0), aboutTheClub);
        listDataChild.put(listDataHeader.get(1), moreRoom);
        listDataChild.put(listDataHeader.get(2), bestSeats);
        listDataChild.put(listDataHeader.get(3), noTwoItemMinimum);
        listDataChild.put(listDataHeader.get(4), onlineTickets);
        listDataChild.put(listDataHeader.get(5), appExclusives);
        listDataChild.put(listDataHeader.get(6), savingLives);

    }//end prepareListData
}
