package com.example.wenhaibo.wechattest1;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by wenhaibo on 2017/12/19.
 */

public class MediaManager {

    private static MediaPlayer mediaPlayer;
    private static boolean isPause;
    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
    if(mediaPlayer==null){
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                mediaPlayer.reset();
                return false;
            }
        });

    }else
    {
        mediaPlayer.reset();

    }
        try {
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mediaPlayer.setOnCompletionListener(onCompletionListener);

            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public  static  void pause()
    {
        if(mediaPlayer!=null&& mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause=true;
        }
    }
    public  static void resume()
    {
       if(mediaPlayer!=null&&isPause){
           mediaPlayer.start();;
           isPause=false;
       }
    }
    public  static void release(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }

    }
}
