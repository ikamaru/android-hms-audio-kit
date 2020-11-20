package com.huawei.hms.audiokitdemotest;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.huawei.hms.api.bean.HwAudioPlayItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HwAudioPlayItemBeta extends HwAudioPlayItem{

    private static final String TAG = "Ringtone";

    public static enum Ext {
        MP3(".mp3"), M4A(".m4a"), AAC(".aac"), AMR(".amr"), IMY(".imy"), WAV(".wav"), OGG(".ogg"), RTTTL(".rtttl");
        private String ext;
        private Ext(String ext) { this.ext = ext; }
        @Override
        public String toString(){ return ext; }
    }

    private String resourcePath;
    public void setResourcePath(Activity activity, int resource,Ext ext){
        //get the path of a cache folder /storage/emulated/0/.[PACKAGE_NAME]
        String dir=Environment.getExternalStorageDirectory()+File.separator+"."+activity.getPackageName();
       //get the path where to save the file /storage/emulated/0/.[PACKAGE_NAME]/[RESOURCE_ID].[EXT]
        this.resourcePath=dir+File.separator+resource+ext.toString();
        //read the audio from resource folder and put it in the external storage
        try {
            File directory = new File(dir);
            if (directory.mkdirs() || directory.isDirectory()) {
                InputStream in = activity.getResources().openRawResource(resource);
                FileOutputStream out = new FileOutputStream(this.resourcePath);
                byte[] buff = new byte[1024];
                int read = 0;
                try {
                    while ((read = in.read(buff)) > 0) {
                        out.write(buff, 0, read);
                    }
                } finally {
                    in.close();
                    out.close();
                }
                //set the file from
                this.setFilePath(this.resourcePath);
            }
        }catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "setResourcePath (IOException): "+e.getMessage());
        }
    }
    public void setResourcePath(Activity activity, int resource){
        setResourcePath(activity,resource,Ext.MP3);
    }

    public String getResourcePath() {
        return this.resourcePath;
    }
}
