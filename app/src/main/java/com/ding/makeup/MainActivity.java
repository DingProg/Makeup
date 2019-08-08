package com.ding.makeup;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ding.makeup.utils.BitmapUtils;
import com.ding.makeup.utils.DrawUtils;
import com.ding.makeup.utils.MakeupBeautyUtils;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private MakeupBeautyUtils makeupBeautyUtils;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.obj instanceof Bitmap){
                drawMakeup((Bitmap) msg.obj);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeupBeautyUtils.destroy();
    }
}
