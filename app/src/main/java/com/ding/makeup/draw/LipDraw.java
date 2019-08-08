package com.ding.makeup.draw;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * @author by dingdegao
 *         time 2017/8/29 12:18
 *         function: lip draw
 */

public class LipDraw {


    public static int alphaColor(int color, int alpha) {
        return (color & 0x00FFFFFF) | alpha;
    }

    public static void drawLipPerfect(Canvas canvas, Path lipPath, int color, int alpha) {
        //most 70% alpha
        if (alpha > 80) {
            alpha = (int) (alpha * 0.9f + 0.5f);
        }

        alpha = (int) (Color.alpha(color) * ((float) alpha / 255)) << 24;
        color = alphaColor(color, alpha);
        final PointF position = new PointF();
        float blur_radius = 5;

        Bitmap mask = createMask(lipPath, color, blur_radius, position);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawBitmap(mask, position.x, position.y, paint);
    }

    /**
     * create closed path and filled with specified color.
     *
     * @param path
     * @param color       the color of filled region.
     * @param blur_radius Blur radius, use it to get smooth mask.
     * @param position    stores the mask top-left position if <code>position</code> is not null.
     * @return mask
     */
    public static Bitmap createMask(final Path path, int color, float blur_radius, PointF position) {
        if (path == null || path.isEmpty())
            return null;

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        bounds.inset(-blur_radius, -blur_radius);

        int width = (int) bounds.width();
        int height = (int) bounds.height();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  // mutable
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));
        paint.setColor(color);
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
