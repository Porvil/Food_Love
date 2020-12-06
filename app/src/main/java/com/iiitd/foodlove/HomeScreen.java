package com.iiitd.foodlove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;

public class HomeScreen extends AppCompatActivity {

    private String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    ServiceRestartAutomaticReceiver receiver;
    View view;
    Button btn_start;
    Button btn_stop;
    RadioButton spiderman;
    RadioButton superman;
    RadioButton ironman;
    RadioButton flash;
    RadioButton swallow;
    RadioButton chewfast;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        view = findViewById(R.id.view);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        radioGroup = findViewById(R.id.rg);
        spiderman = findViewById(R.id.rb_spider);
        superman = findViewById(R.id.rb_super);
        ironman = findViewById(R.id.rb_iron);
        flash = findViewById(R.id.rb_flash);
        swallow = findViewById(R.id.rb_swallow);
        chewfast = findViewById(R.id.rb_chewfast);

        checkPermissions();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.iiitd.foodlove.restartService");

        receiver = new ServiceRestartAutomaticReceiver();
        registerReceiver(receiver, filter);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton viewById = radioGroup.findViewById(checkedRadioButtonId);

                String hero = viewById.getText().toString();
                System.out.println(hero);

                AutomaticService mSensorService = new AutomaticService(getApplicationContext());
                Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
                mServiceIntent.putExtra(Constants.HERO , hero);
                if (!isMyServiceRunning(mSensorService.getClass())) {
                    ContextCompat.startForegroundService(getApplicationContext(), mServiceIntent);
                }
                else{
                    stopService(mServiceIntent);
                    System.out.println("Tried to stop");
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("STOP using button");
                AutomaticService mSensorService = new AutomaticService(getApplicationContext());
                Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
                stopService(mServiceIntent);
            }
        });

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

    private void checkPermissions(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!hasPermissions(PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.PERMISSION_ALL);
            }
        }
    }

    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.PERMISSION_ALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(view, "Permissions Granted.",
                        Snackbar.LENGTH_SHORT).show();
            } else {
                final Snackbar snackbar = Snackbar.make(view, "App won't work without Read/Write Permissions.",
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Give Permissions", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermissions();
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}