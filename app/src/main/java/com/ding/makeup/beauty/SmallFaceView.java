package com.ding.makeup.beauty;

/**
 * @author by dingdegao
 * time 2017/10/12 18:21
 * function:
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ding.makeup.utils.CommonShareBitmap;

/**
 * @author by dingdegao
 *         function: SmallFaceView
 *         算法来源：http://www.gson.org/thesis/warping-thesis.pdf
 */
public class SmallFaceView extends View {

    private int mWidth, mHeight;//View 的宽高

    //作用范围半径
    private int r = 160;

    //画圆的作业范围
    private float radius = 50;

    private Paint circlePaint;
    private Paint directionPaint;

    //是否显示变形圆圈
    private boolean showCircle;
    //是否显示变形方向
    private boolean showDirection;

    //变形起始坐标,滑动坐标
    private float startX, startY, moveX, moveY;

    //将图像分成多少格
    private int WIDTH = 200;
    private int HEIGHT = 200;

    //交点坐标的个数
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);

    //用于保存COUNT的坐标
    //x0, y0, x1, y1......
    private float[] verts = new float[COUNT * 2];

    //用于保存原始的坐标
    private float[] orig = new float[COUNT * 2];

    private Bitmap mBitmap;

    private boolean isEnableOperate = true;
    private float mScale = 1.0f;
    private int dx = 0;
    private int dy = 0;


    private IOnStepChangeListener onStepChangeListener;

    public SmallFaceView(Context context) {
        super(context);
        init();
    }

    public SmallFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmallFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(5);
        circlePaint.setColor(Color.parseColor("#FFE130"));
        circlePaint.setAntiAlias(true);

        directionPaint = new Paint();
        directionPaint.setStyle(Paint.Style.FILL);
        directionPaint.setStrokeWidth(5);
        directionPaint.setColor(Color.parseColor("#FFE130"));
        directionPaint.setAntiAlias(true);

        radius = 100;
    }

    public void setEnableOperate(boolean enableOperate) {
        isEnableOperate = enableOperate;
    }


    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        if(bitmap == null) return;
        post(new Runnable() {
            @Override
            public void run() {
                zoomBitmap(mBitmap,getWidth(),getHeight());
                invalidate();
            }
        });
        invalidate();
    }

    public void setRestoreBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidate();
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public Bitmap getBitmap() {
        if(mBitmap == null) return null;
        Bitmap copy = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(copy);
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        return copy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public float getScale(){
        return mScale;
    }


    private void restoreVerts() {
        int index = 0;
        float bmWidth = mBitmap.getWidth();
        float bmHeight = mBitmap.getHeight();
        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                //X轴坐标 放在偶数位
                verts[index * 2] = fx;
                orig[index * 2] = verts[index * 2];
                //Y轴坐标 放在奇数位
                verts[index * 2 + 1] = fy;
                orig[index * 2 + 1] = verts[index * 2 + 1];
                index += 1;
            }
        }
        showCircle = false;
        showDirection = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void zoomBitmap(Bitmap bitmap, int width, int height) {
        if(bitmap == null) return;
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();

        float scale = 1.0f;

        // 图片的宽度大于控件的宽度，图片的高度小于空间的高度，我们将其缩小
        if (dw > width && dh < height) {
            scale = width * 1.0f / dw;
        }

        // 图片的宽度小于控件的宽度，图片的高度大于空间的高度，我们将其缩小
        if (dh > height && dw < width) {
            scale = height * 1.0f / dh;
        }

        // 缩小值
        if (dw > width && dh > height) {
            scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
        }

        // 放大值
        if (dw < width && dh < height) {
            scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
        }

        //缩小
        if (dw == width && dh > height) {
            scale = height * 1.0f / dh;
        }
        dx = width / 2 - (int) (dw * scale + 0.5f) / 2;
        dy = height / 2 - (int) (dh * scale + 0.5f) / 2;

        mScale = scale;
        restoreVerts();
    }

    public void setLevel(int level) {
        //level [0,4]
        r = 140 + 15 * level;
        radius = 100;
        invalidate();
    }

    boolean isSmllBody = false;

    public void setSmllBody(boolean isSmallBody) {
        isSmllBody = isSmallBody;
    }

    boolean isShowOrigin = false;

    public void showOrigin(boolean isShowOrigin) {
        this.isShowOrigin = isShowOrigin;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap == null) return;
        canvas.save();
        canvas.translate(dx, dy);
        canvas.scale(mScale, mScale);
        if (isShowOrigin) {
            canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, orig, 0, null, 0, null);
        } else {
            canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        }

        canvas.restore();
        if (showCircle && isEnableOperate) {
            canvas.drawCircle(startX, startY, radius, circlePaint);
            canvas.drawCircle(startX, startY, 5, directionPaint);
        }
        if (showDirection && isEnableOperate) {
            canvas.drawLine(startX, startY, moveX, moveY, directionPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnableOperate) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //绘制变形区域
                startX = event.getX();
                startY = event.getY();
                showCircle = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //绘制变形方向
                moveX = event.getX();
                moveY = event.getY();
                showDirection = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                showCircle = false;
                showDirection = false;

                //调用warp方法根据触摸屏事件的坐标点来扭曲verts数组
                if(mBitmap != null && verts!= null && !mBitmap.isRecycled()) {
                    warp(startX, startY, event.getX(), event.getY());
                }

                if (onStepChangeListener != null) {
                    onStepChangeListener.onStepChange(false);
                }
                break;
        }
        return true;
    }

    /**
     * 将屏幕触摸坐标x转换成在图片中的坐标
     */
    public final float toX(float touchX) {
        return (touchX  - dx) /  mScale;
    }

    /**
     * 将屏幕触摸坐标y转换成在图片中的坐标
     */
    public final float toY(float touchY) {
        return (touchY  - dy) /  mScale;
    }

    private void warp(float startX, float startY, float endX, float endY) {
        startX = toX(startX);
        startY = toY(startY);
        endX = toX(endX);
        endY = toY(endY);

        //计算拖动距离
        float ddPull = (endX - startX) * (endX - startX) + (endY - startY) * (endY - startY);
        float dPull = (float) Math.sqrt(ddPull);
        //dPull = screenWidth - dPull >= 0.0001f ? screenWidth - dPull : 0.0001f;
        if (dPull < 2 * r) {
            if (isSmllBody) {
                dPull = 1.8f * r;
            } else {
                dPull = 2.5f * r;
            }
        }

        int powR = r * r;
        int index = 0;
        int offset = 1;
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                //边界区域不处理
                if(i < offset || i > HEIGHT - offset || j < offset || j > WIDTH - offset){
                    index = index + 1;
                    continue;
                }
                //计算每个坐标点与触摸点之间的距离
                float dx = verts[index * 2] - startX;
                float dy = verts[index * 2 + 1] - startY;
                float dd = dx * dx + dy * dy;

                if (dd < powR) {
                    //变形系数，扭曲度
                    double e = (powR - dd) * (powR - dd) / ((powR - dd + dPull * dPull) * (powR - dd + dPull * dPull));
                    double pullX = e * (endX - startX);
                    double pullY = e * (endY - startY);
                    verts[index * 2] = (float) (verts[index * 2] + pullX);
                    verts[index * 2 + 1] = (float) (verts[index * 2 + 1] + pullY);

                   // check
                    if(verts[index * 2] < 0){
                        verts[index * 2] = 0;
                    }
                    if(verts[index * 2] > mBitmap.getWidth()){
                        verts[index * 2] =  mBitmap.getWidth();
                    }

                    if(verts[index * 2 + 1] < 0){
                        verts[index * 2 +1] = 0;
                    }
                    if(verts[index * 2 + 1] > mBitmap.getHeight()){
                        verts[index * 2 + 1] = mBitmap.getHeight();
                    }
                }
                index = index + 1;
            }
        }
        invalidate();
    }

    /**
     * 一键恢复
     */

    public void resetView() {
        for (int i = 0; i < verts.length; i++) {
            verts[i] = orig[i];
        }
        if (onStepChangeListener != null) {
            onStepChangeListener.onStepChange(true);
        }
        showCircle = false;
        showDirection = false;
        invalidate();
    }

    public void setOnStepChangeListener(IOnStepChangeListener onStepChangeListener) {
        this.onStepChangeListener = onStepChangeListener;
    }

    public interface IOnStepChangeListener {
        void onStepChange(boolean isEmpty);
    }

}

