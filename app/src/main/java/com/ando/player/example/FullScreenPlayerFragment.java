package com.ando.player.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ando.player.R;
import com.ando.player.VideoBean;
import com.ando.player.ext.TabletOriginTitleView;
import com.ando.player.ext.TabletOriginVodControlView;
import com.bumptech.glide.Glide;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.LiveControlView;
import com.dueeeke.videocontroller.component.PrepareView;
import com.dueeeke.videoplayer.player.VideoView;


/**
 * Title: 通用
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/2  9:59
 */
public class FullScreenPlayerFragment extends Fragment {

    private Activity activity;
    private VideoView mVideoView;
    private StandardVideoController mController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        initPlayerView(view);
        return view;
    }

    private void initPlayerView(View view) {

        mVideoView = view.findViewById(R.id.videoView);
        mVideoView.startFullScreen();

        final VideoBean videoBean = new VideoBean("预告片8",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4");


        mController = new StandardVideoController(activity);
        //根据屏幕方向自动进入/退出全屏
        mController.setEnableOrientation(false);

        PrepareView prepareView = new PrepareView(activity);//准备播放界面
        ImageView thumb = prepareView.findViewById(R.id.thumb);//封面图
        Glide.with(activity).load(videoBean.getThumb()).into(thumb);
        prepareView.setClickStart();
        mController.addControlComponent(prepareView);

        mController.addControlComponent(new CompleteView(activity));//自动完成播放界面

        mController.addControlComponent(new ErrorView(activity));//错误界面

        TabletOriginTitleView titleView = new TabletOriginTitleView(activity);//标题栏
        titleView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               activity.onBackPressed();
            }
        });
        mController.addControlComponent(titleView);


        //根据是否为直播设置不同的底部控制条
        //boolean isLive = intent.getBooleanExtra(IntentKeys.IS_LIVE, false);
        boolean isLive = false;
        if (isLive) {
            mController.addControlComponent(new LiveControlView(activity));//直播控制条
        } else {
            TabletOriginVodControlView vodControlView = new TabletOriginVodControlView(activity);//点播控制条
            //是否显示底部进度条。默认显示
//                vodControlView.showBottomProgress(false);
            mController.addControlComponent(vodControlView);
        }

        GestureView gestureControlView = new GestureView(activity);//滑动控制视图
        mController.addControlComponent(gestureControlView);
        //根据是否为直播决定是否需要滑动调节进度
        mController.setCanChangePosition(!isLive);

        //设置标题
        String title = videoBean.getTitle();
        titleView.setTitle(title);

        //注意：以上组件如果你想单独定制，我推荐你把源码复制一份出来，然后改成你想要的样子。
        //改完之后再通过addControlComponent添加上去
        //你也可以通过addControlComponent添加一些你自己的组件，具体实现方式参考现有组件的实现。
        //这个组件不一定是View，请发挥你的想象力😃

        //如果你不需要单独配置各个组件，可以直接调用此方法快速添加以上组件
//            controller.addDefaultControlComponent(title, isLive);

        //竖屏也开启手势操作，默认关闭
//            controller.setEnableInNormal(true);
        //滑动调节亮度，音量，进度，默认开启
//            controller.setGestureEnabled(false);
        //适配刘海屏，默认开启
//            controller.setAdaptCutout(false);

        //如果你不想要UI，不要设置控制器即可
        mVideoView.setVideoController(mController);

        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_16_9);
        mVideoView.addOnStateChangeListener(mOnStateChangeListener);
        mVideoView.setUrl(videoBean.getUrl());

        //保存播放进度
//            mVideoView.setProgressManager(new ProgressManagerImpl());
        //播放状态监听
//            mVideoView.addOnVideoViewStateChangeListener(mOnVideoViewStateChangeListener);

        //临时切换播放核心，如需全局请通过VideoConfig配置，详见MyApplication
        //使用IjkPlayer解码
//            mVideoView.setPlayerFactory(IjkPlayerFactory.create());
        //使用ExoPlayer解码
//            mVideoView.setPlayerFactory(ExoMediaPlayerFactory.create());
        //使用MediaPlayer解码
//            mVideoView.setPlayerFactory(AndroidMediaPlayerFactory.create());

//        mVideoView.start();

    }

    private VideoView.OnStateChangeListener mOnStateChangeListener = new VideoView.SimpleOnStateChangeListener() {
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL://小屏
                    Log.w("123", "小屏");
                    break;
                case VideoView.PLAYER_FULL_SCREEN://全屏
                    Log.w("123", "全屏");
                    break;
                default:
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    break;
                case VideoView.STATE_PREPARED:
                    break;
                case VideoView.STATE_PLAYING:
                    //需在此时获取视频宽高
                    int[] videoSize = mVideoView.getVideoSize();
                    Log.w("123", "视频宽：" + videoSize[0]);
                    Log.w("123", "视频高：" + videoSize[1]);
                    break;
                case VideoView.STATE_PAUSED:
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    break;
                case VideoView.STATE_ERROR:
                    break;
                default:
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //https://stackoverflow.com/questions/22552958/handling-back-press-when-using-fragments-in-android
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button
                    Toast.makeText(activity, "Handle back event.", Toast.LENGTH_SHORT).show();
                    onPlayerBackPressed();
                    return true;

                }

                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }

    public void onPlayerBackPressed() {
        activity.onBackPressed();
    }

}