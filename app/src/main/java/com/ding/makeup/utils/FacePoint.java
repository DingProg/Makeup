package com.ding.makeup.utils;

import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * author:DingDeGao
 * time:2019-08-06-18:01
 * function: default function
 */
public class FacePoint {


    /**
     * face地址  https://www.faceplusplus.com.cn/dense-facial-landmarks/#demo
     */

    /**
     * 关键点说明: https://console.faceplusplus.com.cn/documents/55107022
     */
    public static Path getMouthPath(String faceJson){
        try {
            JSONObject jsonObject = new JSONObject(faceJson);
            JSONObject mouthJson = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("mouth");

             Path outPath = new Path();
             Path inPath = new Path();

            Point start = getPointByJson(mouthJson.getJSONObject("upper_lip_0"));
            outPath.moveTo(start.x,start.y);
             for(int i = 1;i < 18;i++){
                 Point pointByJson = getPointByJson(mouthJson.getJSONObject("upper_lip_" + i));
                 outPath.lineTo(pointByJson.x,pointByJson.y);
             }

            for(int i = 16;i > 0;i--){
                Point pointByJson = getPointByJson(mouthJson.getJSONObject("lower_lip_" + i));
                outPath.lineTo(pointByJson.x,pointByJson.y);
            }
            outPath.close();


            Point inStart = getPointByJson(mouthJson.getJSONObject("upper_lip_32"));
            inPath.moveTo(inStart.x,inStart.y);

            for(int i = 46;i < 64;i++){
                Point pointByJson = getPointByJson(mouthJson.getJSONObject("upper_lip_" + i));
                inPath.lineTo(pointByJson.x,pointByJson.y);
            }

            for(int i = 63;i >= 46;i--){
                Point pointByJson = getPointByJson(mouthJson.getJSONObject("lower_lip_" + i));
                inPath.lineTo(pointByJson.x,pointByJson.y);
            }

            //取不同的地方
            outPath.op(inPath, Path.Op.DIFFERENCE);
            return  outPath;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int getLeftEyeRadius(String faceJson){
        try {
            JSONObject jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("left_eye");

            return eye.optInt("left_eye_pupil_radius");
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static Point getLeftEyeCenter(String faceJson){
        try {
            JSONObject jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("left_eye");

            return getPointByJson(eye.getJSONObject("left_eye_pupil_center"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Point getRightEyeCenter(String faceJson){
        try {
            JSONObject jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("right_eye");

            return getPointByJson(eye.getJSONObject("right_eye_pupil_center"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static Path getLeftEyePath(String faceJson){
        try {
            JSONObject jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("left_eye");

            Path path = new Path();
            Point start = getPointByJson(eye.getJSONObject("left_eye_0"));
            path.moveTo(start.x,start.y);
           for(int i= 1;i< 63;i++){
               Point point = getPointByJson(eye.getJSONObject("left_eye_"+i));
               path.lineTo(point.x,point.y);
           }
           path.close();
           return  path;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Point> getLeftEyePoint(String faceJson){
        try {
            JSONObject jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("left_eye");

            List<Point> list = new ArrayList<>();
            for(int i= 0;i< 63;i++){
                Point point = getPointByJson(eye.getJSONObject("left_eye_"+i));
                list.add(point);
            }
            return  list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static Path getLeftEyeBrow(String faceJson){
        try {
            JSONObject jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("left_eyebrow");

            Path path = new Path();
            Point start = getPointByJson(eye.getJSONObject("left_eyebrow_0"));
            path.moveTo(start.x,start.y);
            for(int i= 1;i< 64;i++){
                Point point = getPointByJson(eye.getJSONObject("left_eyebrow_"+i));
                path.lineTo(point.x,point.y);
            }
            path.close();
            return  path;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static Path landmark(String faceJson){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("face");

            Path path = new Path();
            Point start = getPointByJson(eye.getJSONObject("face_contour_left_0"));
            path.moveTo(start.x,start.y);
            for(int i= 1;i< 64;i++){
                Point point = getPointByJson(eye.getJSONObject("face_contour_left_"+i));
                path.lineTo(point.x,point.y);
            }

            for(int i= 144;i>= 72;i--){
                Point point = getPointByJson(eye.getJSONObject("face_hairline_"+i));
                path.lineTo(point.x,point.y);
            }
            path.close();
            return  path;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    public static List<Point> getLeftFacePoint(String faceJson){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(faceJson);
            JSONObject face = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("face");
            List<Point> list = new ArrayList<>();
            for(int i= 0;i< 64;i++){
                Point point = getPointByJson(face.getJSONObject("face_contour_left_"+i));
                list.add(point);
            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Point> getRightFacePoint(String faceJson){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(faceJson);
            JSONObject face = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("face");
            List<Point> list = new ArrayList<>();
            for(int i= 0;i< 64;i++){
                Point point = getPointByJson(face.getJSONObject("face_contour_right_"+i));
                list.add(point);
            }

            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Point getCenterPoint(String faceJson){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(faceJson);
            JSONObject center = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("nose");
            return getPointByJson(center.getJSONObject("nose_midline_30"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Path getBlush(String faceJson){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(faceJson);
            JSONObject eye = jsonObject.getJSONObject("face").getJSONObject("landmark").getJSONObject("face");

            Path path = new Path();
            Point start = getPointByJson(eye.getJSONObject("face_contour_left_0"));
            path.moveTo(start.x,start.y);
            for(int i= 1;i< 64;i++){
                Point point = getPointByJson(eye.getJSONObject("face_contour_left_"+i));
                path.lineTo(point.x,point.y);
            }

            Point leftTop = getPointByJson(eye.getJSONObject("face_contour_left_63"));
            Point rightTop = getPointByJson(eye.getJSONObject("face_contour_right_63"));
            path.moveTo((leftTop.x + rightTop.x)/2.0f,leftTop.y);
            path.close();
            return  path;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }



    public static Point getPointByJson(JSONObject jsonObject){
        Point point = new Point(jsonObject.optInt("x"),jsonObject.optInt("y"));
        return point;
    }


    public static String getFaceJson(Context context,String facePointJson){
        InputStream input = null;
        try {
            input = context.getAssets().open(facePointJson);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte []bytes= new byte[8 * 1024];
            int len ;
            while ((len = input.read(bytes)) != -1){
                byteArrayOutputStream.write(bytes,0,len);
            }

            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
