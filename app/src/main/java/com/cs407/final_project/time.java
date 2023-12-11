package com.cs407.final_project;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class time extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        // Add other initialization code as needed
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) { // 30 seconds countdown
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            public void onFinish() {
                // Handle timer finish logic, e.g., show a message or perform an action
                timerTextView.setText("00:00:00");
            }
        }.start();
    }

    private void updateTimerText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;

        String time = String.format("%02d:%02d:%02d",
                hours % 24,
                minutes % 60,
                seconds % 60);

        timerTextView.setText(time);
    }

    // Add methods for pause, resume, reset, etc., if needed

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
