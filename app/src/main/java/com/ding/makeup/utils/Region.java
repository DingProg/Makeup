package com.ding.makeup.utils;

/**
 * author:DingDeGao
 * time:2019-08-08-10:27
 * function: default function
 */
public enum Region {

    FOUNDATION("粉底"),
    BLUSH("腮红"),
    LIP("唇彩"),
    BROW("眉毛"),

    EYE_LASH("睫毛"),
    EYE_CONTACT("美瞳"),
    EYE_DOUBLE("双眼皮"),
    EYE_LINE("眼线"),
    EYE_SHADOW("眼影");



    private String name;

    Region(String name) {
        this.name = name;
    }
}
