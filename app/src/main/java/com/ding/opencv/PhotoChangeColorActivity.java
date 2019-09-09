package com.ding.opencv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ding.makeup.R;
import com.ding.makeup.utils.BitmapUtils;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoChangeColorActivity extends AppCompatActivity {

    private static final String TAG = PhotoChangeColorActivity.class.getSimpleName();

    private ImageView img;
    private ImageView imgPreview;
    private ImageView imgResult;
    private View showPreview;
    private Button compare;

    private  Bitmap bitmap;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.obj instanceof Bitmap){
                showCompare((Bitmap) message.obj);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_change);

        showPreview = findViewById(R.id.showPreview);
        img = findViewById(R.id.img);
        imgPreview = findViewById(R.id.imgPreview);
        imgResult = findViewById(R.id.imgResult);

        bitmap = BitmapUtils.getBitmapByAssetsNameRGB(this,"photo.jpeg");
        img.setImageBitmap(bitmap);

        compare = findViewById(R.id.compare);
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare.setVisibility(View.GONE);
                showPreview.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
            }
        });

        initLoaderOpenCV();
    }

    private void initLoaderOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if (!success) {
            Log.d(TAG, "初始化失败");
        }
    }

    public void changePhoto(View view) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               Log.i(TAG,"start handle photo");
               startDetail();
           }
       }).start();
    }

    /**
     * 2.利用openCV中cvSplit函数的在选择图像IPL_DEPTH_32F类型时，H取值范围是0-360，S取值范围是0-1（0%-100%），V取值范围是0-1（0%-100%）。
     *
     * 3.利用openCV中cvSplit函数的在选择图像IPL_DEPTH_8UC类型时，H取值范围是0-180，S取值范围是0-255，V取值范围是0-255。
     *
     */
    private void startDetail() {
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);

        Mat hsvImg = new Mat();
        Imgproc.cvtColor(image, hsvImg, Imgproc.COLOR_BGR2HSV);


        List<Mat> list = new ArrayList<>();
        Core.split(hsvImg, list);

        Mat roiH = list.get(0).submat(new Rect(0, 0, 20, 20));
        Mat roiS = list.get(1).submat(new Rect(0, 0, 20, 20));

        Log.i(TAG,"start sum bg");
        int SumH = 0;
        int SumS = 0;
        byte[] h = new byte[1];
        byte[] s = new byte[1];
        //取一块蓝色背景，计算出它的平均色调和平均饱和度
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                roiH.get(j, i, h);
                roiS.get(j, i, s);

                SumH = h[0] + SumH;
                SumS = s[0] + SumS;
            }
        }

        int avgH, avgS;//蓝底的平均色调和平均饱和度
        avgH = SumH / 400;
        avgS = SumS / 400;


        Log.i(TAG,"depth="+list.get(0).depth());
        Log.i(TAG,"start sum detail all photo");
        //遍历整个图像
        int nl = hsvImg.height();
        int nc = hsvImg.width();


//        byte[] changeColor = new byte[]{127};

        byte[] hArray = new byte[nl * nc];
        byte[] sArray = new byte[nl * nc];
        byte[] vArray = new byte[nl * nc];

        list.get(0).get(0,0,hArray);
        list.get(1).get(0,0,sArray);
//        list.get(2).get(0,0,vArray);

        int row,index;
        for (int j = 0; j < nl; j++) {
            row = j * nc;
            for (int i = 0; i < nc; i++) {
                index = row + i;

                if(hArray[index] <= (avgH + 20) && hArray[index] >= (avgH - 20)
                        && sArray[index] <= (avgS + 150)
                        && sArray[index] >= (avgS -150)
                ){
                    hArray[index] = 127;
//                    sArray[index] = 0;
//                    vArray[index] = (byte) 255;
                }
            }
        }

        list.get(0).put(0,0,hArray);
        list.get(1).put(0,0,sArray);
//        list.get(2).put(0,0,vArray);


        Log.i(TAG,"merge photo");
        Core.merge(list,hsvImg);

        Imgproc.cvtColor(hsvImg,image, Imgproc.COLOR_HSV2BGR);

        Bitmap resultBitmap = getResultBitmap();
        Utils.matToBitmap(image,resultBitmap);
        Message obtain = Message.obtain();
        obtain.obj = resultBitmap;
        handler.sendMessage(obtain);
    }


    public void gray(View view) {
        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        Bitmap resultBitmap = getResultBitmap();
        Utils.matToBitmap(dst, resultBitmap);
        src.release();
        dst.release();

        showCompare(resultBitmap);
    }

    private Bitmap getResultBitmap(){
        return Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);
    }

    private void showCompare(Bitmap resultBitmap){
        showPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageBitmap(bitmap);
        imgResult.setImageBitmap(resultBitmap);
        img.setVisibility(View.GONE);
        compare.setVisibility(View.VISIBLE);
    }

    public void inpaint(View view) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               startInpaint();
           }
       }).start();
    }

    private void startInpaint() {
        bitmap = BitmapUtils.getBitmapByAssetsNameRGB(this,"test.png");
        Mat desc = new Mat(bitmap.getHeight(),bitmap.getWidth(),CvType.CV_8UC3);

        Utils.bitmapToMat(bitmap, desc,true);

        Mat src = new Mat();
        Imgproc.cvtColor(desc,src,Imgproc.COLOR_RGBA2RGB);


        Mat srcGray = new Mat();
        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_RGB2GRAY);

        Imgproc.medianBlur(srcGray,srcGray,3);

        Mat srcThresh = new Mat();
        Imgproc.threshold(srcGray,srcThresh,242,255,Imgproc.THRESH_BINARY);

        Log.i("test","srcThresh channels:"+srcThresh.channels() + ",type:"+ CvType.typeToString(CvType.depth(srcThresh.type())));
        Log.i("test","src channels:"+src.channels() + ",type:"+ CvType.typeToString(CvType.depth(src.type())));

//        Bitmap resultBitmap = getResultBitmap();
//        Utils.matToBitmap(srcThresh, resultBitmap);


        Mat inpaintResult = new Mat();
        Photo.inpaint(src,srcThresh,inpaintResult,3,Photo.INPAINT_TELEA);

        Bitmap resultBitmap = getResultBitmap();
        Utils.matToBitmap(inpaintResult, resultBitmap);

        Message obtain = Message.obtain();
        obtain.obj = resultBitmap;
        handler.sendMessage(obtain);
    }
}
