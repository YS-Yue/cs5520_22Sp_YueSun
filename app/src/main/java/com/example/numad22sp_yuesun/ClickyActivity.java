package com.example.numad22sp_yuesun;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClickyActivity extends AppCompatActivity {
    private String buttonPressed;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicky);

        textView = findViewById(R.id.pressedButtonText);
        String defaultText = "Pressed: -";
        textView.setText(defaultText);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.A:
                buttonPressed = "A";
                break;
            case R.id.B:
                buttonPressed = "B";
                break;
            case R.id.C:
                buttonPressed = "C";
                break;
            case R.id.D:
                buttonPressed = "D";
                break;
            case R.id.E:
                buttonPressed = "E";
                break;
            case R.id.F:
                buttonPressed = "F";
                break;
            default:
                buttonPressed = "-";
        }
        String buttonPressedText = "Pressed: " + buttonPressed;
        textView.setText(buttonPressedText);
    }
}