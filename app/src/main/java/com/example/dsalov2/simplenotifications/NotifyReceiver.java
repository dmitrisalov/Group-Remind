package com.example.dsalov2.simplenotifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by dsalov2 on 4/11/2017.
 */

public class NotifyReceiver extends BroadcastReceiver {
    private final int DEFAULT = 0;
    private String details;

    @Override
    public void onReceive(Context context, Intent intent) {
        details = intent.getStringExtra("details");
        createNotification(context);
    }

    /**
     * Creates a notification and displays it.
     *
     * @param context
     */
    private void createNotification(Context context) {

        //When the notification is clicked, the user is taken to MainActivity.
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent notificationIntent =
                PendingIntent.getActivity(context, DEFAULT, intent, DEFAULT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        setNotificationDetails(notificationBuilder);
        notificationBuilder.setContentIntent(notificationIntent);

        //Creating a manager to display the notification.
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Displaying the notification.
        notificationManager.notify(DEFAULT, notificationBuilder.build());
    }

    /**
     * Sets the required variables for the notification.
     *
     * @param notificationBuilder
     */
    private void setNotificationDetails(NotificationCompat.Builder notificationBuilder) {
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle("Reminder");
        notificationBuilder.setContentText(details);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setAutoCancel(true);
    }
}
