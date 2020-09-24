package com.donevsky.alarmringer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import com.google.firebase.database.FirebaseDatabase;

public class AlertReceiver extends BroadcastReceiver {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.start();
        database.getReference().child(String.valueOf(intent.getExtras().getLong("alarmTimeInMillis"))).setValue("true");
        Log.i("RECIEVER", "ring ring");
    }
}
