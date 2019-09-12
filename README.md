# 项目介绍  

本项目是一个Android Project，用Canvas给人脸化妆(画妆)的APP演示项目  

主要内容包括：
- 唇彩，美瞳，粉底，眼影，腮红，眼线，双眼皮，眉毛等，能画的妆，都画了
- 利用图形局部变形算法进行 大眼，瘦脸，丰胸，大长腿等
- 磨平/美白

# 部分效果展示
美妆  
![](https://github.com/DingProg/Makeup/blob/master/doc/3.png)
![](https://github.com/DingProg/Makeup/blob/master/doc/5.png)      
大眼  
![](https://github.com/DingProg/Makeup/blob/master/doc/1.png)  
瘦脸  
![](https://github.com/DingProg/Makeup/blob/master/doc/2.png)  
大长腿  
![](https://github.com/DingProg/Makeup/blob/master/doc/4.png)   


![](https://github.com/DingProg/Makeup/blob/master/doc/smallface.gif)

更多演示效果请直接查看下方原理文章，或者直接下载 [演示APP Release V1.0.0版本](https://github.com/DingProg/Makeup/releases)   

如果你要看OpenCV相关的(换证件照背景/污点修复)，可以切换到分支[with-photo-changecolor](https://github.com/DingProg/Makeup/tree/with-photo-changecolor)   
相关的演示APP为 [带替换证件照背景/污点修复版本](https://github.com/DingProg/Makeup/releases)

# 演示APP 主要实现了的部分为
```java
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

public enum BeautyType {

    SMALLFACE(2,"瘦脸"),
    LONGLEG(3,"大长腿增高"),
    EYE(4,"眼睛放大"),
    BREST(5,"丰胸"),
    WHITE(7,"美白"),
    SMALLBODY(9,"瘦脸瘦身");

    private int type;
    private String name;

    BeautyType(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
```

# 原理

[Android：让你的“女神”逆袭，代码撸彩妆（画妆)](https://github.com/DingProg/Makeup/blob/master/doc/doc1.md)  
[Android：让你的“女神”逆袭，代码撸彩妆 2（大眼，瘦脸，大长腿）](https://github.com/DingProg/Makeup/blob/master/doc/doc2.md)

# 声明  
本项目是演示性及学习性项目，项目中所用素材对于直接拿去商用所造成的侵权，概不负责.