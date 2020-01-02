package com.ando.player.ext;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.ando.player.R;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.util.PlayerUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Title: TabletTitleView
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/2  10:47
 */
public class TabletTitleView extends TitleView {
    private ControlWrapper controlWrapper;

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        super.attach(controlWrapper);
        this.controlWrapper = controlWrapper;
    }

    public TabletTitleView(@NonNull Context context) {
        super(context);
    }

    public TabletTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabletTitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    {
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = PlayerUtils.scanForActivity(getContext());
                if (activity != null && controlWrapper.isFullScreen()) {
                    //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    controlWrapper.stopFullScreen();
                }
            }
        });
    }

}