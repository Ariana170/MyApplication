package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<ModelAudio> songsList = new ArrayList<>();
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.home);


        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);

        if(checkPermission() == false){
            requestPermission();
            return;
        }


        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor =getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);

        while (cursor.moveToNext()){
            ModelAudio songData = new ModelAudio(cursor.getString(1), cursor.getString(0),cursor.getString(2));
            if (new File(songData.getPath()).exists())
                songsList.add(songData);
        }

        if (songsList.size() == 0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else{
            //recyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        }

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.home:
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileC.class));
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_right);
                    finish();
                    return true;
                case R.id.mVideos:
                    startActivity(new Intent(getApplicationContext(),Music_Videos.class));
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_right);
                    finish();
                    return true;
            }
            return false;
        });


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }



    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest
                .permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;

        }else{
            return false;
        }

    }

    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS", Toast.LENGTH_SHORT);
        }else{

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }

    }

    public void logout(View view){
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();

        moveToLogin();
    }

    private void moveToLogin(){
        Intent intent = new Intent(MainActivity.this, ProfileC.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    protected void onResume(){
        super.onResume();
        if(recyclerView!=null)
            recyclerView.setAdapter(new MusicListAdapter(songsList,getApplicationContext()));
    }
}