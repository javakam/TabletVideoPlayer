package com.ando.player.ext;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;

import com.ando.player.R;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.util.PlayerUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Title: TabletVodControlView
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/2  10:55
 */
public class TabletOriginVodControlView extends VodControlView {
    public TabletOriginVodControlView(@NonNull Context context) {
        super(context);
    }

    public TabletOriginVodControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabletOriginVodControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fullscreen) {

            Activity activity = PlayerUtils.scanForActivity(getContext());
            if (activity == null || activity.isFinishing()) {
                return;
            }
            if (mControlWrapper.isFullScreen()) {
                // activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mControlWrapper.stopFullScreen();
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mControlWrapper.startFullScreen();
            }
        } else if (id == R.id.iv_play) {
            mControlWrapper.togglePlay();
        }
    }

}