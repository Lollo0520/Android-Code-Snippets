package com.wxah.customview.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orange.anhuipeople.R;
import com.wxah.util.DensityUtil;


/**
 * YoDialog
 * Created by 右右 on 2015/5/21.
 */
public class YoDialog extends AlertDialog implements View.OnClickListener {
    private Builder b;
    private TextView title, content;
    private ImageView icon, img_line1, buttonBarDivider;
    private Button btn_Negative, btn_Positive;
    private LinearLayout buttonDefaultFrame, titleFrame;
    private ScrollView contentScrollView;
    private FrameLayout customViewFrame;
    private ListView list_content;


//    public YoDialog(Context context, int theme) {
//        super(context, R.style.Theme_Yo_Dialog_light);
//    }

    protected YoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private static ContextThemeWrapper getTheme(Builder builder) {
        TypedArray a = builder.context.getTheme().obtainStyledAttributes(new int[]{R.attr.md_dark_theme});
        boolean darkTheme = builder.theme == Theme.DARK;
        if (!darkTheme) {
            try {
                darkTheme = a.getBoolean(0, false);
                builder.theme = darkTheme ? Theme.DARK : Theme.LIGHT;
            } finally {
                a.recycle();
            }
        }
        return new ContextThemeWrapper(builder.context, darkTheme ? R.style.MD_Dark : R.style.MD_Light);
    }

    public YoDialog(Builder builder) {
        super(getTheme(builder));

        this.b = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int maxWidth = DensityUtil.dip2px(getContext(), 480);
        int width = dm.widthPixels - DensityUtil.dip2px(getContext(), 28);
        if (width > maxWidth) {
            width = maxWidth;
        }

        View view = getLayoutInflater().inflate(R.layout.dialog_yo, null);
        addContentView(view, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));

        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        img_line1 = (ImageView) findViewById(R.id.img_line1);
        btn_Negative = (Button) findViewById(R.id.btn_Negative);
        btn_Positive = (Button) findViewById(R.id.btn_Positive);
        btn_Positive.setOnClickListener(this);
        btn_Negative.setOnClickListener(this);

        if (b.strTitle != null) {
            title.setText(b.strTitle);
        } else {
            titleFrame = (LinearLayout) findViewById(R.id.titleFrame);
            titleFrame.setVisibility(View.GONE);
            findViewById(R.id.titleBarDivider).setVisibility(View.GONE);
        }

        if (b.iconRes != 0) {
            icon = (ImageView) findViewById(R.id.icon);
            icon.setImageResource(b.iconRes);
        }

        if (b.strContent != null) {
            contentScrollView = (ScrollView) findViewById(R.id.contentScrollView);
            contentScrollView.setVisibility(View.VISIBLE);
            content = (TextView) findViewById(R.id.content);
            content.setText(b.strContent);
        }

        if (b.items != null) {
            list_content = (ListView) findViewById(R.id.list_content);
            list_content.setVisibility(View.VISIBLE);
            list_content.setOverScrollMode(View.OVER_SCROLL_NEVER);

            buttonBarDivider = (ImageView) findViewById(R.id.buttonBarDivider);
            buttonDefaultFrame = (LinearLayout) findViewById(R.id.buttonDefaultFrame);
            buttonBarDivider.setVisibility(View.GONE);
            buttonDefaultFrame.setVisibility(View.GONE);

            YoDialogListAdapter adapter = new YoDialogListAdapter(getContext(), b.items);
            list_content.setAdapter(adapter);

            if (b.listCallback != null) {
                list_content.setOnItemClickListener((parent, view, position, id) -> b.listCallback.onSelection(this, view, position, b.items[position]));
            }
        }

        if (b.positiveTextRes != 0) {
            btn_Positive.setText(b.positiveTextRes);
        } else {
            buttonBarDivider = (ImageView) findViewById(R.id.buttonBarDivider);
            buttonDefaultFrame = (LinearLayout) findViewById(R.id.buttonDefaultFrame);
            buttonBarDivider.setVisibility(View.GONE);
            buttonDefaultFrame.setVisibility(View.GONE);
        }

