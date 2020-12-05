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
import android.widget.RadioGroup;

public class HomeScreen extends AppCompatActivity {

    ServiceRestartAutomaticReceiver receiver;
    Button start;
    Button stop;
    RadioButton spider;
    RadioButton superm;
    RadioButton iron;
    RadioButton flash;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        start = findViewById(R.id.btn_start);
        stop = findViewById(R.id.btn_stop);
        spider = findViewById(R.id.rb_spider);
        superm = findViewById(R.id.rb_super);
        iron = findViewById(R.id.rb_iron);
        flash = findViewById(R.id.rb_flash);
        radioGroup = findViewById(R.id.rg);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.iiitd.foodlove.restartService");

        receiver = new ServiceRestartAutomaticReceiver();
        registerReceiver(receiver, filter);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton viewById = radioGroup.findViewById(checkedRadioButtonId);

                System.out.println(viewById.getText().toString());
                String s = viewById.getText().toString();
                AutomaticService mSensorService = new AutomaticService(getApplicationContext());
//                mSensorService.hero =
                Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
                mServiceIntent.putExtra("H" , s);
                if (!isMyServiceRunning(mSensorService.getClass())) {
                    ContextCompat.startForegroundService(getApplicationContext(), mServiceIntent);
                }
                else{
                    stopService(mServiceIntent);
                    System.out.println("Tried to stop");
                }
//                startActivity(new Intent(HomeScreen.this, MainActivity.class));
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("STOP");
            }
        });

//        AutomaticService mSensorService = new AutomaticService(getApplicationContext());
//
//        Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
//        if (!isMyServiceRunning(mSensorService.getClass())) {
//            ContextCompat.startForegroundService(getApplicationContext(), mServiceIntent);
//        }

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