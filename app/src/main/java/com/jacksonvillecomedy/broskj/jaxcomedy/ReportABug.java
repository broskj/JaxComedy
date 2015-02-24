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
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kyle on 2/16/2015.
 */
public class ReportABug extends Activity {

    int screenWidth, screenHeight;
    EditText etName, etDescription, etSteps;
    String sName, sDescription, sSteps;
    String[] email = {"JaxComedy@gmail.com"};

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_a_bug);
        System.out.println("report_a_bug created");

        declarations();
    }//end onCreate

    public void declarations() {
        ActivityManager manager = new ActivityManager(this);
        manager.manageActionBar(getActionBar());
        manager.scaleBackground((RelativeLayout) findViewById(R.id.rlReportABug), R.drawable.background);

        etName = (EditText) findViewById(R.id.etBugReportName);
        etName.requestFocus();
        etDescription = (EditText) findViewById(R.id.etBugReportDescription);
        etDescription.setMovementMethod(new ScrollingMovementMethod());
        etSteps = (EditText) findViewById(R.id.etReportBugSteps);
        etSteps.setMovementMethod(new ScrollingMovementMethod());
    }//end declarations

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

    private void convertEditTextToString() {
        // TODO Auto-generated method stub
        sName = etName.getText().toString();
        sDescription = etDescription.getText().toString();
        sSteps = etSteps.getText().toString();
    }//end convertEditTextToString

    public void onSubmitReport(View view) {
        convertEditTextToString();

        if (sName.matches("")) {
            etName.requestFocus();
            etName.setError("Must enter name.");
        } else if (sDescription.matches("")) {
            etDescription.requestFocus();
            etDescription.setError("Must enter description.");
        } else if (sSteps.matches("")) {
            etSteps.requestFocus();
            etDescription.setError("Must enter steps.");
        } else {
            SimpleDateFormat dateformat = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            Date date = new Date();
            String message = sDescription + "\n\n" + sSteps + "\n\n" + dateformat.format(date);
            String subject = "Bug Report from " + sName;

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(emailIntent);
            this.finish();
        }
    }//end onSubmitReport
}
