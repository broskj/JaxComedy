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

/**
 * Created by Kyle on 12/29/2014.
 */

public class AboutUs extends Activity {

    int screenWidth, screenHeight;
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

        declarations();

    } //end onCreate

    public void declarations(){
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlAboutUs), R.drawable.background);
        manager.scaleImage((ImageView) findViewById(R.id.ivAboutUs), R.drawable.front_of_building, 1, .25);

        expListView = (ExpandableListView) (findViewById(R.id.elvAboutUs));
        prepareListData();

        listAdapter = new AboutUsExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        /*
        creates listener for parent objects of expListView.  when a parent is expanded to show child,
          closes all other parents.
         */
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < expListView.getExpandableListAdapter().getGroupCount(); i++) {
                    if (i != groupPosition)
                        expListView.collapseGroup(i);
                }
            }
        });
    }

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

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        /*
         declares arraylist of headers for expListView and hashmap of
         children Strings for expListView.  string arrays are used for header and children
         text, and are traversed in the for loop to give text to expandable list view
         sections.
         */
        List<String>[] list = (ArrayList<String>[]) new ArrayList[7];
        String[] headers = {"About the Club", "More Room", "Best Seats", "No Two Item Minimum",
                "Online Tickets", "App Exclusives", "Saving Lives"};
        String[] children = {getResources().getString(R.string.about_the_club),
                getResources().getString(R.string.more_room),
                getResources().getString(R.string.best_seats),
                getResources().getString(R.string.no_two_item_minimum),
                getResources().getString(R.string.online_tickets),
                getResources().getString(R.string.app_exclusives),
                getResources().getString(R.string.saving_lives)};

        for (int i = 0; i < list.length; i++) {

            list[i] = new ArrayList<>();
            listDataHeader.add(headers[i]);
            list[i].add(children[i]);
            listDataChild.put(listDataHeader.get(i), list[i]);
        }

    }//end prepareListData
}
