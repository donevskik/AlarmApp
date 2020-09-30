package com.donevsky.alarmstopper;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private TextView tvAlarmSetFor;
    private Button btnDisableAlarm;
    private String alarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAlarmSetFor = findViewById(R.id.tvAlarmSetFor);
        btnDisableAlarm = findViewById(R.id.btnDisableAlarm);

        FirebaseDatabase.getInstance().getReference().child("Alarms").addChildEventListener(childEventListenerAlarms);

        btnDisableAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Alarms").child(alarmTime).setValue("false");
                btnDisableAlarm.setVisibility(View.INVISIBLE);
            }
        });
    }

    private ChildEventListener childEventListenerAlarms = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
            // add to recycler view new alarm that added
        }

        @Override
        public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
            // Alarm ringing?
            // Later on call another activity
            if (snapshot.getValue().equals("true")){
                btnDisableAlarm.setVisibility(View.VISIBLE);
                alarmTime = snapshot.getKey();
            }

        }

        @Override
        public void onChildRemoved(DataSnapshot snapshot) {
            // remove from recyler view alarm thats removed
        }

        @Override
        public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    };

}