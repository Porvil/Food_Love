package com.iiitd.foodlove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.vv);

        Intent intent = getIntent();
        if(intent != null){
            int c = intent.getIntExtra("C", -1);
            String s = intent.getStringExtra("H");
            System.out.println("INTENT = " + c);
//            Toast.makeText(MainActivity.this, c + " == ", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, s + " == ", Toast.LENGTH_SHORT).show();
        }

        //Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
        System.out.println(Environment.getExternalStorageDirectory().getPath());
        Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/video.mp4");

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MainActivity.this, "FINISHED" , Toast.LENGTH_SHORT).show();
                AutomaticService mSensorService = new AutomaticService(getApplicationContext());
                Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
                stopService(mServiceIntent);
                System.out.println("Called from video");
                finish();
            }
        });
    }
}