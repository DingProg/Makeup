package com.ding.makeup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import com.ding.makeup.draw.BlushDraw;
import com.ding.makeup.draw.BrowDraw;
import com.ding.makeup.draw.EyeDraw;
import com.ding.makeup.draw.FoundationDraw;
import com.ding.makeup.draw.LipDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * author:DingDeGao
 * time:2019-08-08-10:26
 * function: default function
 */
public class DrawUtils {

    private List<Region> makeupList = new ArrayList<>();

    public void init(){
        makeupList.add(Region.FOUNDATION);
        makeupList.add(Region.LIP);

        makeupList.add(Region.BLUSH);
        makeupList.add(Region.BROW);


        makeupList.add(Region.EYE_SHADOW);
        makeupList.add(Region.EYE_LASH);
        makeupList.add(Region.EYE_DOUBLE);
        makeupList.add(Region.EYE_LINE);
        makeupList.add(Region.EYE_CONTACT);
    }

    public void draw(Context context, List<Region> list,Bitmap originBitmap,String facePointJson) {
        try {
            if(originBitmap == null) return;

            Canvas canvas = new Canvas(originBitmap);
            String faceJson = FacePoint.getFaceJson(context,facePointJson);

            for (Region region : list) {
               switch (region){
                   case FOUNDATION:
                       FoundationDraw.draw(canvas,FacePoint.landmark(faceJson), Color.WHITE,80);
                       break;
                   case BLUSH:
                       Bitmap blush = BitmapUtils.getBitmapByAssetsName(context,"face_blush.png");
                       BlushDraw.drawBlush(canvas,blush,FacePoint.getBlush(faceJson),100);
                       break;
                   case BROW:
                       Bitmap eyeBrow = BitmapUtils.getBitmapByAssetsName(context,"brow.png");
                       BrowDraw.draw(canvas,eyeBrow,FacePoint.getLeftEyeBrow(faceJson),100);
                       break;
                   case LIP:
                       Path mouthPath = FacePoint.getMouthPath(faceJson);
                       LipDraw.drawLipPerfect(canvas,mouthPath, Color.RED,120);
                       break;
                   case EYE_LASH:
                       EyeAngleAndScaleCalc.Bean bean = new EyeAngleAndScaleCalc.Bean();
                       bean.topP1 = new Point(90,148);
                       bean.topP2 = new Point(246,83);
                       bean.topP3 = new Point(405,136);
                       bean.bottomP1 = new Point(45,8);
                       bean.bottomP2 = new Point(65,187);
                       bean.bottomP3 = new Point(342,32);
                       bean.resTop = "lash_res_top.png";
                       bean.resBottom = "lash_res_bottom.png";

                       EyeDraw.drawLash(context,canvas,bean,FacePoint.getLeftEyePoint(faceJson),80,false);
                       break;
                   case EYE_CONTACT:
                       Bitmap contact = BitmapUtils.getBitmapByAssetsName(context,"eye.png");
                       EyeDraw.drawContact(canvas,contact,FacePoint.getLeftEyePath(faceJson),
                               FacePoint.getLeftEyeCenter(faceJson),FacePoint.getLeftEyeRadius(faceJson),120);

                       Paint paint = new Paint();
                       paint.setColor(Color.WHITE);
                       paint.setStyle(Paint.Style.STROKE);
                       break;
                   case EYE_DOUBLE:
                       //双眼皮
                       EyeAngleAndScaleCalc.Bean doubleBean = new EyeAngleAndScaleCalc.Bean();
                       doubleBean.topP1 = new Point(285,288);
                       doubleBean.topP2 = new Point(459,213);
                       doubleBean.topP3 = new Point(633,288);

                       doubleBean.resTop = "double_eye.png";

                       EyeDraw.drawLash(context,canvas,doubleBean,FacePoint.getLeftEyePoint(faceJson),150,false);
                       break;
                   case EYE_LINE:
                       //眼线
                       EyeAngleAndScaleCalc.Bean lineBean = new EyeAngleAndScaleCalc.Bean();
                       lineBean.topP1 = new Point(298,276);
                       lineBean.topP2 = new Point(440,216);
                       lineBean.topP3 = new Point(604,266);

                       lineBean.resTop = "eye_line.png";

                       EyeDraw.drawLash(context,canvas,lineBean,FacePoint.getLeftEyePoint(faceJson),100,false);
                       break;
                   case EYE_SHADOW:
                       EyeAngleAndScaleCalc.Bean eyeShadow = new EyeAngleAndScaleCalc.Bean();
                       eyeShadow.topP1 = new Point(74,160);
                       eyeShadow.topP2 = new Point(155,102);
                       eyeShadow.topP3 = new Point(229,160);

                       eyeShadow.rect = new Rect(74,102,229,184);

                       eyeShadow.resTop = "eye_shadow.png";

                       EyeDraw.drawShadow(context,canvas,eyeShadow,FacePoint.getLeftEyePath(faceJson),FacePoint.getLeftEyePoint(faceJson),150);

                       break;
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Region> getMakeupList() {
        return makeupList;
    }
}
