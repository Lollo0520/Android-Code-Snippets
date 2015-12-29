package com.wxah.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 监听滑动事件的ScrollView
 * Created by 右右 on 2015/5/19.
 */
public class ObservableScrollView extends ScrollView {

    private ObservableScrollListener listener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setOnScrollListener(ObservableScrollListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (listener != null) {
            listener.onScrollChanged(x, y, oldx, oldy);
        }
    }
}
