package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Music_Videos extends AppCompatActivity {

    Button notifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_videos);

        notifyBtn = findViewById(R.id.notif);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(Music_Videos.this,"My Notification");
                builder.setContentTitle("My notification");
                builder.setContentText("This notification takes you to your song library");
                builder.setSmallIcon(R.drawable.musical_note);


                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_ONE_SHOT);

                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Music_Videos.this);
                if(checkPermission() == false){
                    requestPermission();
                    return;
                }
                managerCompat.notify(1,builder.build());
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_right);
                    finish();
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileC.class));
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_right);
                    finish();
                    return true;
                case R.id.mVideos:
                    return true;
            }
            return false;
        });
    }
    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(Music_Videos.this, android.Manifest
                .permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;

        }else{
            return false;
        }

    }

    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(Music_Videos.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(Music_Videos.this,"READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS", Toast.LENGTH_SHORT);
        }else{

            ActivityCompat.requestPermissions(Music_Videos.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }

    }
}