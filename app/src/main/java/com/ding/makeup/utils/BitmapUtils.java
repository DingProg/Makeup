package com.ding.makeup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.IOException;

/**
 * author:DingDeGao
 * time:2019-08-07-10:42
 * function: default function
 */
public class BitmapUtils {

    public static Bitmap getBitmapByAssetsName(Context context, String name){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inMutable = true;

        try {
            return BitmapFactory.decodeStream(context.getAssets().open(name),new Rect(),options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
