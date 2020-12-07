package com.iiitd.foodlove;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class AutomaticService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private Context context = null;
    private int counter = 0;
    public String hero;
    public int timeInterval = 20000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public AutomaticService(Context appContext){
        super();
        context = appContext;
        Log.d("TAG", "Automatic Service Constructor Called");
    }

    public AutomaticService(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){
            hero = intent.getStringExtra(Constants.HERO);
            timeInterval = intent.getIntExtra(Constants.TIME, 20);
            timeInterval *= 1000;
            System.out.println("hero ===== " + hero);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("TAG", "on destroy!");

//        Intent broadcastIntent = new Intent("com.iiitd.foodlove.restartService");
//        sendBroadcast(broadcastIntent);
        stopTimerTask();
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, Constants.TIME_DELAY, timeInterval);
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.d("TAG", "in timer ++++  " + (counter++));
                System.out.println("PLAY VIDEO NOW");
                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.HERO, hero);
                startActivity(intent);
            }
        };
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.iiitd.foodlove";
        String channelName = "Automatic Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Meal Habits is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

}

