package com.cs407.final_project;
// TimerHelper.java
import android.os.CountDownTimer;

public class TimerHelper {

    private CountDownTimer countDownTimer;
    private TimerCallback timerCallback;

    public interface TimerCallback {
        void onTimerTick(long millisUntilFinished);
        void onTimerFinish();
    }

    public TimerHelper(TimerCallback callback) {
        this.timerCallback = callback;
    }

    public void startTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                timerCallback.onTimerTick(millisUntilFinished);
            }

            public void onFinish() {
                timerCallback.onTimerFinish();
            }
        }.start();
    }

    public void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}


