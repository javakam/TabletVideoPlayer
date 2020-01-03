package com.ando.player.example;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ando.player.R;
import com.ando.player.VideoBean;
import com.ando.player.ui.tablet.TabletStandardVideoController;
import com.ando.player.ui.tablet.component.TabletCompleteView;
import com.ando.player.ui.tablet.component.TabletErrorView;
import com.ando.player.ui.tablet.component.TabletGestureView;
import com.ando.player.ui.tablet.component.TabletLiveControlView;
import com.ando.player.ui.tablet.component.TabletPrepareView;
import com.ando.player.ui.tablet.component.TabletTitleView;
import com.ando.player.ui.tablet.component.TabletVodControlView;
import com.bumptech.glide.Glide;
import com.dueeeke.videoplayer.player.VideoView;

/**
 * Title: 自定义样式
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/2  9:59
 */
public class CustomPlayerFragment extends Fragment {

    private static final String[] STRINGS_PLAYER_STATE = {"开始", "暂停"};

    private Activity activity;
    private VideoView mVideoView;
    private TabletStandardVideoController mController;

    private ImageView ivPlayerControl;
    private TextView tvPlayerState, tvPlayerProgress;
    private TextView tvPlayerBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_custom, container, false);
        mVideoView = view.findViewById(R.id.videoView);


        ivPlayerControl = view.findViewById(R.id.iv_player_control);
        tvPlayerState = view.findViewById(R.id.tv_player_state);
        tvPlayerProgress = view.findViewById(R.id.tv_player_progress);
        tvPlayerBack = view.findViewById(R.id.tv_player_back);

        initPlayerView();
        initPlayerOutView();
        return view;
    }

    private void initPlayerView() {
        adaptCutoutAboveAndroidP();

        final VideoBean videoBean = new VideoBean("预告片8",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4");


        mController = new TabletStandardVideoController(activity);
        //根据屏幕方向自动进入/退出全屏
        mController.setEnableOrientation(false);

        TabletPrepareView tabletPrepareView = new TabletPrepareView(activity);//准备播放界面
        ImageView thumb = tabletPrepareView.findViewById(R.id.thumb);//封面图
        Glide.with(activity).load(videoBean.getThumb()).into(thumb);
        tabletPrepareView.setClickStart();
        mController.addControlComponent(tabletPrepareView);

        mController.addControlComponent(new TabletCompleteView(activity));//自动完成播放界面

        mController.addControlComponent(new TabletErrorView(activity));//错误界面

        TabletTitleView titleView = new TabletTitleView(activity);//标题栏
        mController.addControlComponent(titleView);

        //根据是否为直播设置不同的底部控制条
        //boolean isLive = intent.getBooleanExtra(IntentKeys.IS_LIVE, false);
        boolean isLive = false;
        if (isLive) {
            mController.addControlComponent(new TabletLiveControlView(activity));//直播控制条
        } else {
            TabletVodControlView vodControlView = new TabletVodControlView(activity);//点播控制条
            //是否显示底部进度条。默认显示
            // vodControlView.showBottomProgress(false);
            vodControlView.setVodProgressChanged(new TabletVodControlView.VodProgressChanged() {
                @Override
                public void onProgressChanged(String progressTime) {
                    if (tvPlayerProgress != null) {
                        tvPlayerProgress.setText(progressTime);
                    }
                }
            });

            mController.addControlComponent(vodControlView);

        }

        TabletGestureView gestureControlView = new TabletGestureView(activity);//滑动控制视图
        mController.addControlComponent(gestureControlView);
        //根据是否为直播决定是否需要滑动调节进度
        mController.setCanChangePosition(!isLive);

        //设置标题
        String title = videoBean.getTitle();
        titleView.setTitle(title);

        mVideoView.setVideoController(mController);

        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_16_9);
        mVideoView.addOnStateChangeListener(mOnStateChangeListener);

        mVideoView.setUrl(videoBean.getUrl());

        //保存播放进度
//        mVideoView.setProgressManager(new ProgressManagerImpl());
//        mVideoView.start();

    }

    private void initPlayerOutView() {

        //播放控制
        ivPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int playState = mVideoView.getCurrentPlayState();
                //播放完成 or 播放错误  不开启播放控制
                if (playState == VideoView.STATE_PLAYBACK_COMPLETED) {
                    mVideoView.replay(true);
                    return;
                } else if (playState == VideoView.STATE_ERROR) {
                    mVideoView.replay(false);
                    return;
                }

                ivPlayerControl.setSelected(!v.isSelected());
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    tvPlayerState.setText(STRINGS_PLAYER_STATE[1]);
                } else {
                    tvPlayerState.setText(STRINGS_PLAYER_STATE[0]);

                    if (mVideoView.getDuration() == 0) {
                        //tabletPrepareView.setClickStart();
                        mVideoView.start();
                    } else {
                        mVideoView.resume();
                    }
                }

            }
        });

        //返回
        tvPlayerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "返回", Toast.LENGTH_SHORT).show();
            }
        });
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
                    ivPlayerControl.setSelected(false);
                    tvPlayerState.setText(STRINGS_PLAYER_STATE[1]);
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

                    ivPlayerControl.setSelected(true);
                    tvPlayerState.setText(STRINGS_PLAYER_STATE[0]);

                    break;
                case VideoView.STATE_PAUSED:
                    ivPlayerControl.setSelected(false);
                    tvPlayerState.setText(STRINGS_PLAYER_STATE[1]);
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    Log.w("123", "播放结束");
                    ivPlayerControl.setSelected(false);
                    tvPlayerState.setText(STRINGS_PLAYER_STATE[1]);
                    break;
                case VideoView.STATE_ERROR:
                    ivPlayerControl.setSelected(false);
                    tvPlayerState.setText(STRINGS_PLAYER_STATE[1]);
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

        if (mVideoView != null) {

            if (mController.isLocked()) {
                mController.show();
                Toast.makeText(activity, R.string.tablet_lock_tip, Toast.LENGTH_SHORT).show();
                return;
            }

            if (mVideoView.isFullScreen()) {
                Log.w("123", "退出全屏");
                mVideoView.stopFullScreen();
                return;
            }
        }

        activity.finish();
    }

    private void adaptCutoutAboveAndroidP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            activity.getWindow().setAttributes(lp);
        }
    }


}