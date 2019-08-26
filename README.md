# 项目介绍  

本项目是一个使Android Project，用Canvas给人脸化妆(画妆)的APP 演示项目

# 部分效果展示

![](https://user-gold-cdn.xitu.io/2019/8/9/16c743bb13f971b9?w=456&h=494&f=png&s=292505)   

![](https://user-gold-cdn.xitu.io/2019/8/26/16ccc98a45420279?w=640&h=960&f=gif&s=1226488)  

![](https://user-gold-cdn.xitu.io/2019/8/23/16cbed58a8b2a665?w=478&h=682&f=png&s=348487)   

更多效果，直接查看APP，或者下面描述的原理文章，有更多效果展示.

## 主要内容包括

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

[Android：让你的“女神”逆袭，代码撸彩妆（画妆)]()  
[Android：让你的“女神”逆袭，代码撸彩妆 2（大眼，瘦脸，大长腿）]()
