package com.itep.mt.common.view;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.itep.mt.common.R;


/**
 *手势指引
 */
public class GesturePopWindow extends PopupWindow {
    private View conentView;
    private int w;
    private int h;

    public GesturePopWindow(final Activity context, int w, int h) {
        initCurrency(context, R.layout.dialog_gesture, w, h);
    }

    public GesturePopWindow(final Activity context, int resourceId, int w, int h) {
        initCurrency(context, resourceId, w, h);
    }

    //根据本地版本信息配置，控制签名框样式
    //抽取之前变量及控件初始化语句为方法，方便重复使用
    private void initCurrency(Activity context, int resourceId, int w, int h) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(resourceId, null);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        this.h = h;
        this.w = w;
        //设置边界值
        if (this.h > screenHeight)
            this.h = screenHeight;
        if (this.w > screenWidth)
            this.w = screenWidth;
        this.setContentView(conentView);
        this.setWidth(this.w);
        this.setHeight(this.h);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.update();
    }

    public void showPopupWindow(View parent, int x, int y) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.TOP | Gravity.START, x, y);
        } else {
            this.dismiss();
        }
    }

}
