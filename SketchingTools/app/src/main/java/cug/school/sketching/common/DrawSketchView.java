package cug.school.sketching.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * 自定义绘制视图控件
 */
public class DrawSketchView extends View {

    //加载的图片或相机拍照的图片
    private Bitmap bitmap;
    //标志位，用于判断是否重绘bitmap
    private boolean Flag = false;
    //声明画笔
    private Paint paint_red = new Paint();
    private Paint paint_black = new Paint();

    //保存所有的点,用于在xml文件中展示
    private ArrayList<ArrayList<Point>> allPointArrayList = new ArrayList<>();
    //声明存储路径的数组
    private ArrayList<Path> pathArrayList = new ArrayList<>();
    //声明画线路径
    private Path path;
    //声明存储当前点的数组
    private ArrayList<Point> pointArrayList = new ArrayList<>();
    //声明当前点数组大小
    int pointSize = 0;
    //点数组中画线的开始点
    private Point startPoint = null;
    //点数组中画线的结束点
    private Point endPoint = null;
    //标识，用于判断撤销和重置
    private String TAG = " ";
    //记录图片添加时的序号
    private int ImageNumber;
    //图片的尺寸
    private Rect ImageRect;
    //当前视图的尺寸
    private RectF ScreenRect;

    //根据时间改变线条颜色
    long currentTime = System.currentTimeMillis();

    public DrawSketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置画笔风格
        paintStyle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                Point point = new Point((int) event.getX(), (int) event.getY());
                pointArrayList.add(point);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                TAG = " ";
                pathArrayList.add(path);
                ArrayList<Point> arrayList = new ArrayList<>();
                arrayList.addAll(pointArrayList);
                allPointArrayList.add(arrayList);
                int currentSize = allPointArrayList.size();
                for (int i = pointArrayList.size() - 1; i >= 0; i--) {
                    allPointArrayList.get(currentSize - 1).add(arrayList.get(i));
                }
                pointArrayList.clear();
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //画图
        if (Flag) {
            canvas.drawBitmap(bitmap, ImageRect, ScreenRect, paint_black);
        }

        //当前点数组的大小
        pointSize = pointArrayList.size();
        if (pointSize != 0) {
            for (int i = 0, j = 1; j < pointSize; i++, j++) {
                startPoint = pointArrayList.get(i);
                endPoint = pointArrayList.get(j);
                canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint_black);
            }
        } else {
            startPoint = null;
            endPoint = null;
        }

        //画出之前的路径
        int pathSize = pathArrayList.size();
        switch (TAG) {
            case "UNDO":
                if (pathSize != 0) {
                    if (ImageNumber == pathSize) {
                        ImageNumber = 0;
                    } else {
                        pathArrayList.remove(pathSize - 1);
                        allPointArrayList.remove(pathSize - 1);
                        pathSize--;
                    }
                    for (int i = 0; i < pathSize; i++) {
                        canvas.drawPath(pathArrayList.get(i), paint_black);
                    }
                }
                break;
            case "RESET":
                for (int i = pathSize - 1; i >= 0; i--) {
                    pathArrayList.remove(i);
                    allPointArrayList.remove(i);
                }
                break;
            default:
                //如果路径数组不为空，则绘制当前路径
                for (int i = 0; i < pathSize; i++) {
                    canvas.drawPath(pathArrayList.get(i), paint_black);
                }
                break;
        }

    }

    //撤销一步
    public void unDo() {
        TAG = "UNDO";
        if (ImageNumber == pathArrayList.size()) {
            Flag = false;
            Recycle();
        }
        invalidate();
    }

    //重置
    public void reSet() {
        TAG = "RESET";
        Flag = false;
        ImageNumber = 0;
        Recycle();
        invalidate();
    }

    //加载图片
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.ImageNumber = pathArrayList.size();
        //获得图片的宽高
        int Bitmap_Width = bitmap.getWidth();
        int Bitmap_Height = bitmap.getHeight();
        //设置要变换的图片信息
        this.ImageRect = new Rect(0, 0, Bitmap_Width, Bitmap_Height);
        //获得当前控件的宽高
        int View_Width = this.getWidth();
        int View_Height = this.getHeight();
        //设置图片要匹配的空间信息
        this.ScreenRect = new RectF(0, 0, View_Width, View_Height);
        Flag = true;
        invalidate();
    }

    public void Recycle() {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    /**
     * 设置画笔的风格
     */
    private void paintStyle() {
        //画笔颜色为黑色/红色
        paint_black.setColor(Color.BLACK);
        paint_red.setColor(Color.RED);
        //画笔宽度为20
        paint_black.setStrokeWidth(20.0f);
        paint_red.setStrokeWidth(20.0f);
        //抗锯齿
        paint_black.setAntiAlias(true);
        paint_red.setAntiAlias(true);
        //防抖动
        paint_black.setDither(true);
        paint_red.setDither(true);
        //画笔类型为实线
        paint_black.setStyle(Paint.Style.STROKE);
        paint_red.setStyle(Paint.Style.STROKE);
        //画笔交汇点为圆形
        paint_black.setStrokeJoin(Paint.Join.ROUND);
        paint_red.setStrokeJoin(Paint.Join.ROUND);
        //画笔两端为圆形
        paint_black.setStrokeCap(Paint.Cap.ROUND);
        paint_red.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * 返回当前画面的点
     */
    public ArrayList<ArrayList<Point>> getAllPoint() {
        return allPointArrayList;
    }

}
