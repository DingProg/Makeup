package com.ding.makeup.utils;

import android.util.Log;

import com.ding.makeup.BuildConfig;

/**
 * author:DingDeGao
 * time:2019-08-23-15:01
 * function: default function
 */
public class TimeAopUtils {

    private static long startTime;

    public static void start(){
        startTime = System.currentTimeMillis();
    }

    public static long end(String tag,String msgPre){
       long end = System.currentTimeMillis() - startTime;
       if(BuildConfig.DEBUG){
           Log.i(tag,msgPre+"-耗时:"+ end);
       }
       return end;
    }
}
