package com.example.numad22sp_yuesun;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutMeButton:
                showAboutMe(view);
                break;
            case R.id.clickyButton:
                Intent intent_clicky = new Intent(this, ClickyActivity.class);
                startActivity(intent_clicky);
                break;
        }
    }
}