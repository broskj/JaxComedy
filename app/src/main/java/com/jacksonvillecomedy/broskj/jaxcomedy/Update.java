package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.OutputStreamWriter;

/**
 * Created by Kyle on 2/23/2015.
 */
public class Update extends BroadcastReceiver {

    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    final String showsFileName = "shows.txt",
            dealsFileName = "deals.txt",
            showSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1Ax2-gUY33i_pRHZIwR8AULy6-nbnAbM8Qm5-CGISevc/gviz/tq",
            dealsSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1dnpODnbz6ME4RY5vNwrtAc6as3-uj2rK_IgtYszsvsM/gviz/tq";
    OutputStreamWriter osw;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Update onReceive entered");
        this.context = context;
        getShows(context);
        getDeals(context);
    }//end onReceive


    public void getShows(Context context) {
        connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask(context, new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        saveToFile(object.toString(), showsFileName);
                    }
                }).execute(showSpreadsheetURL);
            } else {
                System.out.println("Network info not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end getShows

    public void getDeals(Context context) {
        connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask(context, new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        saveToFile(object.toString(), dealsFileName);
                    }
                }).execute(dealsSpreadsheetURL);
            } else {
                System.out.println("Network info not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end getDeals

    void saveToFile(String s, String filename) {
        try {
            osw = new OutputStreamWriter(context.getApplicationContext()
                    .openFileOutput(filename, Context.MODE_PRIVATE));
            osw.write(s);
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end saveToFile
}
