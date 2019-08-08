package com.ding.makeup.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * @author by dingdegao
 *         time 2019/8/7 18:11
 *         function: blush draw
 */

public class BlushDraw {

    public static void drawBlush(Canvas canvas, Bitmap faceBlush, Path path, int alpha) {
        Paint paint = new Paint();
        paint.setAlpha(alpha);

        RectF rectF = new RectF();
        path.computeBounds(rectF,true);

        canvas.drawBitmap(faceBlush,null,rectF,paint);

    }
}
