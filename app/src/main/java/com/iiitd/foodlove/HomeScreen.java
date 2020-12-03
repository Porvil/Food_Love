package com.iiitd.foodlove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class HomeScreen extends AppCompatActivity {

    ServiceRestartAutomaticReceiver receiver;
    Button start;
    Button stop;
    RadioButton spider;
    RadioButton superm;
    RadioButton iron;
    RadioButton flash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        start = findViewById(R.id.btn_start);
        start = findViewById(R.id.btn_start);
        spider = findViewById(R.id.rb_spider);
        superm = findViewById(R.id.rb_super);
        iron = findViewById(R.id.rb_iron);
        flash = findViewById(R.id.rb_flash);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.iiitd.foodlove.restartService");

        receiver = new ServiceRestartAutomaticReceiver();
        registerReceiver(receiver, filter);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, MainActivity.class));
            }
        });


        AutomaticService mSensorService = new AutomaticService(getApplicationContext());
        Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            ContextCompat.startForegroundService(getApplicationContext(), mServiceIntent);
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d ("TAG", true+"");
                return true;
            }
        }
        Log.i ("TAG", false+"");
        return false;
    }
}