package com.ding.makeup.beauty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;



/**
 * @author by dingdegao
 *         time 2017/10/16 11:13
 *         function: AdjustView
 */

public class AdjustLegView extends View {

    private Paint paint = new Paint();
    private Paint bgPaint = new Paint();
    private Paint textPaint = new Paint();

    private int topLine = 0;
    private int bottomLine = 0;

    private float minLine = 0;
    private float maxLine = 0;

    private static final int OFFSETY = 30;
    private static final int LINEHIGHT = 5;
    private int selectPos = -1; // 1 top ,2 bottom
    private Rect rect;
    private String tipStr;
    private Listener listener;
    private int measuredHeight = 0;
    private float imgHeight = 0;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        void down();
        void up(Rect rect);
    }

    public Rect getRect() {
        return rect;
    }

    public AdjustLegView(Context context) {
        super(context);
        init();
    }

    public AdjustLegView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdjustLegView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initHeight();
    }

    private void initHeight() {
        if(measuredHeight == 0) {
            measuredHeight = getMeasuredHeight();
            minLine = measuredHeight/2 - imgHeight/2 ;
            maxLine = measuredHeight/2 + imgHeight/2;
            topLine = (int)(minLine + imgHeight * 0.6f + 0.5f);
            bottomLine = (int)(maxLine - imgHeight * 0.3f + 0.5f);
            rect = new Rect(0, topLine + LINEHIGHT, getWidth(), bottomLine);
            invalidate();
        }
    }

    public void setLineLimit(float height){
        imgHeight = height;
        measuredHeight = 0;
        post(new Runnable() {
            @Override
            public void run() {
                initHeight();
            }
        });

    }

    private void init() {
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        bgPaint.setAlpha(0x7f);
        bgPaint.setAntiAlias(true);

        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(dipToPixels(getContext(),16));
        tipStr = "此区域为选中区域";
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }



    float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectPos = checkSelect(y);
                lastY = y;
                if(selectPos != -1 && listener != null){
                    listener.down();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (selectPos == 1) {
                    // 最小 20 的偏移量
                    topLine += checkLimit(y - lastY);
                    invalidate();
                }
                if (selectPos == 2) {
                    bottomLine += checkLimit(y - lastY);
                    invalidate();
                }
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                selectPos = -1;
                invalidate();
                if( listener != null){
                    listener.up(rect);
                }
                break;
        }
        return true;
    }

    private float checkLimit(float offset) {
        if (selectPos == 1) {
            if(topLine + offset > minLine && topLine + offset < maxLine){
                return offset;
            }
        }
        if (selectPos == 2) {
            if(bottomLine + offset > minLine && bottomLine + offset < maxLine){
                return offset;
            }
        }
        return 0;
    }

    private int checkSelect(float y) {
        selectPos = -1;
        RectF rect = new RectF(0, y - OFFSETY, 0, y + OFFSETY);
        float min = -1;
        if (topLine >= rect.top && topLine <= rect.bottom) {
            selectPos = 1;
            min = rect.bottom - topLine;
        }

        if (bottomLine >= rect.top && bottomLine <= rect.bottom) {
            if (min > bottomLine - rect.top || min == -1) {
                selectPos = 2;
            }
        }
        return selectPos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //line
        canvas.drawRect(0, topLine, getWidth(), topLine + LINEHIGHT, paint);
        //line
        canvas.drawRect(0, bottomLine, getWidth(), bottomLine + LINEHIGHT, paint);

        if (selectPos != -1) {
            swap();
            rect.set(0, topLine + LINEHIGHT, getWidth(), bottomLine);
            canvas.drawRect(rect, bgPaint);
            if(tipStr != null){
                @SuppressLint("DrawAllocation") Rect textRect = new Rect();
                textPaint.getTextBounds(tipStr,0,tipStr.length()-1,textRect);
                canvas.drawText(tipStr,rect.left + (rect.width()/ 2 -textRect.width()/2),
                        rect.top + (rect.height()/ 2 -textRect.height()/2),textPaint);
            }
        }
    }

    private void swap() {
        if(topLine > bottomLine){
            int t = topLine;
            topLine = bottomLine;
            bottomLine = t;
        }
    }
}
