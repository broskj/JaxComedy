package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Kyle on 1/24/2015.
 */
public class Reserve extends Activity {

    EditText etName, etGuests, etEmail;
    RadioGroup rbgShowTimes;
    RadioButton rbEarlyShow, rbLateShow, rbSpecialShow;
    Spinner spDate;
    final String[] email = {"info@jacksonvillecomedy.com"};
    final String[] ccEmail = {"jaxcomedy@gmail.com"};
    String sName, sGuests, sDate, sShowtime, sEmail;
    String confirmationCode, message, subject;
    int screenWidth, screenHeight, groupPosition = -1;
    ArrayList<Show> shows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve);
        System.out.println("groups and parties created");

        declarations();
        setDateAdapter();

    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlReserve), R.drawable.background);

        shows = new ArrayList<>();
        shows = getIntent().getExtras().getParcelableArrayList("shows");

        groupPosition = getIntent().getExtras().getInt("groupPosition");

        etName = (EditText) (findViewById(R.id.etGroupReservationName));
        etGuests = (EditText) (findViewById(R.id.etGroupGuests));
        etEmail = (EditText) (findViewById(R.id.etEmailAddress));
        rbgShowTimes = (RadioGroup) (findViewById(R.id.rbgShowTimes));
        rbEarlyShow = (RadioButton) (findViewById(R.id.rbEarlyShow));
        rbLateShow = (RadioButton) (findViewById(R.id.rbLateShow));
        rbSpecialShow = (RadioButton) (findViewById(R.id.rbSpecialShow));

        spDate = (Spinner) (findViewById(R.id.spDate));

        spDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                System.out.println("onItemSelected called for spDate in Reserve at item " + pos);

                /*
                sets date string for email use equal to the substring from the start of the
                text to the first space, where the name begins, i.e. 1/1/1970 John Doe
                is saved as 1/1/1970
                 */
                sDate = parent.getItemAtPosition(pos).toString().substring(0,
                        parent.getItemAtPosition(pos).toString().indexOf(" "));

                /*
                allows date spinner to be changed.  initializes selection to groupPosition,
                then sets selection to pos thereafter.
                 */
                if (pos == 0) {
                    spDate.setSelection(groupPosition);
                    groupPosition = 0;
                } else {
                    spDate.setSelection(pos);
                }

                /*
                based on certain showtimes, enables/disables radio buttons and checks
                or unchecks them.
                 */
                if (shows.get(pos).getShowTime() != 1) {
                    rbEarlyShow.setEnabled(true);
                    rbLateShow.setEnabled(true);
                    rbSpecialShow.setEnabled(false);

                    rbEarlyShow.setChecked(true);
                    rbSpecialShow.setChecked(false);
                } else {
                    rbEarlyShow.setEnabled(false);
                    rbLateShow.setEnabled(false);
                    rbSpecialShow.setEnabled(true);

                    rbEarlyShow.setChecked(false);
                    rbLateShow.setChecked(false);
                    rbSpecialShow.setChecked(true);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("noNothingSelected called for spDate in Reserve");
                sDate = parent.getItemAtPosition(groupPosition).toString();
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

    public void setDateAdapter() {
        String[] s = new String[shows.size()];

        for (int i = 0; i < s.length; i++) {
            s[i] = shows.get(i).getShowDate() + " " + shows.get(i).getComedian();
        }
        /*
        this commented code uses java.util.Calendar to get date of next saturday/sunday and
          put them in the spinner.  should be replaced with above code, but I'm keeping it
          here in case it becomes useful in the future.
         */

        /*
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
        */

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDate.setAdapter(adapter);

    }//end setDateAdapter

    private void convertEditTextToString() {
        // TODO Auto-generated method stub
        sName = etName.getText().toString();
        sGuests = etGuests.getText().toString();
        sEmail = etEmail.getText().toString();

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
                sShowtime = "~~something bad happened, contact user~~";
                break;
        }
    }//end convertEditTextToString

    public void onReserveSeatsClick(View view) {
        System.out.println("reserve seats clicked from Reserve");
        convertEditTextToString();

        /*
        checks if the string of each edittext field is empty, and sets focus
          to that edittext if it is and gives a message.
         */
        if (sName.matches("")) {
            etName.requestFocus();
            etName.setError("Must enter name.");
        } else if (sEmail.matches("")) {
            etEmail.requestFocus();
            etEmail.setError("Must enter email.");
        } else if (sGuests.matches("")) {
            etGuests.requestFocus();
            etGuests.setError("Must enter number.");
        } else {
            SimpleDateFormat dateformat = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            Date date = new Date();
            subject = "Reservation for " + sGuests + " on " + sDate;
            confirmationCode = Integer.toString(new Random().nextInt(999999) + 1);
            message = sName + " has requested " + sGuests + " ticket(s) for the " + sShowtime + " show on " +
                    sDate + ".\nConfirmation code: " + confirmationCode + "\n\n" + dateformat.format(date);

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
            emailIntent.putExtra(Intent.EXTRA_CC, ccEmail);
            emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{sEmail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
/*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm your reservation")
                    .setMessage("Pressing OK will direct you to your phone's email client.  " +
                    "Print the email and present it at the door to confirm your reservation with" +
                    "the host or hostess.");
            //need onclicklistener
            AlertDialog dialog = builder.create();
            dialog.show();
*/
            startActivity(emailIntent);
            this.finish();
        }
    }//end onReserveSeatsClick

}
