package com.example.numad22sp_yuesun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showAboutMe(View view) {
        Resources res = getResources();
        String text = res.getString(R.string.name) + "\n" + res.getString(R.string.email);
        Toast toast = Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT);
        toast.show();
    }
}