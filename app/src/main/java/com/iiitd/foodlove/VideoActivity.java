package com.iiitd.foodlove;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private ImageButton btn_close;
    private VideoView videoView;
    private String video = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        btn_close = findViewById(R.id.btn_close);
        videoView = findViewById(R.id.vv);

        Intent intent = getIntent();
        if(intent != null){
            video = intent.getStringExtra(Constants.HERO);
            System.out.println("Video = " + video);
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                finishActivity();
            }
        });

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        String videoName = "";
        if(video.equals(getResources().getString(R.string.Spiderman))){
            videoName = "spiderman";
        }
        else if(video.equals(getResources().getString(R.string.Superman))){
            videoName = "superman";
        }
        else if(video.equals(getResources().getString(R.string.Ironman))){
            videoName = "ironman";
        }
        else if(video.equals(getResources().getString(R.string.Flash))){
            videoName = "flash";
        }
        else if(video.equals(getResources().getString(R.string.Swallow))){
            videoName = "swallow";
        }
        else if(video.equals(getResources().getString(R.string.Chewfast))){
            videoName = "chewing fast";
        }
        else{
            videoName = "null";
            System.out.println("Video not available.");
        }

        String rootPath = Constants.getAppPath();
        String videoPath = rootPath + "/" + videoName + ".mp4";
        Uri uri = null;
        try{
            uri = Uri.parse(videoPath);
        }catch (Exception exception){
            System.out.println("Exception = " + exception.getMessage());
//            finish();
            finishActivity();
        }

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.start();
            }
        }, 500);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
//                finishActivity();
            }
        });

    }

    private void finishActivity(){
        Toast.makeText(VideoActivity.this, "FINISHED" , Toast.LENGTH_SHORT).show();
        AutomaticService mSensorService = new AutomaticService(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
        stopService(mServiceIntent);
        System.out.println("Called from video");
        finish();
    }

}