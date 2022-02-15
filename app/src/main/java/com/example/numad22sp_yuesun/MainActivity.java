package com.example.numad22sp_yuesun;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.numad22sp_yuesun.link_collector.LinkCollectorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aboutMeButton:
                Intent intentAboutMe = new Intent(this, AboutMeActivity.class);
                startActivity(intentAboutMe);
                break;
            case R.id.clickyButton:
                Intent intentClicky = new Intent(this, ClickyActivity.class);
                startActivity(intentClicky);
                break;
            case R.id.linkCollectorButton:
                Intent intentLinkCollector = new Intent(this, LinkCollectorActivity.class);
                startActivity(intentLinkCollector);
                break;
        }
    }
}