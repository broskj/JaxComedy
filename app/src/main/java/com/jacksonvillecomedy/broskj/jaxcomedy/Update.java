package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

/**
 * Created by Kyle on 2/23/2015.
 */
public class Update extends BroadcastReceiver {

    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    SharedPreferences spSpreadsheets;
    SharedPreferences.Editor editor;
    final String showSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1Ax2-gUY33i_pRHZIwR8AULy6-nbnAbM8Qm5-CGISevc/gviz/tq",
            dealsSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1dnpODnbz6ME4RY5vNwrtAc6as3-uj2rK_IgtYszsvsM/gviz/tq";

    @Override
    public void onReceive(Context context, Intent intent) {
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
                        editor = spSpreadsheets.edit();
                        editor.putString("showsSpreadsheet", object.toString());
                        editor.apply();
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
                        editor = spSpreadsheets.edit();
                        editor.putString("dealsSpreadsheet", object.toString());
                        editor.apply();
                    }
                }).execute(dealsSpreadsheetURL);
            } else {
                System.out.println("Network info not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end getDeals
}
