package com.digitalsigma.hobrasul.Activity;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.digitalsigma.hobrasul.Other.Constant;
import com.digitalsigma.hobrasul.R;

import net.alhazmy13.catcho.library.Catcho;

/**
 * Created by user2 on 1/11/2017.
 */

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    String API_KEY,Video_ID;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Catcho.Builder(this)
                .activity(ContactActivity.class)
                .build();



        Video_ID= Constant.video_id;
        API_KEY="AIzaSyAV0ysZvmqBDggFnoeRHzw8IT4zV2Kxqos";
       // API_KEY="AIzaSyCXTIIQA1igh0TbzWqSp8ji6wEq8LVLgB0";

        setContentView(R.layout.activity_youtube_player);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(API_KEY, this);


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {



        if (null == youTubePlayer) return;

        youTubePlayer.setFullscreen(true);
        //   YouTubePlayer.loadVideo(Config.YOUTUBE_VIDEO_CODE);
        if (!b) {

            //youTubePlayer.loadVideo(VIDEO_ID);

            // youTubePlayer.cueVideo(Video_ID);
            youTubePlayer.loadVideo(Constant.video_id);
        }

        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        // Hiding player controls
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    YouTubePlayer.PlaybackEventListener playbackEventListener=new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    YouTubePlayer.PlayerStateChangeListener playerStateChangeListener=new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };



}
