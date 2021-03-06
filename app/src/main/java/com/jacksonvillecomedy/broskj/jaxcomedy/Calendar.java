package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyle on 12/29/2014.
 *
 * no longer in use, keeping class for future reference.
 */
public class Calendar extends Activity {

    CalendarExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<Show> shows;
    int screenWidth, screenHeight;

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        System.out.println("calendar created");

        declarations();
    }//end onCreate

    @Override
    public void onRestart() {
        super.onRestart();

        /*
        collapses all groups when activity is resumed, particularly for after a deal was redeemed.
         */
        for (int i = 0; i < expListView.getExpandableListAdapter().getGroupCount(); i++) {
            expListView.collapseGroup(i);
        }
    }//end onRestart

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlCalendar), R.drawable.background);

        shows = new ArrayList<>();
        shows = getIntent().getExtras().getParcelableArrayList("shows");

        expListView = (ExpandableListView) (findViewById(R.id.elvCalendar));
        prepareListData();

        listAdapter = new CalendarExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                openReserve(groupPosition);
                return true;
            }
        });

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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } //end onOptionsItemSelected

    public void prepareListData() {
        List<String>[] list = (ArrayList<String>[]) new ArrayList[shows.size()];
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        for (int i = 0; i < list.length; i++) {

            list[i] = new ArrayList<>();
            listDataHeader.add("(" + shows.get(i).getShowDate() + ") " + shows.get(i).getComedian());
            list[i].add(shows.get(i).getDescription());
            listDataChild.put(listDataHeader.get(i), list[i]);
        }
    }//end prepareListData

    public void openReserve(int index) {
        startActivity(new Intent(this, Reserve.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight).putExtra("groupPosition", index).putParcelableArrayListExtra("shows", shows));
        System.out.println("reserve clicked from calendar");
    }//end openReserve
}