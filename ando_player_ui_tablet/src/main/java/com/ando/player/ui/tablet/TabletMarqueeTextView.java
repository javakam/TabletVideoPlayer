package com.ando.player.ui.tablet;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 实现跑马灯效果的TextView
 */
public class TabletMarqueeTextView extends AppCompatTextView {
    private boolean mNeedFocus;
    public TabletMarqueeTextView(Context context) {
        super(context);
    }
    public TabletMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TabletMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //返回textview是否处在选中的状态
    //而只有选中的textview才能够实现跑马灯效果
    @Override
    public boolean isFocused() {
        if (mNeedFocus) {
            return false;
        }
        return super.isFocused();
    }

    public void setNeedFocus(boolean needFocus) {
        mNeedFocus = needFocus;
    }
}