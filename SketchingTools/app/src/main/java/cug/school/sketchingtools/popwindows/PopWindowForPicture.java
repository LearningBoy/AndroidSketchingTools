package cug.school.sketchingtools.popwindows;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import cug.school.sketchingtools.R;

/**
 * 处理宝贝寻家中拍照按钮
 * 从底部弹出选择框，有从相册中选择，拍照和取消三个选项
 */
public class PopWindowForPicture extends PopupWindow{

    public PopWindowForPicture(Context context,View.OnClickListener itemsOnClick) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置加载PopWindow的视图
        View  WindowView = inflater.inflate(R.layout.popup_windows_for_picture, null);
        //打开相机按钮
        Button Open_Camera = (Button) WindowView.findViewById(R.id.open_camera);
        Open_Camera.setOnClickListener(itemsOnClick);
        //打开相册按钮
        Button Open_Picture = (Button) WindowView.findViewById(R.id.open_picture);
        Open_Picture.setOnClickListener(itemsOnClick);
        //隐藏PopWindow按钮
        Button Cancal = (Button) WindowView.findViewById(R.id.cancal);
        Cancal.setOnClickListener(itemsOnClick);

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

    }
}
