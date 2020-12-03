package com.iiitd.foodlove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        if(intent != null){
            int c = intent.getIntExtra("C", -1);
            System.out.println("INTENT = " + c);
            Toast.makeText(Test.this, c + " == ", Toast.LENGTH_SHORT).show();
        }
    }
}