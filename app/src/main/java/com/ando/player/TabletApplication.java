package com.ando.player;

import android.app.Application;

import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;

/**
 * Title: TabletApplication
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/2  9:52
 */
public class TabletApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initVideoPlayer(BuildConfig.DEBUG);
    }

    private void initVideoPlayer(boolean isDebug) {
        //1.全局切换
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                .setLogEnabled(isDebug)
                //使用ExoPlayer解码
                .setPlayerFactory(IjkPlayerFactory.create())
                .build());

        //2.临时切换 - 调用VideoView中的setPlayerFactory方法，需要在start方法之前调用方可生效

        //使用IjkPlayer解码
        //mVideoView.setPlayerFactory(IjkPlayerFactory.create());
        //使用ExoPlayer解码
        //mVideoView.setPlayerFactory(ExoMediaPlayerFactory.create());


        //严苛模式
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
//        }
    }

}