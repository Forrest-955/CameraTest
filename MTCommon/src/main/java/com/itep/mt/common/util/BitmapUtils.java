package com.itep.mt.common.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.View;

import com.itep.mt.common.constant.VersionNameConstant;
import com.itep.mt.common.sys.SysConf;

import java.io.ByteArrayOutputStream;

/**
 * 图片处理工具类
 * Created by JimGw on 2017/5/25.
 */
public class BitmapUtils {
    /**
     * 获取图片bitmap
     *
     * @param Imagepath
     * @return
     */
    public static Bitmap getImageThumbnail(String Imagepath) {
        Bitmap bm = BitmapFactory.decodeFile(Imagepath);
        bm = resizeImage(bm, 100, 60);
        return bm;
    }

    /**
     * 重置图片大小
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 重置图片大小
     *
     * @param bitmap
     * @param proportion 缩放比例
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, float proportion) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        float scaleWidth = proportion;
        float scaleHeight = proportion;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 把白底转换成透明
     *
     * @param mBitmap
     * @return
     */
    public static Bitmap getImageToChange(Bitmap mBitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (mBitmap != null) {
            int mWidth = mBitmap.getWidth();
            int mHeight = mBitmap.getHeight();
            for (int i = 0; i < mHeight; i++) {
                for (int j = 0; j < mWidth; j++) {
                    int color = mBitmap.getPixel(j, i);
                    int g = Color.green(color);
                    int r = Color.red(color);
                    int b = Color.blue(color);
                    int a = Color.alpha(color);
//                    if (g >= 250 && r >= 250 && b >= 250) {
                    if (g > 160 && r > 160 && b > 160) {
                        a = 0;
                        g = r = b = 255;
                    }

                    if (g < 255 && r < 255 && b < 255) {
//                        g -= 70;
//                        b -= 70;
                        r = 255;
                        g = 0;
                        b = 0;
                    }
                    color = Color.argb(a, r, g, b);
                    createBitmap.setPixel(j, i, color);
                }
            }
        }
        return createBitmap;
    }

    /**
     * 重置图片大小,剪裁图片
     *
     * @param bitmap
     * @param w      宽
     * @param h      高
     * @param x      起始点x坐标
     * @param y      起始点y坐标
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h, int x, int y) {
        Logger.i("resizeImage:" + w + " " + h + " " + x + " " + y);
        if (w < 20) {//用于最小边界值判断，防止裁剪直白
            w = 20;
        }
        if (h < 20) {//用于最小边界值判断，防止裁剪直白
            h = 20;
        }
        if (y < 0) {//用于最小边界值判断，防止裁剪直白
            y = 0;
        }
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        //防止图片比例变化造成失真
        if ((w - h) * (width - height) < 0) {
            scaleWidth = ((float) newWidth) / height;
            scaleHeight = ((float) newHeight) / width;
        }

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        if ((x + w) > bitmap.getWidth()) {//边界最大值判断，防止裁剪失败
            x = 0;
            w = bitmap.getWidth();
        }
        if ((y + h) > bitmap.getHeight()) {//边界最大判断，防止裁剪失败
            if (VersionNameConstant.SCNX.equals(SysConf.getAppVersionName())) {
                y = bitmap.getHeight() - h - 1;
            } else {
                y = 0;
                h = bitmap.getHeight();
            }
        }
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, x, y, w, h, matrix, true);
        return resizedBitmap;
    }

    public static Bitmap resizeImage2(Bitmap bitmap, int w, int h, int x, int y) {
        Logger.i("resizeImage:" + w + " " + h + " " + x + " " + y);
        if (w < 20) {//用于最小边界值判断，防止裁剪直白
            w = 20;
        }
        if (h < 20) {//用于最小边界值判断，防止裁剪直白
            h = 20;
        }
        if (y < 0) {//用于最小边界值判断，防止裁剪直白
            y = 0;
        }
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//
//        //防止图片比例变化造成失真
//        if ((w - h) * (width - height) < 0) {
//            scaleWidth = ((float) newWidth) / height;
//            scaleHeight = ((float) newHeight) / width;
//        }

        float scaleWidth = 1;
        float scaleHeight = 1;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        if ((x + w) > bitmap.getWidth()) {//边界最大值判断，防止裁剪失败
            x = bitmap.getWidth() - w;
//            w = bitmap.getWidth();
        }
        if ((y + h) > bitmap.getHeight()) {//边界最大判断，防止裁剪失败
            if (VersionNameConstant.SCNX.equals(SysConf.getAppVersionName())) {
                y = bitmap.getHeight() - h - 1;
            } else {
                y = bitmap.getHeight() - h;
//                h = bitmap.getHeight();
            }
        }
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, x, y, w, h, matrix, true);
        return resizedBitmap;
    }

    /**
     * Bitmap转换byte
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, baos);
        return baos.toByteArray();
    }

    /**
     * view显示在屏幕内的界面转成bitmap
     *
     * @param v 要截图的view
     * @return 截取的bitmap图片
     */
    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        //        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    /**
     * 根据指定的Activity截图（带空白的状态栏）
     *
     * @param context 要截图的Activity
     * @return Bitmap
     */
    public static Bitmap shotActivity(Activity context) {
        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 合并两张图片
     *
     * @param bmp1 下层图片
     * @param bmp2 上层图片
     * @return 输出合成图片
     */
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        try {

            Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
            Canvas canvas = new Canvas(bmOverlay);
            canvas.drawBitmap(bmp1, 0, 0, null);
            canvas.drawBitmap(bmp2, 0, 0, null);
            return bmOverlay;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对图片进行二值化处理
     *
     * @param bm 原始图片
     * @return 二值化处理后的图片
     */
    public static Bitmap getBinaryzationBitmap(Bitmap bm) {
        Bitmap bitmap = null;
        // 获取图片的宽和高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 创建二值化图像
        bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        // 遍历原始图像像素,并进行二值化处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 得到当前的像素值
                int pixel = bitmap.getPixel(i, j);
                // 得到Alpha通道的值
                int alpha = pixel & 0xFF000000;
                // 得到Red的值
                int red = (pixel & 0x00FF0000) >> 16;
                // 得到Green的值
                int green = (pixel & 0x0000FF00) >> 8;
                // 得到Blue的值
                int blue = pixel & 0x000000FF;
                // 通过加权平均算法,计算出最佳像素值
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                // 对图像设置黑白图
                if (gray <= 95) {
                    gray = 0;
                } else {
                    gray = 255;
                }
                // 得到新的像素值
                int newPiexl = (alpha | (gray << 16) | (gray << 8) | gray) | 0x00FF0000;//二值化成红色
                // 赋予新图像的像素
                bitmap.setPixel(i, j, newPiexl);
            }
        }
        return bitmap;
    }

}