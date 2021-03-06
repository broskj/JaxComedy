package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kyle on 12/29/2014.
 */
public class Contact extends Activity {

    TextView tvMain;
    EditText etName, etEmail;
    String sName, sEmail, someMessage = "", emailSubject = "";
    String[] email = {"info@jacksonvillecomedy.com"};
    Button sendEmail;
    int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        System.out.println("contact created");

        declarations();
        makePhoneNumberClickable();
    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlContact), R.drawable.background);

        tvMain = (TextView) (findViewById(R.id.tvContactMain));
        etName = (EditText) (findViewById(R.id.etContactName));
        etName.requestFocus();
        etEmail = (EditText) (findViewById(R.id.etContactMessage));
        etEmail.setMovementMethod(new ScrollingMovementMethod());
        sendEmail = (Button) (findViewById(R.id.btSendEmail));

        try {
            someMessage = getIntent().getExtras().getString("message");
            if (!someMessage.matches("")) {
                etEmail.setText(someMessage);
                emailSubject = "Group Party information requested";
            }
        } catch (Exception e) {
            System.out.println("someMessage in Contact equal to \"\"");
        }
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
    onClick method for email button.  Converts EditText text to string, formats a date, and
    compiles them into an email message.
     */
    public void onSendEmail(View view) {
        convertEditTextToString();

        if (sName.matches("")) {
            etName.requestFocus();
            etName.setError("Must enter name.");
        } else if (sEmail.matches("")) {
            etEmail.requestFocus();
            etEmail.setError("Must enter message.");
        } else {
            SimpleDateFormat dateformat = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            Date date = new Date();
            String message = sName + '\n' + sEmail + '\n' + dateformat.format(date);
            String subject = "Mobile App Message From " + sName;

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
            if (emailSubject.matches(""))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            else
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(emailIntent);
            this.finish();
        }

    }//end onSendEmail

    private void convertEditTextToString() {
        // TODO Auto-generated method stub
        sName = etName.getText().toString();

        sEmail = etEmail.getText().toString();
    }//end convertEditTextToString

    public void makePhoneNumberClickable() {
        SpannableString ss = new SpannableString(this.getString(R.string.contact_textview_main));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:9046464277"));
                startActivity(new Intent(phoneIntent));
            }
        };
        ss.setSpan(clickableSpan, 102, 116, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvMain.setText(ss);
        tvMain.setMovementMethod(LinkMovementMethod.getInstance());
    }
}