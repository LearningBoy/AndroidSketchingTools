package cug.school.sketchingtools.baby_home;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import cug.school.sketchingtools.R;
import cug.school.sketchingtools.common.DrawSketchView;
import cug.school.sketchingtools.common.GridViewAdapter;
import cug.school.sketchingtools.common.XmlSave;
import cug.school.sketchingtools.popwindows.PopWindowForPicture;

/**
 * 宝贝寻家主界面
 */
public class BabyHomeActivity extends Activity {

    /**
     * 打开相机、相册相关字段注释
     */
    private PopWindowForPicture popWindowForPicture; //相机、相册的PopWindow
    private static final int OPEN_CAMERA_CODE = 10; //打开相机的返回码
    private static final int OPEN_ALBUM_CODE = 11;  //打开相册时的返回码

    /**
     * 图形选择按钮相关字段
     */
    private TabHost tabHost;
    private Boolean TAG = false;

    /**
     * 操作按钮相关字段
     */
    private LinearLayout operationLinearLayout;

    /**
     * 寻家按钮相关字段
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_home_main_layout);
        initView();
    }

    private void initView() {
        //打开相机或者打开相册
        Button Open_Camera_Or_Picture = (Button) findViewById(R.id.open_camera_or_open_picture);
        Open_Camera_Or_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏图形选择界面或操作界面
                if (TAG) {
                    if (tabHost.getVisibility() == View.VISIBLE) {
                        tabHost.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        tabHost.setVisibility(View.GONE);
                    } else {
                        operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        operationLinearLayout.setVisibility(View.GONE);
                    }
                    TAG = false;
                }
                //实例化PopWindow
                popWindowForPicture = new PopWindowForPicture(BabyHomeActivity.this, Camera_Album_itemsOnClick);
                //显示窗口
                popWindowForPicture.showAtLocation(BabyHomeActivity.this.findViewById(R.id.baby_home), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        //图形选择按钮
        Button Choose_Picture = (Button) findViewById(R.id.choose_picture);
        tabHost = (TabHost) findViewById(R.id.tab_host);
        Choose_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = tabHost.getVisibility();
                switch (i) {
                    case View.VISIBLE:
                        tabHost.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        tabHost.setVisibility(View.GONE);
                        TAG = false;
                        break;
                    case View.GONE:
                        if (TAG) {
                            tabHost.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.graphic_show_when_operation_has_show));
                            operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.operation_hide_if_graphic_will_show));
                            operationLinearLayout.setVisibility(View.GONE);
                        } else {
                            TAG = true;
                            tabHost.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_show));
                        }
                        tabHost.setVisibility(View.VISIBLE);
                }
            }
        });
        //访问资源文件
        Resources resources = getResources();
        //装载TabHost
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.road)).setIndicator(resources.getString(R.string.road)).setContent(R.id.road_gridView));
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.bridge)).setIndicator(resources.getString(R.string.bridge)).setContent(R.id.bridge_gridView));
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.building)).setIndicator(resources.getString(R.string.building)).setContent(R.id.building_gridView));
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.river)).setIndicator(resources.getString(R.string.river)).setContent(R.id.river_gridView));
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.design)).setIndicator(resources.getString(R.string.design)).setContent(R.id.design_gridView));
        tabHost.setCurrentTab(0);
        //修改TabWidget中的字体大小
        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getTabCount(); i++) {
            View view = tabWidget.getChildAt(i);
            TextView textView = (TextView) view.findViewById(android.R.id.title);
            textView.setTextSize(13);
        }

        //加载道路GridView
        GridView roadGridView = (GridView) findViewById(R.id.road_gridView);
        GridViewAdapter roadAdapter = new GridViewAdapter(this);
        roadAdapter.setTAG("ROAD");
        roadGridView.setAdapter(roadAdapter);
        //加载桥梁GridView
        GridView bridgeGridView = (GridView) findViewById(R.id.bridge_gridView);
        GridViewAdapter bridgeAdapter = new GridViewAdapter(this);
        bridgeAdapter.setTAG("BRIDGE");
        bridgeGridView.setAdapter(bridgeAdapter);
        //加载建筑GridView
        GridView buildingGridView = (GridView) findViewById(R.id.building_gridView);
        GridViewAdapter buildingAdapter = new GridViewAdapter(this);
        buildingAdapter.setTAG("BUILDING");
        buildingGridView.setAdapter(buildingAdapter);

        //操作按钮
        Button OperationButton = (Button) findViewById(R.id.operation_button);
        OperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationLinearLayout = (LinearLayout) findViewById(R.id.operation_linearLayout);
                int i = operationLinearLayout.getVisibility();
                switch (i) {
                    case View.VISIBLE:
                        operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        operationLinearLayout.setVisibility(View.GONE);
                        TAG = false;
                        break;
                    case View.GONE:
                        if (TAG) {
                            operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.operation_show_when_graphic_has_show));
                            tabHost.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.graphic_hide_if_operation_will_show));
                            tabHost.setVisibility(View.GONE);
                        } else {
                            TAG = true;
                            operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_show));
                        }
                        operationLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        //撤销
        Button undoButton = (Button) findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawSketchView) findViewById(R.id.draw_view)).unDo();
            }
        });
        //重置
        Button resetButton = (Button) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawSketchView) findViewById(R.id.draw_view)).reSet();
            }
        });
        //寻家
        Button findHomeButton = (Button) findViewById(R.id.find_home);
        findHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView XmlContentView = (TextView) findViewById(R.id.xml_content);
                XmlSave xmlSave = new XmlSave();
                XmlContentView.setText(xmlSave.Save_as_XmlString(1, "123456", "test", ((DrawSketchView) findViewById(R.id.draw_view)).getAllPoint()));
                int i = XmlContentView.getVisibility();
                switch (i) {
                    case View.VISIBLE:
                        XmlContentView.setVisibility(View.GONE);
                        break;
                    case View.GONE:
                        XmlContentView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    //拍照和相册按钮的点击事件
    private View.OnClickListener Camera_Album_itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            popWindowForPicture.dismiss();
            switch (v.getId()) {
                case R.id.open_camera:
                    getImageFromCamera();
                    break;
                case R.id.open_picture:
                    getImageFromAlbum();
                    break;
                default:
                    break;
            }
        }
    };

    //从相机获得照片
    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, OPEN_CAMERA_CODE);
    }

    //从相册获得照片
    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //设置获得文件地址和类型
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, OPEN_ALBUM_CODE);
    }

    //打开相机和相册的返回函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            return;
        }
        ((DrawSketchView) findViewById(R.id.draw_view)).Recycle();
        String picture_path = getRealPicturePath(data.getData());
        ((DrawSketchView) findViewById(R.id.draw_view)).setBitmap(compressBitmap(findViewById(R.id.draw_view), picture_path));
        super.onActivityResult(requestCode, resultCode, data);
    }

    //将图片的Uri路径转换为绝对路径String类型
    private String getRealPicturePath(Uri uri) {
        String pic_Path = null;
        String scheme = uri.getScheme();
        if (scheme == null) {
            pic_Path = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            pic_Path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        pic_Path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return pic_Path;
    }

    //返回经过压缩的照片
    private Bitmap compressBitmap(View view, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只得到属性信息，不加载Bitmap到内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //获得原始照片的宽高
        int Bitmap_Width = options.outWidth;
        int Bitmap_Height = options.outHeight;
        //获得控件的宽高
        int View_Width = view.getWidth();
        int View_Height = view.getHeight();
        //设置照片的压缩比例,取最大比例，保证整个图片可以显示在View控件中
        options.inSampleSize = Math.max(Bitmap_Width / View_Width, Bitmap_Height / View_Height);
        //inJustDecodeBounds设置为false，加载Bitmap到内存中
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
}
