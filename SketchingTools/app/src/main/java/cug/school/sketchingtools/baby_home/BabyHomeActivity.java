package cug.school.sketchingtools.baby_home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import cug.school.sketchingtools.R;
import cug.school.sketchingtools.popwindows.PopWindowForGraphic;
import cug.school.sketchingtools.popwindows.PopWindowForPicture;

public class BabyHomeActivity extends Activity {

    private Button Do_Something;
    private Button Find_Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_home_layout);
        initView();
    }

    private void initView() {
        //打开相机或者打开相册
        Button Open_Camera_Or_Picture = (Button) findViewById(R.id.open_camera_or_open_picture);
        Open_Camera_Or_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化PopWindow
                PopWindowForPicture popWindowForPicture = new PopWindowForPicture(BabyHomeActivity.this);
                //显示窗口
                popWindowForPicture.showAtLocation(BabyHomeActivity.this.findViewById(R.id.baby_home), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        //图形选择按钮
        final Button Choose_Picture = (Button) findViewById(R.id.choose_picture);
        Choose_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化PopWindow
                PopWindowForGraphic popWindowForGraphic = new PopWindowForGraphic(BabyHomeActivity.this);
                //获得Choose_Picture位置，设置popWindowForGraphic的显示位置
                int[] location = new int[2];
                Choose_Picture.getLocationOnScreen(location);
                //显示窗口
                popWindowForGraphic.showAtLocation(BabyHomeActivity.this.findViewById(R.id.baby_home), Gravity.NO_GRAVITY, 0, location[1] - 290);
            }
        });
        Do_Something = (Button) findViewById(R.id.do_something);
        Find_Home = (Button) findViewById(R.id.find_home);
    }

}
