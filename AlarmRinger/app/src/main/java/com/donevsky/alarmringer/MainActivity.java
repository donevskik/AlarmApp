package com.donevsky.alarmringer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDatePicker = findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("alarmTimeInMillis", String.valueOf(c.getTimeInMillis()));
        int requestCode = (int) (c.getTimeInMillis()%2147483640);
        // needs to be something more unique because there is a chance every 25 days to generated the same requestCode this way
        // maybe alarmId (made sure every user has different alarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Log.i("MainActivity", "Alarm set for "+ c.getTimeInMillis());
        Log.i("MainActivity", "Casted " + (int) (c.getTimeInMillis()%2147483640));

        FirebaseDatabase.getInstance().getReference().child("Alarms")
                .child(String.valueOf(c.getTimeInMillis())).setValue("false");
    }


    private void updateTimeText(Calendar c) {
        TextView textView = findViewById(R.id.tvAlarmSetFor);
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        textView.setText(timeText);
    }
}
