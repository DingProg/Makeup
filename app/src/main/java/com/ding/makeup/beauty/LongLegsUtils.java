package com.ding.makeup.beauty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * @author by dingdegao
 *         time 2017/10/13 15:38
 *         function:  大长腿 utils
 */

public class LongLegsUtils {

    /**
     * 大长腿算法
     *
     * @param bitmap 原来的bitmap
     * @param rect   拉伸区域
     * @param strength   拉伸力度 [0.04f,0.10f]
     * @return 之后的图片
     */
    public static Bitmap longLeg(Bitmap bitmap, Rect rect, float strength) {
        //将图像分成多少格
        int WIDTH = 200;
        int HEIGHT = 200;

        //交点坐标的个数
        int COUNT = (WIDTH + 1) * (HEIGHT + 1);

        //用于保存COUNT的坐标
        float[] verts = new float[COUNT * 2];


        float bmWidth = bitmap.getWidth();
        float bmHeight = bitmap.getHeight();

        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                //X轴坐标 放在偶数位
                verts[index * 2] = fx;
                //Y轴坐标 放在奇数位
                verts[index * 2 + 1] = fy;
                index += 1;
            }
        }

        int centerY = rect.centerY(),totalHeight = bitmap.getHeight();
        if(totalHeight < 5) return bitmap;
        warpLeg(COUNT,verts,centerY,totalHeight,rect.height(),strength);
        warpLeg(COUNT,verts,centerY,totalHeight,rect.height(),strength);

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        return resultBitmap;
    }

    private static void warpLeg(int COUNT, float verts[], float centerY,int totalHeight,float region,float strength) {

        float  r = region / 2; //缩放区域力度

        for (int i = 0; i < COUNT * 2; i += 2) {
            //计算每个坐标点与触摸点之间的距离
            float dy = verts[i + 1] - centerY;
            double e = (totalHeight - Math.abs(dy)) / totalHeight;
            if(Math.abs(dy) < r){
                //拉长比率
                double pullY = e * dy * strength;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }else if(Math.abs(dy) < 2 * r || dy > 0){
                double pullY = e * e * dy * strength;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }else if(Math.abs(dy) < 3 * r){
                double pullY = e * e * dy * strength /2;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }else {
                double pullY = e * e * dy * strength /4;
                verts[i + 1] = (float) (verts[i + 1] + pullY);
            }

            //没问题的
//            if(Math.abs(dy) < r){
//                //拉长比率
//                double pullY = e * dy * strength;
//                verts[i + 1] = (float) (verts[i + 1] + pullY);
//            }else if(Math.abs(dy) < 2 * r){
//                double pullY = e * e * dy * strength;
//                verts[i + 1] = (float) (verts[i + 1] + pullY);
//            }
        }
    }
}
