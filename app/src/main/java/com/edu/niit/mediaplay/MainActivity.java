package com.edu.niit.mediaplay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
//测试
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MediaPlayer mediaPlayer;
    Button btn_paly, btn_pause, btn_stop;
    SeekBar seekBar;
    Runnable runnable;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_paly = (Button) findViewById(R.id.btn_paly);
        btn_paly.setOnClickListener(this);

        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(this);

        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(this);

        findViewById(R.id.start).setOnClickListener(this);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    //从当前进度开始播放
                    mediaPlayer.seekTo(progress);
                }
            }
        });
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        //mediaPlayer.getDuration()获取音频文件的总长度
        seekBar.setMax(mediaPlayer.getDuration());

        runnable = new Runnable() {
            @Override
            public void run() {
                // 获得歌曲现在播放位置并设置成播放进度条的值
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                // 每次延迟100毫秒再启动线程
                handler.postDelayed(runnable, 100);
            }
        };
        /**
         * 设置循环播放
         */
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_paly:
                try {
                    mediaPlayer.start();
                    handler.post(runnable);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                toast("音乐开始播放。。。");
                btn_paly.setEnabled(false);
                btn_pause.setEnabled(true);
                btn_stop.setEnabled(true);
                break;
            case R.id.btn_pause:
                mediaPlayer.pause();
                btn_paly.setEnabled(true);
                btn_pause.setEnabled(false);
                btn_stop.setEnabled(true);
                toast("音乐已经暂停。。。");
                break;
            case R.id.btn_stop:
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btn_paly.setEnabled(true);
                btn_pause.setEnabled(false);
                btn_stop.setEnabled(false);
                handler.removeCallbacks(runnable);
                toast("音乐已经停止。。。");
                break;
            case R.id.start:
                startActivity(new Intent(MainActivity.this, Video.class));
                break;
        }
    }
    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
