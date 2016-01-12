package cug.school.sketchingtools.popwindows;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import cug.school.sketchingtools.R;

/**
 * 处理宝贝寻家中拍照按钮
 * 从底部弹出选择框，有从相册中选择，拍照和取消三个选项
 */
public class PopWindowForPicture extends PopupWindow implements View.OnClickListener {

    public PopWindowForPicture(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置加载PopWindow的视图
        final View WindowView = inflater.inflate(R.layout.popup_windows_for_picture, null);
        //打开相机按钮
        Button Open_Camera = (Button) WindowView.findViewById(R.id.open_camera);
        Open_Camera.setOnClickListener(this);
        //打开相册按钮
        Button Open_Picture = (Button) WindowView.findViewById(R.id.open_picture);
        Open_Picture.setOnClickListener(this);
        //隐藏PopWindow按钮
        Button Cancal = (Button) WindowView.findViewById(R.id.cancal);
        Cancal.setOnClickListener(this);

        //将视图加载到PopWindow
        this.setContentView(WindowView);
        //设置PopWindow的宽度
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        //设置PopWindow的高度
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        //实例化一个colorDrawable颜色为纯白色
        ColorDrawable colorDrawable = new ColorDrawable(0xFFFFFFFF);
        //设置PopWindow的背景
        this.setBackgroundDrawable(colorDrawable);
        //设置PopWindow的显示和隐藏动画
        this.setAnimationStyle(R.style.popup_window_for_picture);
        //PopWindow可点击
        this.setFocusable(true);
        //点击到PopWindow之外，隐藏PopWindow
        WindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = WindowView.getHeight();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_camera:
                break;
            case R.id.open_picture:
                break;
            case R.id.cancal:
                dismiss();
                break;
            default:
                break;
        }
    }
}
