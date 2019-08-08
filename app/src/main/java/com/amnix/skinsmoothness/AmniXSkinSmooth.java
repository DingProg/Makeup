package com.amnix.skinsmoothness;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.ByteBuffer;

public class AmniXSkinSmooth {
    private static ByteBuffer mByteBuffer = null;

    static {
        System.loadLibrary("AmniXSkinSmooth");
    }

    private static AmniXSkinSmooth mAmniXSkinSmooth = null;

    private AmniXSkinSmooth() {
    }

    public static AmniXSkinSmooth getInstance() {
        if (mAmniXSkinSmooth == null) mAmniXSkinSmooth = new AmniXSkinSmooth();
        return mAmniXSkinSmooth;
    }

    public void startSkinSmoothness(float level) {
        if (mByteBuffer == null) return;
        jniStartSkinSmooth(level);
    }

    public void startSkinWhiteness(float level) {
        if (mByteBuffer == null) return;
        jniStartWhiteSkin(level);
    }

    public void startFullBeauty(float smoothLevel, float whileLevel) {
        if(mByteBuffer == null) return;
        jniStartFullBeauty(smoothLevel,whileLevel);
    }

    public void initSdk() {
        if (mByteBuffer == null) return;
        jniInitBeauty(mByteBuffer);
    }

    public void unInitSdk() {
        jniUninitBeauty();
    }

    public void storeBitmap(Bitmap bitmap, boolean recyle) {
        if (mByteBuffer != null)
            freeBitmap();
        mByteBuffer = jniStoreBitmapData(bitmap);
        if (recyle)
            bitmap.recycle();
    }

    public void freeBitmap() {
        if (mByteBuffer == null)
            return;
        jniFreeBitmapData(mByteBuffer);
        mByteBuffer = null;
    }

    public Bitmap getBitmap() {
        if (mByteBuffer == null)
            return null;
        return jniGetBitmapFromStoredBitmapData(mByteBuffer);
    }

    public Bitmap getBitmapAndFree() {
        final Bitmap bitmap = getBitmap();
        freeBitmap();
        return bitmap;
    }

    public void onDestroy() {
        freeBitmap();
        unInitSdk();
        mAmniXSkinSmooth = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mByteBuffer == null)
            return;
        Log.w("AmniXSkinSmooth", "JNI bitmap wasn't freed nicely.please remember to free the bitmap as soon as you can");
        freeBitmap();
        Log.w("AmniXSkinSmooth", "AmniXSkinSmooth wasn't uninit nicely.please remember to uninit");
        unInitSdk();
    }

    private native void jniUninitBeauty();

    private native void jniInitBeauty(ByteBuffer handler);

    private native void jniStartSkinSmooth(float skinSmoothLevel);

    private native void jniStartWhiteSkin(float whitenessLevel);

    private native void jniStartFullBeauty(float skinSmoothLevel, float whitenessLevel);

    private native ByteBuffer jniStoreBitmapData(Bitmap bitmap);

    private native void jniFreeBitmapData(ByteBuffer handler);

    private native Bitmap jniGetBitmapFromStoredBitmapData(ByteBuffer handler);

}
