package cug.school.sketchingtools.common;

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

    private Bitmap bitmap;

    //标志位，用于判断是否重绘bitmap
    private String Flag = " ";

    //声明画笔
    private Paint paint = new Paint();
    //声明存储路径的数组
    private ArrayList<Path> pathArrayList = new ArrayList<>();
    //声明画线路径
    private Path path;
    //声明存储当前点的数组
    private ArrayList<Point> pointArrayList = new ArrayList<>();
    //声明当前点
    private Point point;
    //标识，用于判断撤销和重置
    private String TAG = " ";

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
                point = new Point((int) event.getX(), (int) event.getY());
                pointArrayList.add(point);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                TAG = " ";
                pathArrayList.add(path);
                for (int i = pointArrayList.size() - 1; i >= 0; i--) {
                    pointArrayList.remove(i);
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        switch (Flag) {
            case "CAMERA":
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, getWidth(), getHeight()), paint);
                break;
            case "ALBUM":
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, getWidth(), getHeight()), paint);
                break;
            default:
                break;
        }

        //当前点数组的大小
        int pointSize = pointArrayList.size();
        for (int i = 0, j = 1; j < pointSize; i++, j++) {
            Point startPoint = pointArrayList.get(i);
            Point endPoint = pointArrayList.get(j);
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint);
        }

        int pathSize = pathArrayList.size();
        switch (TAG) {
            case "UNDO":
                if (pathSize != 0) {
                    pathArrayList.remove(pathSize - 1);
                    for (int i = 0; i < pathSize - 1; i++) {
                        canvas.drawPath(pathArrayList.get(i), paint);
                    }
                }
                break;
            case "RESET":
                for (int i = pathSize - 1; i >= 0; i--) {
                    pathArrayList.remove(i);
                }
                break;
            default:
                //如果路径数组不为空，则绘制当前路径
                for (int i = 0; i < pathSize; i++) {
                    canvas.drawPath(pathArrayList.get(i), paint);
                }
                break;
        }
    }

    //撤销一步
    public void unDo() {
        TAG = "UNDO";
        invalidate();
    }

    //重置
    public void reSet() {
        TAG = "RESET";
        invalidate();
    }

    //加载图片
    public void setBitmap(Bitmap bitmap, String Flag) {
        this.bitmap = bitmap;
        this.Flag = Flag;
        invalidate();
    }

    public void Recycle() {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    /**
     * 设置画笔的风格
     */
    private void paintStyle() {
        //画笔颜色为黑色
        paint.setColor(Color.BLACK);
        //画笔宽度为20
        paint.setStrokeWidth(20.0f);
        //抗锯齿
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        //画笔类型为实线
        paint.setStyle(Paint.Style.STROKE);
        //画笔交汇点为圆形
        paint.setStrokeJoin(Paint.Join.ROUND);
        //画笔两端为圆形
        paint.setStrokeCap(Paint.Cap.ROUND);
    }
}
