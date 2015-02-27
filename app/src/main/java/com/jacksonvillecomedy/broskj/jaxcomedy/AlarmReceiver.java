package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kyle on 2/19/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    NotificationCompat.Builder notificationBuilder;
    Intent notificationResultIntent;
    ArrayList<Show> shows;
    SharedPreferences spSpreadsheets, spNotifications;
    final String showSpreadsheetURL = "https://docs.google.com/spreadsheets/d/1Ax2-gUY33i_pRHZIwR8AULy6-nbnAbM8Qm5-CGISevc/gviz/tq";

    public void onReceive(Context context, Intent intent) {
        System.out.println("AlarmReceiver created");
        spNotifications = context.getSharedPreferences("notificationToggle", Context.MODE_PRIVATE);
        spSpreadsheets = context.getSharedPreferences("spreadsheets", Context.MODE_PRIVATE);

        if (spNotifications.getBoolean("notifications", false)) {
            int nextRegularShowIndex = 0, i = 0;

            shows = new ArrayList<>();

            try {
                if (spSpreadsheets.getString("showsSpreadsheet", "").equals(""))
                    getShows(context);
                shows = processShowsJson(new JSONObject(spSpreadsheets.getString("showsSpreadsheet", "")));

                while (shows.get(i).getShowTime() != 0) {
                /*
                checks for first instance of a regular showtime
                 */
                    i++;
                    nextRegularShowIndex = i;
                }
                checkForPastShows();
                notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.applogo)
                        .setContentTitle("Comedy Club of Jacksonville")
                        .setContentText(shows.get(nextRegularShowIndex).getComedian() + " headlines this weekend at the Comedy " +
                                "Club of Jacksonville.  Click to read more.")
                        .setDefaults(Notification.DEFAULT_ALL);
                notificationResultIntent = new Intent(context, ThisWeekend.class).putParcelableArrayListExtra("shows", shows);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(ThisWeekend.class);
                stackBuilder.addNextIntent(notificationResultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(resultPendingIntent);
                notificationBuilder.setAutoCancel(true);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, notificationBuilder.build());
                System.out.println("Notification built");

            } catch (Exception e) {
                e.printStackTrace();
            }//end onReceive
        }
    }

    private ArrayList<Show> processShowsJson(JSONObject object) {
        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int i = 0; i < rows.length(); ++i) {
                JSONObject row = rows.getJSONObject(i);
                JSONArray columns = row.getJSONArray("c");

                String showDate = columns.getJSONObject(0).getString("v");
                int showTime = columns.getJSONObject(1).getInt("v");
                String comedian = columns.getJSONObject(2).getString("v");
                String description = columns.getJSONObject(3).getString("v");
                String videoURL = columns.getJSONObject(4).getString("v");

                Show show = new Show(comedian, description, showDate, showTime, videoURL);
                shows.add(show);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shows;
    }//end processShowsJson

    public void getShows(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                if (shows.isEmpty()) {
                    new DownloadWebpageTask(context, new AsyncResult() {
                        @Override
                        public void onResult(JSONObject object) {
                            processShowsJson(object);
                            SharedPreferences.Editor editor = spSpreadsheets.edit();
                            editor.putString("showsSpreadsheet", object.toString());
                            editor.apply();
                        }
                    }).execute(showSpreadsheetURL);
                }
            } else {
                System.out.println("Network info not available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end getShows

    public void checkForPastShows() {
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date today = new Date();
            today.setTime(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
            for (int i = 0; i < shows.size(); i++) {
                if (today.after(df.parse(shows.get(i).getShowDate()))) {
                    shows.remove(i);
                    for (int j = 0; j < shows.size(); j++) {
                        if (shows.get(i).equals(shows.get(j)) && i != j)
                            shows.remove(j);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
