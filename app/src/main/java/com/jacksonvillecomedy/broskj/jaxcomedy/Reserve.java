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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    EditText etName, etGuests, etEmail, etPhone;
    RadioGroup rbgShowTimes;
    RadioButton rbEarlyShow, rbLateShow, rbSpecialShow;
    CheckBox cbBestSeats;
    Spinner spDate;
    final String[] email = {"steve@jacksonvillecomedy.com"};
    final String[] ccEmail = {"jaxcomedy@gmail.com"};
    final String emailPassword = "K04b8244";
    String sName, sGuests, sDate, sShowtime, sEmail, sPhone;
    String confirmationCode, message, subject, bestSeatsDialog, bestSeatsMessage;
    int groupPosition = -1, cbClicks = 0;
    ArrayList<Show> shows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve);
        System.out.println("reserve created");

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
        etPhone = (EditText) (findViewById(R.id.etPhoneNumber));
        rbgShowTimes = (RadioGroup) (findViewById(R.id.rbgShowTimes));
        rbEarlyShow = (RadioButton) (findViewById(R.id.rbEarlyShow));
        rbLateShow = (RadioButton) (findViewById(R.id.rbLateShow));
        rbSpecialShow = (RadioButton) (findViewById(R.id.rbSpecialShow));
        cbBestSeats = (CheckBox) (findViewById(R.id.cbBestSeats));

        cbBestSeats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbBestSeats.isChecked()) {
                    cbClicks++;
                    sPhone = etPhone.getText().toString();
                    if (cbClicks == 1 && sPhone.matches("")) {
                        AlertDialog.Builder bsBuilder = new AlertDialog.Builder(Reserve.this);
                        bsBuilder.setTitle("Best Seats")
                                .setMessage("Thanks for requesting the Best Seats in the House!  In order " +
                                        "to confirm your Best Seats reservation, enter your phone number in " +
                                        "the field above so we can validate your purchase.");
                        bsBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etPhone.requestFocus();
                            }
                        });
                        AlertDialog dialog = bsBuilder.create();
                        dialog.show();
                    } else if (cbClicks == 1 && !sPhone.matches("")) {
                        final AlertDialog.Builder bsBuilder = new AlertDialog.Builder(Reserve.this);
                        bsBuilder.setTitle("Best Seats")
                                .setMessage("Thanks for requesting the Best Seats in the House!  We'll " +
                                        "contact you by phone and validate your purchase to reserve your seats.");
                        bsBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    }
                }
            }
        });

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDate.setAdapter(adapter);

    }//end setDateAdapter

    private void convertEditTextToString() {
        // TODO Auto-generated method stub
        sName = etName.getText().toString();
        sGuests = etGuests.getText().toString();
        sEmail = etEmail.getText().toString();
        sPhone = etPhone.getText().toString();

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
        } else if (Integer.parseInt(sGuests) < 1 || Integer.parseInt(sGuests) > 100) {
            etGuests.requestFocus();
            etGuests.setError("Must enter between 1 and 100 guests.");
        } else if (cbBestSeats.isChecked() && sPhone.matches("")) {
            etPhone.requestFocus();
            etPhone.setError("Must enter phone number.");
        } else {
            if (cbBestSeats.isChecked()) {
                bestSeatsDialog = "\n\nYou've requested the Best Seats in the house; please be aware " +
                        "that our Best Seats are very popular and very limited, and you must validate your " +
                        "purchase by phone to reserve your seats.  If the phone number above is accurate, " +
                        "you will be contacted at our earliest convenience.";
                bestSeatsMessage = "The Best Seats in the House have been requested for this reservation.";
            } else {
                bestSeatsDialog = "";
                bestSeatsMessage = "";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(Reserve.this);
            builder.setTitle("Confirm your reservation")
                    .setMessage("Pressing OK will direct you to your phone's email client; send the message, then " +
                            "print the email and present it at the door to confirm your reservation with " +
                            "the host or hostess." + bestSeatsDialog);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendEmail();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }//end onReserveSeatsClick

    public void sendEmail() {
        SimpleDateFormat dateformat = new SimpleDateFormat(
                "MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        subject = "Android reservation for " + sGuests + " on " + sDate;
        confirmationCode = Integer.toString(new Random().nextInt(999999) + 1);
        message = sName + " has requested " + sGuests + " ticket(s) for the " + sShowtime + " show on " +
                sDate + "\n\n" + bestSeatsMessage + ".\n\nConfirmation code: " + confirmationCode +
                dateformat.format(date);

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_CC, ccEmail);
        emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{sEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(emailIntent);
/*
        try {
            GmailSender sender = new GmailSender(ccEmail, emailPassword);
            sender.sendMail(subject,
                    message,
                    ccEmail, *//* sender (jaxcomedy@gmail.com *//*
                    ccEmail, *//* recipient (Steve) *//*
                    ccEmail, *//* ccRecipient (jaxcomedy@gmail.com *//*
                    sEmail); *//* bccRecipient (user) *//*
        } catch (Exception e) {
            Toast.makeText(this, "Error sending email.", Toast.LENGTH_SHORT).show();
        }*/
        this.finish();

    }//end sendEmail
}
