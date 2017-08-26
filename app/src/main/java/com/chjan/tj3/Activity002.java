package com.chjan.tj3;
/* 影片播放 */
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Activity002 extends AppCompatActivity implements MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener {

    private VideoView mVideoView;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout002);

        mVideoView = (VideoView)findViewById(R.id.videoView);
        MediaController mc = new MediaController(this);
        mVideoView.setMediaController(mc);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video01);
        flag = 2;
        mVideoView.setVideoURI(uri);
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        mVideoView.stopPlayback();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mVideoView.start();
        super.onResume();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Uri uri;
        if(flag == 1){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video01);
            flag = 2;
        }else{
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video02);
            flag = 1;
        }
        mVideoView.setVideoURI(uri);
        mVideoView.start();

        Toast.makeText(Activity002.this,"播放完畢,Play Next.",Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(Activity002.this,"發生錯誤",Toast.LENGTH_LONG).show();
        return false;
    }
}
