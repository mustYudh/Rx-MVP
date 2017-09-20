package com.htgames.rxmvp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;


public class MeasureUtils {

    private MeasureUtils() {
        throw new AssertionError();
    }

    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp 转 px
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /***
     * 获取控件宽度
     * @param child
     * @return
     */
    public static int getMeasuredWidthWithMargins(View child) {
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth()/* + lp.leftMargin + lp.rightMargin*/;
    }

    /**
     * 获取控件高度
     * @param child
     * @return
     */
    public static int getMeasuredWidthWithHeight(View child) {
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        return child.getHeight()/* + lp.bottomMargin + lp.topMargin*/;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }



    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }



    /**
     * 获取控件当前坐标
     * @param view
     * @return
     */
    public static int[] getViewLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * 获取view中的内容为位图
     *
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        //        Bitmap bitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.RGB_565);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        //把view中的内容绘制在画布上
        view.draw(canvas);
        return bitmap;
    }
}
