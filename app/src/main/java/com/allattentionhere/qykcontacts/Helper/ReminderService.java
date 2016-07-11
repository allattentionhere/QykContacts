package com.allattentionhere.qykcontacts.Helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.allattentionhere.qykcontacts.Activities.MainActivity;
import com.allattentionhere.qykcontacts.Model.Contacts;
import com.allattentionhere.qykcontacts.R;
import com.google.gson.Gson;

import java.util.Calendar;


public class ReminderService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getExtras().getString("name");
        String id = intent.getExtras().getString("id");
        Contacts mC = new Gson().fromJson(intent.getStringExtra("string_contact"), Contacts.class);

        Intent mainIntent = new Intent(this, MainActivity.class);

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("REMINDER")
                .setContentText("CALL "+name)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
        Calendar cal_alarm = Calendar.getInstance();
        int x=((int) cal_alarm.getTimeInMillis()) % 10000;
        notificationManager.notify(Math.abs(x), noti);

        mC.setIsToBeReminded("false");
        DbHandler.updateContact(id, new Gson().toJson(mC));

        stopSelf();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


}