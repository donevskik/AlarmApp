package com.donevsky.alarmringer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import com.google.firebase.database.FirebaseDatabase;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmTime = intent.getExtras().getString("alarmTimeInMillis");

        FirebaseDatabase.getInstance().getReference().child("Alarms")
                .child(alarmTime)
                .setValue("true");

        Log.i("Receiver", alarmTime);

        //start service
        Intent serviceIntent = new Intent(context, RingtonePlayerService.class);
        serviceIntent.putExtra("alarmTimeInMillis", alarmTime);
        // later on when i want to put playlist choice i put in intent extras int id of R.raw
        // and put in serviceIntent extras the same int id
        context.startService(serviceIntent);

   }
}
