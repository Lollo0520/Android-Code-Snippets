package com.compscieddy.meetinthemiddle.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.compscieddy.meetinthemiddle.FontCache;
import com.compscieddy.meetinthemiddle.R;

public class FontTextView extends TextView {

  private final Context mContext;

  public FontTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    if (isInEditMode()) return;

    TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.FontTextView);
    int typefaceId = ta.getInt(R.styleable.FontTextView_fontface, FontCache.MONTSERRAT_REGULAR);
    setCustomTypeFace(typefaceId);
    ta.recycle();

  }

  public void setCustomTypeFace(int typeFaceId) {
    setTypeface(FontCache.get(mContext, typeFaceId));
  }


}
