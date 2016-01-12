package cug.school.sketchingtools.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 *
 */
public class DrawSketchView extends View {

    //声明画笔
    private Paint paint = new Paint();
    //声明存储路径的数组
    private ArrayList<Path> pathArrayList = new ArrayList<>();
    //声明画线路径
    private Path path = new Path();

    public DrawSketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置画笔风格
        paintStyle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                pathArrayList.add(path);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //如果路径数组不为空，则绘制当前路径
        if (pathArrayList.size() != 0) {
            int i = pathArrayList.size() - 1;
            Path path = pathArrayList.get(i);
            canvas.drawPath(path, paint);
        }
        //在画布中保存当前路径
        canvas.save();
    }

    /**
     * 设置画笔的风格
     */
    private void paintStyle(){
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
