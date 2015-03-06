package com.jacksonvillecomedy.broskj.jaxcomedy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Kyle on 3/5/2015.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.WEDNESDAY);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 8);
        calendar.set(java.util.Calendar.MINUTE, 15);

        /*
        sets alarm manager to go off at 8:15 in the morning every 7 days on Wednesday
         */
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 * 7, pendingIntent);
    }
}
