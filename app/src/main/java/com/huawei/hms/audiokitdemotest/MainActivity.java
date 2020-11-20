package com.huawei.hms.audiokitdemotest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.callback.HwAudioConfigCallBack;
import com.huawei.hms.audiokit.player.manager.HwAudioManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManagerFactory;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerConfig;

import com.huawei.hms.audiokit.player.manager.HwAudioPlayerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private HwAudioManager mHwAudioManager;

    private HwAudioPlayerManager mHwAudioPlayerManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(MainActivity.this);
        Button playBtn = findViewById(R.id.play);
        Button pauseBtn = findViewById(R.id.pause);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHwAudioPlayerManager != null) {
                    mHwAudioPlayerManager.play();
                }
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHwAudioPlayerManager != null) {
                    mHwAudioPlayerManager.pause();
                }
            }
        });
    }

    /**
     * init sdk
     * @param context context
     */
    @SuppressLint("StaticFieldLeak")
    public void init(final Context context) {
        Log.i(TAG, "init start");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                HwAudioPlayerConfig hwAudioPlayerConfig = new HwAudioPlayerConfig(context);
                HwAudioManagerFactory.createHwAudioManager(hwAudioPlayerConfig, new HwAudioConfigCallBack() {
                    @Override
                    public void onSuccess(HwAudioManager hwAudioManager) {
                        try {
                            Log.i(TAG, "createHwAudioManager onSuccess");
                            mHwAudioManager = hwAudioManager;
                            mHwAudioPlayerManager = hwAudioManager.getPlayerManager();
                            mHwAudioPlayerManager.playList(getOnlinePlaylist(), 0, 0);
                        } catch (Exception e) {
                            Log.e(TAG, "player init fail", e);
                        }
                    }
                    @Override
                    public void onError(int errorCode) {
                        Log.e(TAG, "init err:" + errorCode);
                    }
                });
                return null;
            }
        }.execute();
    }

    /**
     * get online PlayList
     *
     * @return play list
     */
    public List<HwAudioPlayItem> getOnlinePlaylist() {
        List<HwAudioPlayItem> playItemList = new ArrayList<>();

        /*Read Audio from Online Path*/
        HwAudioPlayItemBeta audio1=new HwAudioPlayItemBeta();
        audio1.setAudioId("1001");
        audio1.setSinger("singer name audio1");
        audio1.setOnlinePath("https://archive.org/download/PodcastTest_201508/podcast%20test.mp3");
        audio1.setOnline(1);
        audio1.setAudioTitle("title audio1");
        playItemList.add(audio1);


        /*Read Audio from resource folder*/
        HwAudioPlayItemBeta audio2=new HwAudioPlayItemBeta();
        audio2.setAudioId("1002");
        audio2.setSinger("singer name audio2");
        audio2.setResourcePath(this,R.raw.audio, HwAudioPlayItemBeta.Ext.MP3);
        audio2.setAudioTitle("title audio2");
        playItemList.add(audio2);

        /*Read Audio from local path*/
        HwAudioPlayItemBeta audio3=new HwAudioPlayItemBeta();
        audio3.setAudioId("1003");
        audio3.setSinger("singer name audio3");
        audio3.setFilePath("storage/emulated/0/download/audio.mp3");
        audio3.setAudioTitle("title audio3");
        playItemList.add(audio3);

        return playItemList;
    }
}
