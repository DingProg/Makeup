package com.ding.makeup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ding.makeup.beauty.MagnifyEyeUtils;
import com.ding.makeup.utils.BitmapUtils;
import com.ding.makeup.utils.CommonShareBitmap;
import com.ding.makeup.utils.DrawUtils;
import com.ding.makeup.utils.FacePoint;
import com.ding.makeup.utils.MakeupBeautyUtils;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private MakeupBeautyUtils makeupBeautyUtils;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.obj instanceof Bitmap){
                if(msg.what == 0) {
                    drawMakeup((Bitmap) msg.obj);
                    magnifyEye((Bitmap) msg.obj);
                }else if(msg.what == 1){
                    imageView.setImageBitmap((Bitmap) msg.obj);
                }
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeupBeautyUtils = new MakeupBeautyUtils();
        imageView = findViewById(R.id.img);
        load();
    }

    private void load() {
        try {
            Bitmap bitmap = BitmapUtils.getBitmapByAssetsName(this,"makeup1.jpeg");
            CommonShareBitmap.originBitmap = bitmap;
            makeupBeautyUtils.progress(bitmap,handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawMakeup(Bitmap bitmap){
        DrawUtils drawUtils = new DrawUtils();
        drawUtils.init();
        drawUtils.draw(this,drawUtils.getMakeupList(),bitmap,"face_point1.json");
        imageView.setImageBitmap(bitmap);
    }

    private void magnifyEye(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String faceJson = FacePoint.getFaceJson(MainActivity.this,"face_point1.json");
                Bitmap magnifyEye = MagnifyEyeUtils.magnifyEye(bitmap, Objects.requireNonNull(FacePoint.getLeftEyeCenter(faceJson)), FacePoint.getLeftEyeRadius(faceJson) * 3, 3);

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = magnifyEye;
                handler.sendMessage(msg);

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeupBeautyUtils.destroy();
        if(CommonShareBitmap.originBitmap != null && !CommonShareBitmap.originBitmap.isRecycled()){
            CommonShareBitmap.originBitmap.recycle();
            CommonShareBitmap.originBitmap = null;
        }
    }

    public void enterEyeMagnify(View view) {
        startActivity(new Intent(this,MagnifyActivity.class));
    }

    public void enterSmallFace(View view) {
        startActivity(new Intent(this,SmallFaceActivity.class));
    }

    public void enterLongLeg(View view) {
        startActivity(new Intent(this, AdjustLegActivity.class));
    }
}
