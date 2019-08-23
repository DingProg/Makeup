package com.ding.makeup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ding.makeup.beauty.SmallFaceView;
import com.ding.makeup.utils.CommonShareBitmap;

/**
 * author:DingDeGao
 * time:2019-08-23-16:16
 * function: default function
 */
public class SmallFaceActivity extends AppCompatActivity {

    private ImageView img;
    private ImageView imgResult;

    private SmallFaceView smallFaceView;
    private View showPreview;
    private Button compare;

    private  boolean isShowCompare = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_face);

        imgResult = findViewById(R.id.imgResult);
        img = findViewById(R.id.img);
        showPreview = findViewById(R.id.showPreview);
        smallFaceView = findViewById(R.id.smallFaceView);


        compare = findViewById(R.id.compare);
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowCompare = !isShowCompare;
                compare.setText(isShowCompare?"返回":"对比");
                loadImage();
            }
        });

        smallFaceView.setBitmap(CommonShareBitmap.originBitmap);
        loadImage();
    }

    private void loadImage() {

        showPreview.setVisibility(isShowCompare ?View.VISIBLE:View.GONE);
        smallFaceView.setVisibility(!isShowCompare ?View.VISIBLE:View.GONE);

        if(isShowCompare){
            img.setImageBitmap(CommonShareBitmap.originBitmap);
            imgResult.setImageBitmap(smallFaceView.getBitmap());
        }


//        img.setImageBitmap(CommonShareBitmap.originBitmap);
//
//        String faceJson = FacePoint.getFaceJson(this,"face_point1.json");
//
//        Bitmap bitmap = SmallFaceUtils.smallFaceMesh(CommonShareBitmap.originBitmap,
//                FacePoint.getLeftFacePoint(faceJson),
//                FacePoint.getRightFacePoint(faceJson),
//                FacePoint.getCenterPoint(faceJson), 5);
//
//
//        imgResult.setImageBitmap(bitmap);

    }
}
