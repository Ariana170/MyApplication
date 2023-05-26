package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView title, currentTime, totalTime;
    SeekBar seekBar;
    ImageView pausePlay, next, prev, music;

    VideoView video;
    ArrayList<ModelAudio> songsList;
    ModelAudio currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        title = findViewById(R.id.song_title);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
        music = findViewById(R.id.music_icon_player);
        title.setSelected(true);
        songsList =(ArrayList<ModelAudio>) getIntent().getSerializableExtra("LIST");
        video = findViewById(R.id.music_player_video);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video_play;
        Uri uri = Uri.parse(videoPath);
        video.setVideoURI(uri);

        setResourcesWithMusic();
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(convert(mediaPlayer.getCurrentPosition() + ""));


                    if (mediaPlayer.isPlaying()) {
                        pausePlay.setImageResource(R.drawable.baseline_pause_24);
                        music.setRotation(x++);
                        video.start();
                        video.requestFocus();
                        video.setOnPreparedListener (mp -> mp.setLooping(true));

                    }else{
                        pausePlay.setImageResource(R.drawable.baseline_play_arrow_24);
                        music.setRotation(0);
                        x = 0;
                        video.pause();
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        title.setText(currentSong.getTitle());
        totalTime.setText(convert(currentSong.getDuration()));
        pausePlay.setOnClickListener(view-> pausePlay());
        next.setOnClickListener(view-> playNextSong());
        prev.setOnClickListener(view-> playPreviousSong());

        playMusic();
    }

    private void playMusic(){

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void playNextSong(){

        if(MyMediaPlayer.currentIndex == songsList.size()-1)
            return;

        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){

        if(MyMediaPlayer.currentIndex == 0)
            return;

        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void pausePlay(){

        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();

    }

    public static String  convert(String dur){
        Long millis = Long.parseLong(dur);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    }
}