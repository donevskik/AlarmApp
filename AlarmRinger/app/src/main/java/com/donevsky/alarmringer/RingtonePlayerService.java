package com.donevsky.alarmringer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RingtonePlayerService extends Service {
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        String alarmTime = intent.getExtras().getString("alarmTimeInMillis");

        // media.create and start
        mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.alarm);
        mediaPlayer.start();

        // set db listener for change
        // when changed to false -> stopSelf()
        FirebaseDatabase.getInstance().getReference().child("Alarms")
                .child(alarmTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue().equals("false")){
                    stopSelf(startId);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        // also send off notification
        // which when clicked sends intent to new AlarmRingingActivity/Fragment?
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // media.stop()
        mediaPlayer.stop();
        mediaPlayer.reset();
    }
}