        if (b.negativeTextRes != 0) {
            btn_Negative.setText(b.negativeTextRes);
        } else {
            btn_Negative.setVisibility(View.GONE);
            img_line1.setVisibility(View.GONE);
        }

        if (b.viewRes != 0) {
            customViewFrame = (FrameLayout) findViewById(R.id.customViewFrame);
            customViewFrame.setVisibility(View.VISIBLE);

            View view = LayoutInflater.from(getContext()).inflate(b.viewRes, null);
            customViewFrame.addView(view);
        }

//        setCanceledOnTouchOutside(b.cancelable);
        setCancelable(b.cancelable);
    }


    /**
     * 设置对话框显示位置
     *
     * @param gravity Gravity.BOTTOM  Gravity.CENTER  Gravity.TOP
     */
    public void setDialogPosition(int gravity) {
        getWindow().setGravity(gravity);
    }

    @Override
    public void onClick(View v) {
        if (b.callback == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_Positive:
                b.callback.onPositive(this);
                break;
            case R.id.btn_Negative:
                b.callback.onNegative(this);
                dismiss();
                break;
        }
    }

    public static class Builder {
        private Context context;
        private String strTitle;
        private int iconRes;
        private int viewRes;
        private String strContent;
        private String[] items;
        private int positiveTextRes;
        private int negativeTextRes;
        private boolean cancelable = true;
        private ButtonCallback callback;
        private ListCallback listCallback;
        private Theme theme = Theme.LIGHT;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(@StringRes int resid) {
            setTitle(context.getString(resid));
            return this;
        }

        public Builder setTitle(String strTitle) {
            this.strTitle = strTitle;
            return this;
        }

        public Builder setContent(@StringRes int resid) {
            setContent(context.getString(resid));
            return this;
        }

        public Builder setContent(String strContent) {
            this.strContent = strContent;
            return this;
        }

        public Builder setItems(@ArrayRes int resid, ListCallback callback) {
            setItems(context.getResources().getStringArray(resid), callback);
            return this;
        }

        public Builder setItems(String[] items, ListCallback callback) {
            this.items = items;
            this.listCallback = callback;
            return this;
        }

        public Builder setTitleIcon(@DrawableRes int resid) {
            this.iconRes = resid;
            return this;
        }


        public Builder setCustomView(@LayoutRes int resid) {
            this.viewRes = resid;
            return this;
        }

        public Builder setPositiveText(@StringRes int resid) {
            this.positiveTextRes = resid;
            return this;
        }

        public Builder setNegativeText(@StringRes int resid) {
            this.negativeTextRes = resid;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder callback(ButtonCallback callback) {
            this.callback = callback;
            return this;
        }

//        public Builder itemsCallback(ListCallback callback) {
//            this.listCallback = callback;
//            return this;
//        }

        public YoDialog build() {
            return new YoDialog(this);
        }

        public YoDialog show() {
            YoDialog dialog = build();
            dialog.show();
            return dialog;
        }

    }

    @Override
    public void show() {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new IllegalStateException("Dialogs can only be shown from the UI thread.");
        super.show();
    }

    public interface ListCallback {
        void onSelection(YoDialog dialog, View itemView, int which, String text);
    }

    public interface ButtonCallback {
        void onPositive(YoDialog dialog);

        void onNegative(YoDialog dialog);
    }


    public class YoDialogListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private CharSequence[] items;
        private ViewHolder holder;

        public YoDialogListAdapter(Context context, CharSequence[] items) {
            inflater = LayoutInflater.from(context);
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dialog_yo_item, parent, false);
                holder = new ViewHolder();
                holder.content = (TextView) convertView.findViewById(R.id.content);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CharSequence item = items[position];

            holder.content.setText(item);

            return convertView;
        }

        class ViewHolder {
            private TextView content;
        }
    }
}
