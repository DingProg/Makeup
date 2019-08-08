package com.ding.makeup;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ding.makeup.utils.BitmapUtils;
import com.ding.makeup.utils.DrawUtils;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.img);
        load();
    }

    private void load() {
        try {

            Bitmap bitmap = BitmapUtils.getBitmapByAssetsName(this,"makeup.jpeg");
            DrawUtils drawUtils = new DrawUtils();
            drawUtils.init();
            drawUtils.draw(this,drawUtils.getMakeupList(),bitmap,"face_point.json");

            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
