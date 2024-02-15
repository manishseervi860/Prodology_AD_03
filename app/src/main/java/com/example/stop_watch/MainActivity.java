package com.example.stop_watch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.MessageFormat;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {

    Button start1,reset1,stop1;
    TextView textView;
    int seconds, minutes, milliSeconds;
    long millisecondTime, startTime, timeBuff, updateTime = 0L ;
    Handler handler;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecondTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 100);

            textView.setText(MessageFormat.format("{0}:{1}:{2}", minutes, String.format(Locale.getDefault(), "%02d", seconds), String.format(Locale.getDefault(),"%01d", milliSeconds)));
            handler.postDelayed(this, 0);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start1 = findViewById(R.id.start);
        reset1 = findViewById(R.id.reset);
        stop1 = findViewById(R.id.stop);
        textView = findViewById(R.id.text1);

        handler = new Handler(Looper.getMainLooper());

        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                reset1.setEnabled(false);
                stop1.setEnabled(true);
                start1.setEnabled(false);
            }
        });

        stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += millisecondTime;
                handler.removeCallbacks(runnable);
                reset1.setEnabled(true);
                stop1.setEnabled(false);
                start1.setEnabled(true);
                BlinkAnimation anim = new BlinkAnimation(500); // 500 milliseconds blink duration
                textView.startAnimation(anim);
            }
        });

        reset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                millisecondTime = 0L ;
                startTime = 0L ;
                timeBuff = 0L ;
                updateTime = 0L ;
                seconds = 0 ;
                minutes = 0 ;
                milliSeconds = 0 ;
                textView.setText("00:00:00");
                textView.clearAnimation();
            }
        });

        textView.setText("00:00:00");

    }
    public class BlinkAnimation extends AlphaAnimation {
        public BlinkAnimation(long duration) {
            super(0.0f, 1.0f);
            setDuration(duration);
            setRepeatMode(Animation.REVERSE);
            setRepeatCount(Animation.INFINITE);
        }
    }
}