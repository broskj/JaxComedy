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
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Steve on 12/29/2014.
 */
public class Deals extends Activity {

    DealsExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    TextView tvRewardPoints;
    SharedPreferences spRewardPointValue;
    Offer[] offers;
    Offer offer;
    int screenWidth, screenHeight, rewardPointValue;
    static final String prefsPointValueName = "userPointValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals);
        System.out.println("deals created");

        declarations();
        manageActionBar();
        scaleBackground(screenWidth, screenHeight);
        prepareListData();

    }//end onCreate

    @Override
    public void onResume() {
        super.onResume();

        spRewardPointValue = getSharedPreferences(prefsPointValueName, MODE_PRIVATE);
        rewardPointValue = spRewardPointValue.getInt("pointValue", -1);
        tvRewardPoints.setText(Integer.toString(rewardPointValue));

    }//end onResume

    public void declarations(){
        /*
        dimension variables for scaling
         */
        screenWidth = getIntent().getExtras().getInt("screenWidth");
        screenHeight = getIntent().getExtras().getInt("screenHeight");

        /*
        declares sharedpreferences file for the reward point value.
        gets the value and assigns it to the int variable rewardPointValue.
        declares textview containing the point value and sets the text to that value
         */
        spRewardPointValue = getSharedPreferences(prefsPointValueName, MODE_PRIVATE);
        rewardPointValue = spRewardPointValue.getInt("pointValue", -1);
        tvRewardPoints = (TextView) findViewById(R.id.tvRewardPoints);
        tvRewardPoints.setText(Integer.toString(rewardPointValue));

        /*
        declares the expandable listview, the listadapter, and sets the expandable listview to
          the adapter.
         */
        expListView = (ExpandableListView) (findViewById(R.id.elvDeals));
        listAdapter = new DealsExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        /*
        used in prepareListData.  declares arraylist of headers for expListView and hashmap of
          children Strings for expListView.
         */
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        /*
        creates listener for child objects of expListView.  on a child click, constructs an Offer
          class based on which child was clicked and passes it to openRedeem.
         */
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //offer = new Offer(offers[groupPosition].getPointValue(), offers[groupPosition].getOfferTitle,
                // offers[groupPosition].getOfferDescription());
                offer = new Offer(150, "Test Title", "Test description");
                openRedeem(offer);
                return true;
            }
        });

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

    public void manageActionBar() {
    /*
    creates ActionBar object, enables the home button, and resets title
    */
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Rewards and Offers");
    }//end manageActionBar

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleBackground(int screenWidth, int screenHeight) {
    /*
    scales background for performance
    */
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.llDeals);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));
    }//end scaleBackground

    public void onRewardPointsClick(View view) {
        /*
        starts activity AddRewardPoints
         */
        startActivity(new Intent(this, AddRewardPoints.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight));
        System.out.println("redeem offer clicked from deals");
    }

    public void prepareListData() {
        /*
        creates a new array of lists of strings.  the for loop initializes them with values for
          children.  sets the data headers to some text (needs to be changed when deals are imported
          from google spreadsheet) and then sets the text from the list array and uses listDataChild.put
          to set the text of each parent/child in expListView.
         */
        List<String>[] list = (ArrayList<String>[]) new ArrayList[5];

        for (int i = 0; i < list.length; i++) {
            list[i] = new ArrayList<>();
            listDataHeader.add("(xxx) Deal " + i);
            list[i].add("Deal info " + i);
            listDataChild.put(listDataHeader.get(i), list[i]);
        }
    }//end prepareListData

    public void openRedeem(Offer offer) {
        /* 
        checks if offer.getPointValue() is less than or equal to the number of points the user has.
            if true, then the user can redeem the offer.  RedeemOffer.class is opened and the point value
              and offer title are passed to the class.
            if false, an error message is displayed and the user is not redirected.
         */
        if (offer.getPointValue() <= rewardPointValue) {
            startActivity(new Intent(this, RedeemOffer.class).putExtra("screenWidth", screenWidth).putExtra("screenHeight", screenHeight)
                    .putExtra("pointValue", offer.getPointValue()).putExtra("offerTitle", offer.getOfferTitle()));
            System.out.println("redeem offer clicked from deals");
        } else
            Toast.makeText(this, "Not enough points for this deal.", Toast.LENGTH_SHORT).show();

    }//end openReserve
}