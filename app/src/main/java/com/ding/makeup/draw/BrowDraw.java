package com.ding.makeup.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * author:DingDeGao
 * time:2019-08-07-13:29
 * function: 画眉毛
 */
public class BrowDraw {

    public static void draw(Canvas canvas, Bitmap eyeBrowRes, Path path, int alpha){
        Paint paint = new Paint();
        paint.setAlpha(alpha);

        RectF rectF = new RectF();
        path.computeBounds(rectF,true);

        canvas.drawBitmap(eyeBrowRes,new Rect(0,0,eyeBrowRes.getWidth(),eyeBrowRes.getHeight() - 30),rectF,paint);
    }

}
