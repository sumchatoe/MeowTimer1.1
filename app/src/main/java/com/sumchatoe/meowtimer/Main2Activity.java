package com.sumchatoe.meowtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    int quantity;
    long roundMills;
    long pauseTimeRemaining;
    int count = 0;
    int round;
    boolean isPaused;
    private SoundPool soundPool;
    private int mCatSound;
    private int mCatSound2;
    private ImageView cat;
    private CountDownTimer countDownTimer;
    private TextView data;
    private TextView roundNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // getIntent():Return the intent that started this activity.
        Intent in = getIntent();
        //Getting bundle
        Bundle b = in.getBundleExtra("b");

        //Getting data from bundle
        roundMills = b.getLong("roundmills");
        quantity = b.getInt("quantity");

        if (savedInstanceState != null) {
            count = savedInstanceState.getInt("count");
            pauseTimeRemaining = savedInstanceState.getLong("pauseTimeRemaining");
            isPaused = savedInstanceState.getBoolean("isPaused");
        }

        // Связываем кнопку громкости с нашим приложением
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Создаем soundPool
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        //устанавливаем call-back функцию, которая вызывается по
        //завершению загрузки файла в память
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.e("Test", "sampleId=" + sampleId + " status=" + status);
            }
        });

        //Загружаем звуки в память
        mCatSound = soundPool.load(this, R.raw.meow, 1);
        mCatSound2 = soundPool.load(this, R.raw.purr, 1);

        // Getting the user sound settings
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        data = findViewById(R.id.data);
        roundNumber = findViewById(R.id.round);
        cat = findViewById(R.id.cat2);

        //Cancel and start timer.
        if (savedInstanceState != null) {
            if (pauseTimeRemaining == 0) {
                nullInstanceState();
            } else {
                onTimerResume();
            }
        }
        else {
            if (roundMills == 0){
                nullInstanceState();
            }else  startTimer();
        }
    }

    private void startTimer() {
        //create countdown timer with 1 sec step
        countDownTimer = new CountDownTimer(roundMills, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                //display time left
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                data.setText(timeLeftFormatted);
                round = count + 1;
                roundNumber.setText(getResources().getString(R.string.round) + " " + round);
                pauseTimeRemaining = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                count++;
                //check if count still less than repeat number
                if (count < quantity) {
                    soundPool.play(mCatSound, 1, 1, 1, 0, 1);
                    data.setText("00:00");
                    countDownTimer.start();
                } else {
                    cat.setVisibility(View.VISIBLE);
                    soundPool.play(mCatSound, 1, 1, 1, 2, 1);
                    data.setText("00:00");
                    countDownTimer.cancel();
                    countDownTimer = null;
                    Button buttonPause = findViewById(R.id.pause);
                    buttonPause.setVisibility(View.INVISIBLE);
                    Button buttonResume = findViewById(R.id.resume);
                    buttonResume.setVisibility(View.INVISIBLE);
                    count = 0;
                    pauseTimeRemaining = 0;
                }
            }
        }.start();
    }

    public void onClickPause(View v) {
        countDownTimer.cancel();
        countDownTimer = null;
        Button buttonPause = findViewById(R.id.pause);
        buttonPause.setVisibility(View.INVISIBLE);
        Button buttonResume = findViewById(R.id.resume);
        buttonResume.setVisibility(View.VISIBLE);
    }

    public void onClickResume(View v) {
        onTimerResume();
    }

    public void onTimerResume(){
        Button buttonPause = findViewById(R.id.pause);
        buttonPause.setVisibility(View.VISIBLE);
        Button buttonResume = findViewById(R.id.resume);
        buttonResume.setVisibility(View.INVISIBLE);
        final TextView data = findViewById(R.id.data);
        if (countDownTimer == null) {//recreate with saved time
            countDownTimer = new CountDownTimer(pauseTimeRemaining, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int minutes = (int) (millisUntilFinished / 1000) / 60;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;

                    //display time left
                    String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    data.setText(timeLeftFormatted);
                    round = count + 1;
                    roundNumber.setText(getResources().getString(R.string.round) + " " + round);
                    pauseTimeRemaining = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    data.setText("00:00");
                    cat = findViewById(R.id.cat2);
                    count++;
                    //check if count still less than repeat number
                    if (count < quantity) {
                        soundPool.play(mCatSound, 1, 1, 1, 0, 1);
                        startTimer();
                    } else {
                        cat.setVisibility(View.VISIBLE);
                        soundPool.play(mCatSound, 1, 1, 1, 2, 1);
                        countDownTimer.cancel();
                        Button buttonPause = findViewById(R.id.pause);
                        buttonPause.setVisibility(View.INVISIBLE);
                        Button buttonResume = findViewById(R.id.resume);
                        buttonResume.setVisibility(View.INVISIBLE);
                        count = 0;
                        pauseTimeRemaining = 0;
                    }
                }
            }.start();
        }
    }

    public void onClickCancel(View v){
        if (countDownTimer == null){
            finish();
        }
        else {
            countDownTimer.cancel();
            finish();
            /*это решение вызывало ошибку
            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(intent);*/
        }
    }

    public void onClickPurr(View v){
        soundPool.play(mCatSound2, 1, 1, 1, 0, 1);
    }

    public void nullInstanceState(){
        data.setText("00:00");
        pauseTimeRemaining = 0;
        count = 0;
        Button buttonPause = findViewById(R.id.pause);
        buttonPause.setVisibility(View.INVISIBLE);
        Button buttonResume = findViewById(R.id.resume);
        buttonResume.setVisibility(View.INVISIBLE);
        cat.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (countDownTimer == null) {
            if (pauseTimeRemaining !=0) {
                savedInstanceState.putLong("pauseTimeRemaining", pauseTimeRemaining);
                savedInstanceState.putInt("count", count);
            } else {
                pauseTimeRemaining = 0;
                savedInstanceState.putLong("pauseTimeRemaining", pauseTimeRemaining);
            }
        }
        else {
            savedInstanceState.putLong("pauseTimeRemaining", pauseTimeRemaining);
            savedInstanceState.putInt("count", count);
            countDownTimer.cancel();
        }
    }
}


