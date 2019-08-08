package com.ding.makeup.draw;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.Nullable;

/**
 * @author by dingdegao
 *         time 2017/11/8 14:38
 *         function: FoundationDraw
 */

public class FoundationDraw {

    public static void draw(Canvas canvas, Path facePath, int color, int alpha){
        final PointF position = new PointF();
        Bitmap mask = createMask(facePath,color, position,alpha,8);

        if(mask != null && !mask.isRecycled()){
            Bitmap gradientBitmapByXferomd = getGradientBitmapByXferomd(mask, Math.max(mask.getWidth(), mask.getHeight()));
            mask.recycle();
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            if(gradientBitmapByXferomd != null) {
                canvas.drawBitmap(gradientBitmapByXferomd, position.x, position.y, paint);
                gradientBitmapByXferomd.recycle();
            }
        }
    }

    private static Bitmap getGradientBitmapByXferomd(Bitmap originBitmap, float radius){
        if(radius < 10) radius = 10;
        Bitmap canvasBitmap = Bitmap.createBitmap(originBitmap.getWidth(),originBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBitmap);
        Paint paint = new Paint();

        BitmapShader bitmapShader = new BitmapShader(originBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        RadialGradient radialGradient = new RadialGradient(originBitmap.getWidth() / 2, originBitmap.getHeight() / 2,
                radius, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        paint.setShader(new ComposeShader(bitmapShader,radialGradient,new PorterDuffXfermode(PorterDuff.Mode.DST_IN)));
        canvas.drawRect(new Rect(0,0,canvasBitmap.getWidth(),canvasBitmap.getHeight()), paint);
        return canvasBitmap;
    }

    /**
     * create closed path and filled with specified color.
     *
     * @param path
     * @param color       the color of filled region.
     * @param position    stores the mask top-left position if <code>position</code> is not null.
     * @return mask
     */
    private static Bitmap createMask(final Path path, int color, @Nullable PointF position, int alpha, int blur_radius) {
        if (path == null || path.isEmpty())
            return null;

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);

        int width = (int) bounds.width();
        int height = (int) bounds.height();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  // mutable
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.FILL);
        path.offset(-bounds.left, -bounds.top);
        canvas.drawPath(path, paint);
        if (position != null) {
            position.x = bounds.left;
            position.y = bounds.top;
        }
        return bitmap;
    }
}
