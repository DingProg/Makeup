package com.ding.makeup.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

import com.amnix.skinsmoothness.AmniXSkinSmooth;

/**
 * author:DingDeGao
 * time:2019-08-08-14:37
 * function: 美颜 美白
 */
public class MakeupBeautyUtils {

    private final AmniXSkinSmooth amniXSkinSmooth = AmniXSkinSmooth.getInstance();

    public void progress(final Bitmap bitmap, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                amniXSkinSmooth.storeBitmap(bitmap, false);
                amniXSkinSmooth.initSdk();
//                amniXSkinSmooth.startFullBeauty(200, 10);
                amniXSkinSmooth.startSkinSmoothness(200);
//                amniXSkinSmooth.startSkinWhiteness(10);

                Bitmap result = amniXSkinSmooth.getBitmapAndFree();
                amniXSkinSmooth.unInitSdk();

                Message obtain = Message.obtain();

                //合成结果,一般是磨皮过的，一般是没有磨皮的
                Bitmap leftAndRightBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(leftAndRightBitmap);
                Rect left = new Rect(0,0,bitmap.getWidth()/2 + 3,bitmap.getHeight()); //3为了弥补 int值相除精读损失，让左边多一些
                Rect right = new Rect(bitmap.getWidth() - bitmap.getWidth()/2 ,0,bitmap.getWidth(),bitmap.getHeight());
                canvas.drawBitmap(result,left,left,null);
                canvas.drawBitmap(bitmap,right,right,null);

                obtain.obj = leftAndRightBitmap;
                obtain.what = 0;
                handler.sendMessage(obtain);
            }
        }).start();
    }

    public void destroy(){
        if(amniXSkinSmooth != null) {
            amniXSkinSmooth.onDestroy();
        }
    }
}
