package com.s4r1m.stopwatch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView countdownText;

    private TextInputEditText enterText;

    private MaterialButton countdownButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning=false;
    private long timeLeftInMillis =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialButton resetBtn = findViewById(R.id.resetButton);
        countdownButton=findViewById(R.id.countdownButton);
        countdownText=findViewById(R.id.countdownText);
        MaterialButton setTimer = findViewById(R.id.set_timer_btn);
        enterText=findViewById(R.id.enterTime);

        setTimer.setOnClickListener(v -> {
            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }

            if(timerRunning){
                Toast.makeText(getApplicationContext(), "Pause the Timer First ", Toast.LENGTH_SHORT).show();
            }
            else{
                String text =enterText.getText().toString();
                if(text.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Time in Minutes First ",Toast.LENGTH_SHORT).show();
                }
                else{
                    timeLeftInMillis=Long.parseLong(text)*60000;
                    updateTimer();
                    enterText.setText(null);
                }
            }

        });

        updateTimer();

        countdownButton.setOnClickListener(v -> startStop());
        resetBtn.setOnClickListener(v -> resetTimer());
    }

    public void resetTimer(){
        if(timerRunning)
            stopTimer();
        timeLeftInMillis=0;
        updateTimer();
    }
    public void startStop(){
        if(timerRunning){
            stopTimer();
        }
        else{
            startTimer();
        }
    }

    @SuppressLint("SetTextI18n")
    public void startTimer(){
        
        if(timeLeftInMillis!=0){
            countDownTimer=new CountDownTimer(timeLeftInMillis,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis =millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                }
            }.start();
            countdownButton.setText("PAUSE");
            timerRunning=true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Set Timer First", Toast.LENGTH_LONG).show();
        }
        
    }
    @SuppressLint("SetTextI18n")
    public void stopTimer(){
        countDownTimer.cancel();
        countdownButton.setText("START");
        timerRunning=false;
    }

    public void updateTimer(){
        int hrs=(int) timeLeftInMillis/3600000;
        int minutes=(int) timeLeftInMillis%3600000 /60000;
        int seconds= (int) timeLeftInMillis%60000/1000;

        String timeLeftText="";
        if(hrs<10)
            timeLeftText+="0";
        timeLeftText+=hrs+":";
        if(minutes<10)
            timeLeftText+="0";
        timeLeftText+=minutes+":";
        if(seconds<10)
            timeLeftText+="0";
        timeLeftText+=seconds;

        countdownText.setText(timeLeftText);

    }
}