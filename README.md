# 项目介绍  

本项目是一个Android Project，用Canvas给人脸化妆(画妆)的APP演示项目

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

更多演示效果请直接查看下方原理文章，或者clone代码到你本地，直接运行APP.

# 演示APP主要内容包括

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

    INPAINT(1,"祛斑"),
    SMALLFACE(2,"瘦脸"),
    LONGLEG(3,"大长腿增高"),
    EYE(4,"眼睛放大"),
    BREST(5,"丰胸"),
    WHITE(7,"美白"),
    MAKEUP(8,"美妆"),
    SMALLBODY(9,"瘦脸瘦身");

    private int type;
    private String name;

    BeautyType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
```

# 原理实现部分

[Android：让你的“女神”逆袭，代码撸彩妆（画妆)](https://github.com/DingProg/Makeup/blob/master/doc/doc1.md)  
[Android：让你的“女神”逆袭，代码撸彩妆 2（大眼，瘦脸，大长腿）](https://github.com/DingProg/Makeup/blob/master/doc/doc2.md)
