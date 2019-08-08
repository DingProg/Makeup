package com.ding.makeup.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import com.ding.makeup.utils.BitmapUtils;
import com.ding.makeup.utils.EyeAngleAndScaleCalc;

import java.util.List;

/**
 * author:DingDeGao
 * time:2019-08-07-10:40
 * function: 画眼睛
 */
public class EyeDraw {

    /**
     * 美瞳
     */
    public static void drawContact(Canvas canvas, Bitmap contactBitmap, Path eyePath, Point centerPoint, int eyeRadius, int alpha) {
        Path contactPath = new Path();
        contactPath.addCircle(centerPoint.x,centerPoint.y,eyeRadius, Path.Direction.CCW);

        contactPath.op(eyePath, Path.Op.INTERSECT);


        RectF bounds = new RectF();
        contactPath.computeBounds(bounds,true);
        bounds.offset(1,0);
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(contactBitmap,new Rect(0,30,contactBitmap.getWidth(),contactBitmap.getHeight() - 60),bounds,paint);
    }

    /**
     * 睫毛
     */
    public static void drawLash(Context context, Canvas canvas, EyeAngleAndScaleCalc.Bean bean, List<Point> pointList, int alpha, boolean needMirror) {
        EyeAngleAndScaleCalc eyeAngleAndScaleCalc = new EyeAngleAndScaleCalc(pointList,bean);

        Paint paint = new Paint();
        paint.setAlpha(alpha);

        Bitmap resTopBitmap = BitmapUtils.getBitmapByAssetsName(context,bean.resTop);
        Bitmap scaledBitmapTop = Bitmap.createScaledBitmap(resTopBitmap, (int) (resTopBitmap.getWidth() * eyeAngleAndScaleCalc.topScaleX + 0.5),
                (int) (resTopBitmap.getHeight() * eyeAngleAndScaleCalc.topScaleY + 0.5), true);
        resTopBitmap.recycle();


        Bitmap resBottomBitmap = null;
        Bitmap scaledBitmapBottom = null;
        if (!TextUtils.isEmpty(bean.resBottom)) {
            resBottomBitmap = BitmapUtils.getBitmapByAssetsName(context,bean.resBottom);
            scaledBitmapBottom = Bitmap.createScaledBitmap(resBottomBitmap, (int) (resBottomBitmap.getWidth() * eyeAngleAndScaleCalc.bottomScaleX + 0.5),
                    (int) (resBottomBitmap.getHeight() * eyeAngleAndScaleCalc.bottomScaleY + 0.5), true);
            resBottomBitmap.recycle();
        }

        if (needMirror) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1);   //镜像水平翻转
            scaledBitmapTop = Bitmap.createBitmap(scaledBitmapTop, 0, 0, scaledBitmapTop.getWidth(), scaledBitmapTop.getHeight(), matrix, true);
            if (resBottomBitmap != null) {
                scaledBitmapBottom = Bitmap.createBitmap(scaledBitmapBottom, 0, 0, scaledBitmapBottom.getWidth(), scaledBitmapBottom.getHeight(), matrix, true);
            }
        }

        canvas.save();
        //canvas.rotate(eyeAngleAndScaleCalc.getTopEyeAngle(), eyeAngleAndScaleCalc.topP1.x, eyeAngleAndScaleCalc.topP1.y);
        canvas.drawBitmap(scaledBitmapTop,
                eyeAngleAndScaleCalc.topP1.x - (int) (bean.topP1.x * eyeAngleAndScaleCalc.topScaleX),
                eyeAngleAndScaleCalc.topP1.y - (int) (bean.topP1.y * eyeAngleAndScaleCalc.topScaleY), paint);
        canvas.restore();

        if (scaledBitmapBottom != null) {
            canvas.save();
            canvas.rotate(eyeAngleAndScaleCalc.getBottomEyeAngle(), eyeAngleAndScaleCalc.bottomP1.x, eyeAngleAndScaleCalc.bottomP1.y);
            canvas.drawBitmap(scaledBitmapBottom, eyeAngleAndScaleCalc.bottomP1.x,
                    eyeAngleAndScaleCalc.bottomP1.y - (int) (bean.bottomP1.y * eyeAngleAndScaleCalc.bottomScaleY), paint);
            canvas.restore();
            scaledBitmapBottom.recycle();
        }
        scaledBitmapTop.recycle();
    }

    /**
     * 绘制彩妆 Shadow
     *
     */
    public static void drawShadow(Context context, Canvas canvas, EyeAngleAndScaleCalc.Bean bean, Path eyePath, List<Point> pointList, int alpha) {
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        EyeAngleAndScaleCalc eyeAngleAndScaleCalc = new EyeAngleAndScaleCalc(pointList,bean);


        Bitmap bitmapShadow = BitmapUtils.getBitmapByAssetsName(context,bean.resTop);

        Point point = eyeAngleAndScaleCalc.topP1;

        RectF rectF = new RectF();
        eyePath.computeBounds(rectF,true);

        float xScale = rectF.width() / bean.rect.width();
        float yScale = rectF.height() / bean.rect.height();


        Bitmap scaledBitmapLower = Bitmap.createScaledBitmap(bitmapShadow, (int) (bitmapShadow.getWidth() * xScale + 0.5),
                (int) (bitmapShadow.getHeight() * yScale + 0.5), true);
        canvas.save();


        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);   //镜像水平翻转
        scaledBitmapLower = Bitmap.createBitmap(scaledBitmapLower, 0, 0, scaledBitmapLower.getWidth(), scaledBitmapLower.getHeight(), matrix, true);


        canvas.rotate(eyeAngleAndScaleCalc.getTopEyeAngle(), point.x, point.y);
        canvas.drawBitmap(scaledBitmapLower, point.x - (bitmapShadow.getWidth() - bean.topP3.x) * xScale, point.y - bean.topP3.y * yScale, paint);
        canvas.restore();

        bitmapShadow.recycle();
        scaledBitmapLower.recycle();
    }


}
