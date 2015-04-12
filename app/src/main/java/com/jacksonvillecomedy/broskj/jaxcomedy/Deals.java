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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyle on 12/29/2014.
 */
public class Deals extends Activity {
    TextView tvRewardPoints;
    SharedPreferences spRewardPointValue;
    ArrayList<Offer> offers;
    ListView lvOffers;
    CardArrayAdapter cardArrayAdapter;
    int rewardPointValue;
    static final String prefsPointValueName = "userPointValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals);
        System.out.println("deals created");

        declarations();

    }//end onCreate

    @Override
    public void onRestart() {
        super.onRestart();

        spRewardPointValue = getSharedPreferences(prefsPointValueName, MODE_MULTI_PROCESS);
        rewardPointValue = spRewardPointValue.getInt("pointValue", -1);
        tvRewardPoints.setText(Integer.toString(rewardPointValue));

    }//end onResume

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((LinearLayout) findViewById(R.id.llDeals), R.drawable.background);

        /*
        initializes arraylist of offers and populates them with offer list from MainActivity,
          which came from spreadsheet.
         */
        offers = new ArrayList<>();
        offers = getIntent().getExtras().getParcelableArrayList("offers");

        lvOffers = (ListView) findViewById(R.id.lvOffers);
        lvOffers.addHeaderView(new View(this));
        lvOffers.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < offers.size(); i++) {
            Card card = new Card("(" + offers.get(i).getPointValue() + ") " + offers.get(i).getOfferTitle(),
                    offers.get(i).getOfferDescription());
            cardArrayAdapter.add(card);
        }

        lvOffers.setAdapter(cardArrayAdapter);

        lvOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position -= 1;
                if (offers.get(position).getPointValue() != 0)
                    openRedeem(offers.get(position));
            }
        });

        /*
        declares sharedpreferences file for the reward point value.
        gets the value and assigns it to the int variable rewardPointValue.
        declares textview containing the point value and sets the text to that value
         */
        spRewardPointValue = getSharedPreferences(prefsPointValueName, MODE_MULTI_PROCESS);
        rewardPointValue = spRewardPointValue.getInt("pointValue", -1);
        tvRewardPoints = (TextView) findViewById(R.id.tvRewardPoints);
        tvRewardPoints.setText(Integer.toString(rewardPointValue));

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

    public void onRewardPointsClick(View view) {
        /*
        starts activity AddRewardPoints
         */
        startActivity(new Intent(this, AddRewardPoints.class));
        System.out.println("redeem offer clicked from deals");
    }//end onRewardPointsClick

    public void openRedeem(Offer offer) {
        /*
        checks if offer.getPointValue() is less than or equal to the number of points the user has.
            if true, then the user can redeem the offer.  RedeemOffer.class is opened and the point value
              and offer title are passed to the class.
            if false, an error message is displayed and the user is not redirected.
         */
        if (offer.getPointValue() <= rewardPointValue) {
            startActivity(new Intent(this, RedeemOffer.class).putExtra("offer", offer));
            System.out.println("redeem offer clicked from deals");
        } else
            Toast.makeText(this, "Not enough points for this deal.", Toast.LENGTH_SHORT).show();

    }//end openReserve
}