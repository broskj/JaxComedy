package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 3/17/2015.
 * <p/>
 * This class and its associated layout file are what will replace Calendar and its layout.
 * Layout will be similar, but uses Cards instead of ExpandableListViews.
 * <p/>
 * Code for using Cards courtesy of http://javapapers.com/android/android-cards-list-view/ (including
 * xml files).
 */
public class UpcomingShows extends Activity {

    ArrayList<Show> shows;
    ListView lvComics;
    CardArrayAdapter cardArrayAdapter;

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_shows);
        System.out.println("upcoming shows created");

        declarations();
    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlUpcomingShows), R.drawable.background);

        shows = new ArrayList<>();
        shows = getIntent().getExtras().getParcelableArrayList("shows");

        lvComics = (ListView) findViewById(R.id.lvComics);
        lvComics.addHeaderView(new View(this));
        lvComics.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < shows.size(); i++) {
            Card card = new Card(shows.get(i).getShowDate(), shows.get(i).getComedian());
            cardArrayAdapter.add(card);
        }

        lvComics.setAdapter(cardArrayAdapter);

        lvComics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= 1;
                //Toast.makeText(getApplicationContext(), (position + " " + shows.get(position).getComedian()), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpcomingShows.this, ShowDetails.class).putExtra("position", position).putParcelableArrayListExtra("shows", shows));
            }
        });

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

}//end class UpcomingShows