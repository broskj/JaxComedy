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
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Kyle on 1/24/2015.
 */
public class Reserve extends Activity {

    EditText etName, etGuests;
    RadioGroup rbgShowTimes;
    Spinner spDate;
    final String email = "info@jacksonvillecomedy.com, kjbrost@gmail.com";
    String sName, sGuests, sDate, sShowtime;
    String confirmationCode, message, subject;
    int screenWidth, screenHeight, groupPosition = -1;
    ArrayList<Show> shows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve);
        System.out.println("groups and parties created");

        declarations();
        manageActionBar();
        scaleBackground();
        setDateAdapter();

        /*
        if(groupPosition != -1){
            //navigated in from either this weekend or upcoming shows.  set date adapter
                to disabled on correct date.
        else
            //navigated in from groups and parties, need to enable date adapter
        }
        */
    }//end onCreate

    public void declarations(){
        screenWidth = getIntent().getExtras().getInt("screenWidth");
        screenHeight = getIntent().getExtras().getInt("screenHeight");

        shows = new ArrayList<>();
        shows = getIntent().getExtras().getParcelableArrayList("shows");

        groupPosition = getIntent().getExtras().getInt("groupPosition");

        etName = (EditText) (findViewById(R.id.etGroupReservationName));
        etGuests = (EditText) (findViewById(R.id.etGroupGuests));
        rbgShowTimes = (RadioGroup) (findViewById(R.id.rbgShowTimes));
        spDate = (Spinner) (findViewById(R.id.spDate));

        spDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                System.out.println("noItemSelected called for spDate in Reserve at item " + pos);
                sDate = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("noNothingSelected called for spDate in Reserve");
                sDate = parent.getItemAtPosition(0).toString();
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

    /*
    creates ActionBar object, enables the home button, and resets title to ''
    */
    public void manageActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Reserve Seats");
    }//end manageActionBar

    /*
    scales background for performance
    */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void scaleBackground() {
        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rlReserve);
        Bitmap bitmapBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap resizedBitmapBackground = Bitmap.createScaledBitmap(bitmapBackground, screenWidth, screenHeight, true);
        myLayout.setBackground(new BitmapDrawable(getResources(), resizedBitmapBackground));
    }//end scaleBackground

    public void setDateAdapter() {
        String[] s = new String[8];
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        java.util.Calendar today = java.util.Calendar.getInstance();
        int dayOfWeek = today.get(java.util.Calendar.DAY_OF_WEEK),
                daysUntilNextFriday = java.util.Calendar.FRIDAY - dayOfWeek,
                daysUntilNextSaturday = java.util.Calendar.SATURDAY - dayOfWeek;
        if (daysUntilNextFriday < 0) {
            daysUntilNextFriday = daysUntilNextFriday + 7;
        }
        if (daysUntilNextSaturday < 0) {
            daysUntilNextSaturday = daysUntilNextSaturday + 7;
        }
        java.util.Calendar nextFriday = (java.util.Calendar) today.clone(), nextSaturday = (java.util.Calendar) today.clone();
        nextFriday.add(java.util.Calendar.DAY_OF_WEEK, daysUntilNextFriday);
        nextSaturday.add(java.util.Calendar.DAY_OF_WEEK, daysUntilNextSaturday);

        for (int i = 0; i < s.length; i++) {
            if (dayOfWeek == java.util.Calendar.SATURDAY) {
                if (i % 2 == 0) {
                    s[i] = "Saturday " + df.format(nextSaturday.getTime());
                    nextSaturday.add(java.util.Calendar.DAY_OF_WEEK, 7);
                } else {
                    s[i] = "Friday " + df.format(nextFriday.getTime());
                    nextFriday.add(java.util.Calendar.DAY_OF_WEEK, 7);
                }
            } else {
                if (i % 2 == 0) {
                    s[i] = "Friday " + df.format(nextFriday.getTime());
                    nextFriday.add(java.util.Calendar.DAY_OF_WEEK, 7);
                } else {
                    s[i] = "Saturday " + df.format(nextSaturday.getTime());
                    nextSaturday.add(java.util.Calendar.DAY_OF_WEEK, 7);
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDate.setAdapter(adapter);

    }//end setDateAdapter

    private void convertEditTextToString() {
        // TODO Auto-generated method stub
        sName = etName.getText().toString();
        sGuests = etGuests.getText().toString();

        switch (rbgShowTimes.getCheckedRadioButtonId()) {
            case R.id.rbEarlyShow:
                sShowtime = "8:04";
                break;
            case R.id.rbLateShow:
                sShowtime = "10:10";
                break;
            case R.id.rbSpecialShow:
                sShowtime = "special";
                break;
            default:
                sShowtime = "~~something wrong happened, contact user~~";
                break;
        }
    }//end convertEditTextToString

    public void onReserveSeatsClick(View view) {
        System.out.println("reserve seats clicked from Reserve");
        convertEditTextToString();
        SimpleDateFormat dateformat = new SimpleDateFormat(
                "MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        subject = "Reservation for " + sDate;
        confirmationCode = Integer.toString(new Random().nextInt(999999)+1);
        message = sName + " has requested " + sGuests + " tickets for the " + sShowtime + " show on " +
                sDate + ".\nConfirmation code: " + confirmationCode + "\n\n" + dateformat.format(date);

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(emailIntent);
        finish();
    }

}
