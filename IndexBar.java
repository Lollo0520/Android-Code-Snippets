package com.wxah.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wxah.util.DensityUtil;
import com.wxah.util.PinyinUtil;

/**
 * 字母索引
 * Created by 右右 on 2015/6/8.
 */
public class IndexBar extends View {
    private IndexSelectedListener listener;
    private ListView listView;

    private String[] index = {"定位", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int height; // 每个索引的高度

    private int selectionPos;   // 记录当前位置
    private int currentScrollPos;   // 记录当前ListView滚动位置

    public IndexBar(Context context) {
        super(context);
        init();
    }

    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListView(ListView listView) {
        this.listView = listView;
        ListAdapter adapter = listView.getAdapter();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    if (currentScrollPos != firstVisibleItem) {
                        currentScrollPos = firstVisibleItem;
                        selectionPos = firstVisibleItem;
                        invalidate();
                    }
                    return;
                }

                if (currentScrollPos != firstVisibleItem) {
                    String firstString = (String) adapter.getItem(firstVisibleItem);
                    String first = PinyinUtil.converterToFirstSpell(firstString.substring(0, 1));
                    for (int i = 1; i < index.length - 1; i++) {
                        if (first.equals(index[i])) {
                            currentScrollPos = firstVisibleItem;
                            selectionPos = i;
                            invalidate();
                            return;
                        }
                    }
                }
            }
        });
    }

    public void setListener(IndexSelectedListener listener) {
        this.listener = listener;
    }

    private void init() {
        paint.setColor(Color.parseColor("#8c8c8c"));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(DensityUtil.sp2px(getContext(), 13));

        Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        focusPaint.setTypeface(font);
        focusPaint.setTextAlign(Paint.Align.CENTER);
        focusPaint.setTextSize(DensityUtil.sp2px(getContext(), 14));
        focusPaint.setColor(Color.parseColor("#f33737"));
    }

    /**
     * 根据Y坐标判断 位置
     */
    private int positionForPoint(float y) {
        int position = (int) (y / height);
        if (position < 0) {
            position = 0;
        }
        if (position > index.length - 1) {
            position = index.length - 1;
        }

        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        height = getHeight() / index.length;

        for (int i = 0; i < index.length; i++) {
            if (selectionPos == i) {
                canvas.drawText(index[i], getWidth() / 2, height * (i + 1), focusPaint);
            } else {
                canvas.drawText(index[i], getWidth() / 2, height * (i + 1), paint);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY();
        int pos = positionForPoint(y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pos != selectionPos) {
                    selectionPos = pos;
                    if (listView != null) {
                        if (currentScrollPos != selectionPos) {
                            listView.setSelection(selectionPos);
                        }
                    }
                    if (listener != null) {
                        listener.onSelection(selectionPos);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (pos != selectionPos) {
                    selectionPos = pos;
                    if (listView != null) {
                        if (currentScrollPos != selectionPos) {
                            listView.setSelection(selectionPos);
                        }
                    }
                    if (listener != null) {
                        listener.onSelection(selectionPos);
                    }
                    invalidate();
                }
                break;
            default:
                break;
        }

        return true;
    }


    public interface IndexSelectedListener {
        void onSelection(int position);
    }
}
