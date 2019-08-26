博客地址: [Android：让你的“女神”逆袭，代码撸彩妆（画妆）](https://juejin.im/post/5d4bd2536fb9a06b1d212f72)

导读: 本文使用代码撸一个你心目中的“女神”，代码上彩妆。
技术主要内容是Canvas的应用.

# 背景
最近刷抖音，看到一些大汉变“女神”，这化妆可以称之为逆袭啊，大汉变萝莉.

作为技术，大部分是男生，并且经常有男生被女票怼我的口红有多少色号，那是一样的红色吗？

为了广大男同胞能好好的“活在”女票跟前，今天来讲述一下【化妆】,用代码撸一个好看的女票.


# 效果
先上效果在说吧,学习抖音的化妆教程方式，就画一半，方便形成对比，效果如下:

![](https://user-gold-cdn.xitu.io/2019/8/9/16c743bb13f971b9?w=456&h=494&f=png&s=292505)


如果正在看篇文章的人是个妹子，你应该很清楚画了些什么吧？为了照顾广大爷们，先讲一下画了些什么吧.

直接看代码吧
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
```
女程序员们，你们看出这么多来了吗？其实我也是挺佩服我自己的，一个男生知道那么多，吓坏了我很多小伙伴，宅男的世界你们不懂。


# 磨皮
砍柴不误摸到工，我们知道，一般的痘痘用粉底是盖不住的，那么先来一次磨皮吧，把"底板"搞干净了,我们使用一个高通滤波器（去掉低频信号，来达到保留细节的效果） + Curve Adjustment某些频率应用调整 然后在融合来达到磨皮的目的
流程大概是这样的

![](https://user-gold-cdn.xitu.io/2019/8/8/16c70ef02667823c?w=2612&h=947&f=png&s=955518)
（图片来源下面所述库里）

效果如下
080819243542_01.png
![](https://user-gold-cdn.xitu.io/2019/8/8/16c70f9a093c00a0?w=1132&h=600&f=png&s=770070)

本文就没有在撸一个这样的库，直接使用了github开源的磨皮库.使用 [HighPassSkinSmoothing](https://github.com/msoftware/HighPassSkinSmoothing-Android)

但是我这里为了形成对比，所以只取了左边的脸
```java
 Bitmap leftAndRightBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
 Canvas canvas = new Canvas(leftAndRightBitmap);
 //+3,为了弥补 int值相除精读损失，让左边多一些
Rect left = new Rect(0,0,bitmap.getWidth()/2 + 3,bitmap.getHeight());
Rect right = new Rect(bitmap.getWidth() - bitmap.getWidth()/2 ,0,bitmap.getWidth(),bitmap.getHeight());
  canvas.drawBitmap(result,left,left,null);
canvas.drawBitmap(bitmap,right,right,null);
```

# 人脸关键点检测
往人脸上化妆，拿整张照片的磨皮肯定不行啊，我们需要精准的人脸，那就需要人脸识别技术,开源的库也有一些，但是精度有待加强，所以本文选用了商用的人脸关键点检测技术，大概看了一下，有这么几家人脸识别技术做的还可以
- 商汤
- Face++
- 百度
- 虹软

他们的技术，人脸精度，使用价格，在此不做评论.
本文选用了Face++的稠密关键点检测. 为了方便去见，没有下载其SDK，使用了[网页版本](https://www.faceplusplus.com.cn/dense-facial-landmarks/#demo)的关键点检测，可以上传本地照片，然后把数据拿下来.

![](https://user-gold-cdn.xitu.io/2019/8/9/16c74269e4f94688?w=2312&h=1406&f=png&s=1893905)

右侧有关键点的json，可以直接复制下来,供后续使用.
```json
{
  "time_used": 140,
  "request_id": "1565152700,b5efc234-055c-4109-8899-e7bd0b9d1d63",
  "face": {
    "landmark": {
      "left_eye": {
        "left_eye_43": {
          "y": 170,
          "x": 140
        },
        "left_eye_42": {
          "y": 170,
          "x": 141
        },
        "left_eye_41": {
          "y": 170,
          "x": 142
        },
        "left_eye_40": {
          "y": 170,
          "x": 143
        },
        "left_eye_47": {
          "y": 170,
          "x": 136
        },
        "left_eye_46": {
          "y": 170,
          "x": 137
        }
       }
     }
   }
}
```
如果商用建议购买其SDK。
有了这些点，我们就可以接下来“画”妆了。

# 粉底
有了磨皮，但是不够白啊，上述的库里其实包含了美白，它是对整个图片进行处理，叠加白色滤波，但效果很差，肯定不是我们想要的。但是有了人脸检测的点，那我们就好办了，涂一层粉底吧.(女生还要先涂水啊，乳啊什么，照片上不了水了....)

![](https://user-gold-cdn.xitu.io/2019/8/8/16c71026de4e21d9?w=2366&h=806&f=png&s=665499)
看Face++的文档我们可以知道json里面的关键点为face_contour_left_和face_hairline_为脸的区域.

直接拿出左边脸的区域.
```java
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
```
有了左边区域，只需要一个画笔就可以画上去(原图就可以是画板 new Canvas(originBitmap))，那我们正常直接涂一层白色，肯定不行，会吓坏小伙伴的，那白色加透明可以吗？那我们试试吧
```java
 Canvas canvas = new Canvas(originBitmap);
 Paint paint = new Paint();
 paint.setColor(Color.WHITE);
 paint.setAlpha(50);
 paint.setStyle(Paint.Style.FILL);
 canvas.drawPath(facePath,paint);
```
效果

![](https://user-gold-cdn.xitu.io/2019/8/9/16c742d4c8d74ac9?w=481&h=505&f=png&s=300474)
感觉挺假的，我们知道，画笔是可以设置成高斯模糊的，那就来试试吧.
```java
  private static Bitmap createMask(final Path path, int color, @Nullable PointF position, int alpha, int blur_radius) {
        if (path == null || path.isEmpty())
            return null;

        RectF bounds = new RectF();
        path.computeBounds(bounds, true);

        int width = (int) bounds.width();
        int height = (int) bounds.height();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  // mutable
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.FILL);
        path.offset(-bounds.left, -bounds.top);
        canvas.drawPath(path, paint);
        if (position != null) {
            position.x = bounds.left;
            position.y = bounds.top;
        }
        return bitmap;
    }
```
事实证明这样是可以的，但是效果还是不咋行，那我们在用原图来做一次渐变，刚好可以达到效果
```java
  private static Bitmap getGradientBitmapByXferomd(Bitmap originBitmap, float radius){
        if(radius < 10) radius = 10;
        Bitmap canvasBitmap = Bitmap.createBitmap(originBitmap.getWidth(),originBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBitmap);
        Paint paint = new Paint();

        BitmapShader bitmapShader = new BitmapShader(originBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        RadialGradient radialGradient = new RadialGradient(originBitmap.getWidth() / 2, originBitmap.getHeight() / 2,
                radius, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        paint.setShader(new ComposeShader(bitmapShader,radialGradient,new PorterDuffXfermode(PorterDuff.Mode.DST_IN)));
        canvas.drawRect(new Rect(0,0,canvasBitmap.getWidth(),canvasBitmap.getHeight()), paint);
        return canvasBitmap;
    }
```

![](https://user-gold-cdn.xitu.io/2019/8/9/16c7432ab73f0c6c?w=705&h=752&f=png&s=568146)

# 口红
关于口红也只是仅仅画上一层颜色,有了画笔，就可以和粉底一样的实现方式.

![](https://user-gold-cdn.xitu.io/2019/8/9/16c7433d6fd69f5c?w=2232&h=560&f=png&s=510474)

先看一下怎么连接的区域吧，为了方便，我直接采用了把外面的区域连接起来，然后在去做一次diff就可以了，代码如下
```java
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
```
Path.op()方法需要在API 19及以上才可以使用，如果使用了低版本的api,可以直接使用canvas.clipPath().


# 腮红
只有粉底，那看上去，还是有点假，那是不是需要用画笔画上一个腮红呢？但是形状什么，不好搞定，所以选择了直接使用腮红素材，直接贴上去.

![](https://user-gold-cdn.xitu.io/2019/8/9/16c743d8ed5b871d?w=319&h=283&f=png&s=40957)

实现也相对容易一些.
```java
  public static void drawBlush(Canvas canvas, Bitmap faceBlush, Path path, int alpha) {
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        RectF rectF = new RectF();
        path.computeBounds(rectF,true);
        canvas.drawBitmap(faceBlush,null,rectF,paint);

    }
```

# 眉毛
眉毛这个其实困扰了我很长时间，因为要把底部的眉毛给扣了，在装新的眉毛在上面，不然可能完全盖不住，眉形变化，识别准确率，会导致效果的直接变化.尝试了很多方法其中OpenCV里有一个著名的inpaint方法的图片修复方法，看别人写的去书印demo，也都还行，但是放到这里去眉毛，效果很差，是因为我使用不对，还是什么问题，有大神可以指点，提取周边的皮肤颜色去掉原来的眉毛.

![](https://user-gold-cdn.xitu.io/2019/8/9/16c74486c733b2ce?w=1584&h=790&f=png&s=1879691)

最终还是放弃了去掉原来的眉毛,直接覆盖眉毛.

![](https://user-gold-cdn.xitu.io/2019/8/9/16c744952bbab16d?w=2214&h=526&f=png&s=438340)


![](https://user-gold-cdn.xitu.io/2019/8/9/16c744e72e919a8d?w=242&h=83&f=png&s=16499)

```java
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

     public static void draw(Canvas canvas, Bitmap eyeBrowRes, Path path, int alpha){
        Paint paint = new Paint();
        paint.setAlpha(alpha);

        RectF rectF = new RectF();
        path.computeBounds(rectF,true);

        canvas.drawBitmap(eyeBrowRes,new Rect(0,0,eyeBrowRes.getWidth(),eyeBrowRes.getHeight() - 30),rectF,paint);
    }

```
最终效果

![](https://user-gold-cdn.xitu.io/2019/8/9/16c744b78893cb7e?w=316&h=136&f=png&s=51543)
但是文中的开始给的效果那张照片，因为识别偏差，导致效果不太好.

# 眼睛（睫毛，眼影，双眼皮，眼线，美瞳）
眼睛部分是最复杂的部分了，因为可以画的实在是太多了.

这就将两个地方的实现，其他具体实现可以参考[实际代码](https://github.com/DingProg/Makeup),先看一下这些不是主要的素材吧

![](https://user-gold-cdn.xitu.io/2019/8/9/16c744f38d89f18a?w=429&h=129&f=png&s=18552)
![](https://user-gold-cdn.xitu.io/2019/8/9/16c744f646f94cb6?w=301&h=163&f=png&s=42059)
![](https://user-gold-cdn.xitu.io/2019/8/9/16c744f9edb79a2e?w=437&h=156&f=png&s=34671)

## 美瞳
要向眼睛里画美瞳，那么我们首先要有这个区域，区域人脸关键点已经给了，那么，我们知道，人的眼睛一般是椭圆性的，不可能直接是圆形的，所以画的时候，需要和眼睛的区域做一个交集来得到结果.

![](https://user-gold-cdn.xitu.io/2019/8/9/16c7456ab51ba8a5?w=482&h=452&f=png&s=289179)

![](https://user-gold-cdn.xitu.io/2019/8/9/16c744ee673d64ec?w=225&h=227&f=png&s=52643)

```java
    public static void drawContact(Canvas canvas, Bitmap contactBitmap, Path eyePath, Point centerPoint, int eyeRadius, int alpha) {
        Path contactPath = new Path();
        contactPath.addCircle(centerPoint.x,centerPoint.y,eyeRadius, Path.Direction.CCW);
        //重点地方，做交集得到结果
        contactPath.op(eyePath, Path.Op.INTERSECT);

        RectF bounds = new RectF();
        contactPath.computeBounds(bounds,true);
        bounds.offset(1,0);
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(contactBitmap,new Rect(0,30,contactBitmap.getWidth(),contactBitmap.getHeight() - 60),bounds,paint);
    }
```

## 睫毛
![](https://user-gold-cdn.xitu.io/2019/8/9/16c745014dd63d5d?w=436&h=187&f=png&s=43377)
![](https://user-gold-cdn.xitu.io/2019/8/9/16c745030c097df5?w=342&h=96&f=png&s=15528)

我们知道，睫毛有上睫毛和下睫毛,那么怎么把这个眉毛画上去呢？
其实我们知道，一般把图片绘制到目标区域需要经过，平移，旋转，缩放来进行.

睫毛我们选取了素材上的三个点，和眼睛上的三个点来做上述的三个操作.

![](https://user-gold-cdn.xitu.io/2019/8/9/16c74594202b12c9?w=866&h=328&f=png&s=137907)

有了这三个点，我们就可以计算宽高比，角度，使用三角函数可以很容易计算得到.

### 旋转角度
使用人眼睛上对应的三个点来计算旋转角度,(如果人的头像是正的，可以不用计算，但是人可能偏头，什么，需要计算旋转角度，来warp)
```java
 /**
     * @param p1 三角形顶点
     * @param p2 三角形顶点
     * @param p3 三角形顶点
     * @return 三角形顶点p3 到 p1,p3垂直高度
     */
    public double getTriangleHeight(Point p1, Point p2, Point p3) {
        int a = p1.x;
        int b = p1.y;
        int c = p2.x;
        int d = p2.y;
        int e = p3.x;
        int f = p3.y;
        //计算三角形面积
        double S = (a * d + b * e + c * f - a * f - b * c - d * e) / 2;
        int lengthSquare = (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
        return Math.abs(2 * S / Math.sqrt(lengthSquare));
    }

     //获取坐标轴内两个点间的距离
    public double getLength(Point p1, Point p2) {
        double diff_x = Math.abs(p1.x - p2.x);
        double diff_y = Math.abs(p1.y - p2.y);
        //两个点在 横纵坐标的差值与两点间的直线 构成直角三角形。length_pow等于该距离的平方
        double length_pow = Math.pow(diff_x, 2) + Math.pow(diff_y, 2);
        double sqrt = Math.sqrt(length_pow);
        return sqrt == 0?0.001f:(float) sqrt;
    }

    static double pi180 = 180 / Math.PI;
    public double getAngle(Point p1, Point p2, Point p3) {
        double _cos1 = getCos(p1, p2, p3);//第一个点为顶点的角的角度的余弦值
        return 90 - Math.acos(_cos1) * pi180;
    }
```

### 宽高比旋转角度
有了角度，那么我们在计算宽高比.
```java
 /**
     * @param targetP1 缩放目标线段点p1
     * @param targetP2 缩放目标线段点p2
     * @param P1       待缩放线段点p1
     * @param P2       待缩放线段点p2
     * @return 水平高度比值
     */
    public double computeScaleX(Point targetP1, Point targetP2, Point P1, Point P2) {
        int targetLengthSquare = (targetP1.x - targetP2.x) * (targetP1.x - targetP2.x) + (targetP1.y - targetP2.y) * (targetP1.y - targetP2.y);
        int sourceLengthSquare = (P1.x - P2.x) * (P1.x - P2.x) + (P1.y - P2.y) * (P1.y - P2.y);
        double scale = targetLengthSquare * 1.0 / sourceLengthSquare;
        return Math.sqrt(scale);
    }

    /**
     * @param targetP1 缩放目标三角形顶点
     * @param targetP2 缩放目标三角形顶点
     * @param targetP3 缩放目标三角形顶点
     * @param P1       待缩放三角形顶点
     * @param P2       待缩放三角形顶点
     * @param P3       待缩放三角形顶点
     * @return 垂直高度比值
     */
    public double computeScaleY(Point targetP1, Point targetP2, Point targetP3, Point P1, Point P2, Point P3) {
        double targetHeight = getTriangleHeight(targetP1, targetP2, targetP3);
        double sourceHeight = getTriangleHeight(P1, P2, P3);
        return targetHeight / sourceHeight;
    }
```

### 平移
因为我们的图形是巨型，不可能从开始位置往上画，那就需要把画的位置通过平移，来达到第一个点的位置和对应位置的点，对应上.
``` java
eyeAngleAndScaleCalc.topP1.x - (int) (bean.topP1.x * eyeAngleAndScaleCalc.topScaleX),
                eyeAngleAndScaleCalc.topP1.y - (int) (bean.topP1.y * eyeAngleAndScaleCalc.topScaleY)
```

有了这些步骤，那既可以直接合成绘制了,代码如下
```java
 public static void drawLash(Context context, Canvas canvas, EyeAngleAndScaleCalc.Bean bean, List<Point> pointList, int alpha, boolean needMirror) {
        EyeAngleAndScaleCalc eyeAngleAndScaleCalc = new EyeAngleAndScaleCalc(pointList,bean);

        Paint paint = new Paint();
        paint.setAlpha(alpha);

        Bitmap resTopBitmap = BitmapUtils.getBitmapByAssetsName(context,bean.resTop);
        Bitmap scaledBitmapTop = Bitmap.createScaledBitmap(resTopBitmap, (int) (resTopBitmap.getWidth() * eyeAngleAndScaleCalc.topScaleX + 0.5),
                (int) (resTopBitmap.getHeight() * eyeAngleAndScaleCalc.topScaleY + 0.5), true);
        resTopBitmap.recycle();


        Bitmap resBottomBitmap = null;
        Bitmap scaledBitmapBottom = null;
        if (!TextUtils.isEmpty(bean.resBottom)) {
            resBottomBitmap = BitmapUtils.getBitmapByAssetsName(context,bean.resBottom);
            scaledBitmapBottom = Bitmap.createScaledBitmap(resBottomBitmap, (int) (resBottomBitmap.getWidth() * eyeAngleAndScaleCalc.bottomScaleX + 0.5),
                    (int) (resBottomBitmap.getHeight() * eyeAngleAndScaleCalc.bottomScaleY + 0.5), true);
            resBottomBitmap.recycle();
        }

        if (needMirror) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1);   //镜像水平翻转
            scaledBitmapTop = Bitmap.createBitmap(scaledBitmapTop, 0, 0, scaledBitmapTop.getWidth(), scaledBitmapTop.getHeight(), matrix, true);
            if (resBottomBitmap != null) {
                scaledBitmapBottom = Bitmap.createBitmap(scaledBitmapBottom, 0, 0, scaledBitmapBottom.getWidth(), scaledBitmapBottom.getHeight(), matrix, true);
            }
        }

        canvas.save();
        //canvas.rotate(eyeAngleAndScaleCalc.getTopEyeAngle(), eyeAngleAndScaleCalc.topP1.x, eyeAngleAndScaleCalc.topP1.y);
        canvas.drawBitmap(scaledBitmapTop,
                eyeAngleAndScaleCalc.topP1.x - (int) (bean.topP1.x * eyeAngleAndScaleCalc.topScaleX),
                eyeAngleAndScaleCalc.topP1.y - (int) (bean.topP1.y * eyeAngleAndScaleCalc.topScaleY), paint);
        canvas.restore();

        if (scaledBitmapBottom != null) {
            canvas.save();
            canvas.rotate(eyeAngleAndScaleCalc.getBottomEyeAngle(), eyeAngleAndScaleCalc.bottomP1.x, eyeAngleAndScaleCalc.bottomP1.y);
            canvas.drawBitmap(scaledBitmapBottom, eyeAngleAndScaleCalc.bottomP1.x,
                    eyeAngleAndScaleCalc.bottomP1.y - (int) (bean.bottomP1.y * eyeAngleAndScaleCalc.bottomScaleY), paint);
            canvas.restore();
            scaledBitmapBottom.recycle();
        }
        scaledBitmapTop.recycle();
    }
```

眼睛部分，略微复杂一些，具体代码可以查看 [Github Makeup](https://github.com/DingProg/Makeup) ,如果你觉得还可以，可以给一个star吗？谢谢

# 其他
我们知道，上述内容只是对脸上进行了一些化妆，那要成为真正的“美女”，可能还要打上问号？
那什么样的化妆才是真正的美女呢，一般是底子好的人。在加上化妆就更漂亮了，那一张照片，要变的底子好，一般有那些方式呢？
这里提供一些思路（包含美体）
```java
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
如果只针对脸部，那么就只需要，磨皮，美白，祛斑，大眼，瘦脸等功能了.

# 文末
今天的文章分享到这就结束了，这些算法，目前知网论文库里都有，可以查看后轻松实现.
如果后续还想在搞就推出下篇博客，打造完美身材.

上述内容资源,使用完后请在24小时内删除，如果有侵权请联系作者立刻删除.


还有一篇比较好玩的博客推荐给大家，Flutter版本的 [Flutter PIP（画中画）效果的实现](https://juejin.im/post/5d37fb1af265da1b695da1a9)

![](https://user-gold-cdn.xitu.io/2019/8/8/16c70a85c03c2099?w=385&h=800&f=png&s=271913)


# 推荐阅读
[Flutter高性能原理](https://juejin.im/post/5d3be5fd6fb9a07ead5a4243)
[Android 绘制原理浅析【干货】](https://juejin.im/post/5d4176365188255d8919be91)

