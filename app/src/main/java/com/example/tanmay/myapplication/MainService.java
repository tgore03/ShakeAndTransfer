package com.example.tanmay.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.*;
import android.os.Process;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Tanmay on 10-02-2015.
 */
public class MainService extends NotificationListenerService {
    public String packName;
    NLServiceReceiver nlServiceReceiver;
    IntentFilter intentFilter;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("msg","msg-onCreate Service");

        nlServiceReceiver = new NLServiceReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.tanmay.myapplication.NOTIFICATION_LISTENER");
        registerReceiver(nlServiceReceiver,intentFilter);

        Log.i("msg","msg- registerReceiver");
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("msg","msg-onNotificationPosted before super");
        //super.onNotificationPosted(sbn);
        Log.i("msg","msg-onNotificationPosted after super");

        Toast.makeText(getApplicationContext(),"Notification posted",LENGTH_SHORT).show();
        Log.i("msg","msg-notification posted");

        Bundle extra = sbn.getNotification().extras;
        String title = extra.getString("android.title");
        System.out.println("title = "+title);
        if(title == "Screenshot captured.") {
            Toast.makeText(getApplicationContext(),"Notification created",Toast.LENGTH_LONG).show();
            packName = sbn.getPackageName();
            createNotification();

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    public void createNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.abc_ic_menu_cut_mtrl_alpha);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText("Tap to send the application!");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        //set up parent intent for second activity
        Intent intent1 = new Intent(this,Second.class);
        intent1.putExtra("packName",packName);

        // Create a task stack builder to manage the back stack:
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add all parents of SecondActivity to the stack:
        stackBuilder.addParentStack(Second.class);

        // Push the intent that starts SecondActivity onto the stack:
        stackBuilder.addNextIntent(intent1);



        // Create a PendingIntent; we're only using one PendingIntent (ID = 0):
        int pendingIntentId = 0;

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(pendingIntentId,PendingIntent.FLAG_ONE_SHOT);



        //setup pending intent with notification
        mBuilder.setContentIntent(pendingIntent);

        // Build the notification:
        Notification  notification = mBuilder.build();

        // Get the notification manager:
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Publish the notification:
        int notificationId = 0;
        notificationManager.notify(notificationId,notification);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlServiceReceiver);
        Log.i("msg","msg-onDestroy");
    }


    class NLServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("msg","msg-onReceive");
        }
    }
}
