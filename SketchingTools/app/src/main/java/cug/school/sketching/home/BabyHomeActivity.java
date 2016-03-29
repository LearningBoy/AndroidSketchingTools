package cug.school.sketching.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;

import cug.school.sketching.R;
import cug.school.sketching.base.BaseActivity;
import cug.school.sketching.common.DrawSketchView;
import cug.school.sketching.common.XmlSave;

/**
 * 宝贝寻家主界面
 */
public class BabyHomeActivity extends BaseActivity {

    private View popupView;
    private PopupWindow popupWindow;
    private static final int REQUEST_OPEN_CAMERA = 0x01;
    private static final int REQUEST_OPEN_ALBUM = 0x02;
    private Uri mDestinationUri;
    private static final String CROPPED_IMAGE_NAME = "CropImage.png";

    private LinearLayout chooseGraphicLl;
    private RecyclerView recyclerView;
    private Boolean flag = false;
    private String tag = "road";
    private RecyclerViewAdapter adapter;
    //道路图集
    private Integer[] roadImages = {R.mipmap.road_01, R.mipmap.road_02,
            R.mipmap.road_03, R.mipmap.road_04,
            R.mipmap.road_05, R.mipmap.road_06,
            R.mipmap.road_07, R.mipmap.road_08,
            R.mipmap.road_09, R.mipmap.road_10};
    //桥梁图集
    private Integer[] bridgeImages = {R.mipmap.bridge_01, R.mipmap.bridge_02,
            R.mipmap.bridge_03, R.mipmap.bridge_04,
            R.mipmap.bridge_05, R.mipmap.bridge_06,
            R.mipmap.bridge_07, R.mipmap.bridge_08,
            R.mipmap.bridge_09};
    //建筑图集
    private Integer[] buildingImages = {R.mipmap.building_01, R.mipmap.building_02,
            R.mipmap.building_03, R.mipmap.building_04,
            R.mipmap.building_05, R.mipmap.building_06,
            R.mipmap.building_07, R.mipmap.building_08,
            R.mipmap.building_09, R.mipmap.building_10};

    private LinearLayout operationLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_home_layout);

        mDestinationUri = Uri.fromFile(new File(getCacheDir(), CROPPED_IMAGE_NAME));

        initView();
    }

    private void initView() {
        popupView = LayoutInflater.from(this).inflate(R.layout.baby_home_popup_layout, null);
        LinearLayout Open_Camera = (LinearLayout) popupView.findViewById(R.id.open_camera);
        Open_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_OPEN_CAMERA);
            }
        });
        Button Open_Picture = (Button) popupView.findViewById(R.id.open_gallery);
        Open_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //设置获得文件地址和类型
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_OPEN_ALBUM);
            }
        });

        final ImageView toolbarRight = (ImageView) findViewById(R.id.toolbar_right);
        toolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏图形选择界面或操作界面
                if (flag) {
                    if (chooseGraphicLl.getVisibility() == View.VISIBLE) {
                        chooseGraphicLl.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        chooseGraphicLl.setVisibility(View.GONE);
                    } else {
                        operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        operationLinearLayout.setVisibility(View.GONE);
                    }
                    flag = false;
                }
                getPopupWindow(popupView).showAsDropDown(toolbarRight, 0, 0);
            }
        });

        //图形选择按钮
        TextView choosePicture = (TextView) findViewById(R.id.choose_picture);
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = chooseGraphicLl.getVisibility();
                switch (i) {
                    case View.VISIBLE:
                        chooseGraphicLl.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        chooseGraphicLl.setVisibility(View.GONE);
                        flag = false;
                        break;
                    case View.GONE:
                        if (flag) {
                            chooseGraphicLl.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.graphic_show_when_operation_has_show));
                            operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.operation_hide_if_graphic_will_show));
                            operationLinearLayout.setVisibility(View.GONE);
                        } else {
                            flag = true;
                            chooseGraphicLl.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_show));
                        }
                        chooseGraphicLl.setVisibility(View.VISIBLE);
                }
            }
        });

        chooseGraphicLl = (LinearLayout) findViewById(R.id.choose_graphic_ll);
        TextView roadTv = (TextView) findViewById(R.id.road_tv);
        roadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("road")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, roadImages);
                    recyclerView.setAdapter(adapter);
                    tag = "road";
                }
            }
        });
        TextView bridgeTv = (TextView) findViewById(R.id.bridge_tv);
        bridgeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("bridge")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, bridgeImages);
                    recyclerView.setAdapter(adapter);
                    tag = "bridge";
                }
            }
        });
        TextView buildingTv = (TextView) findViewById(R.id.building_tv);
        buildingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("building")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, buildingImages);
                    recyclerView.setAdapter(adapter);
                    tag = "building";
                }
            }
        });
        TextView riverTv = (TextView) findViewById(R.id.river_tv);
        riverTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView designTv = (TextView) findViewById(R.id.design_tv);
        designTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerViewAdapter(this, roadImages);
        recyclerView.setAdapter(adapter);

        //操作按钮
        TextView operation = (TextView) findViewById(R.id.operation);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationLinearLayout = (LinearLayout) findViewById(R.id.operation_linearLayout);
                int i = operationLinearLayout.getVisibility();
                switch (i) {
                    case View.VISIBLE:
                        operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_hidden));
                        operationLinearLayout.setVisibility(View.GONE);
                        flag = false;
                        break;
                    case View.GONE:
                        if (flag) {
                            operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.operation_show_when_graphic_has_show));
                            chooseGraphicLl.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.graphic_hide_if_operation_will_show));
                            chooseGraphicLl.setVisibility(View.GONE);
                        } else {
                            flag = true;
                            operationLinearLayout.startAnimation(AnimationUtils.loadAnimation(BabyHomeActivity.this, R.anim.popup_window_show));
                        }
                        operationLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        //撤销
        TextView undoButton = (TextView) findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawSketchView) findViewById(R.id.draw_view)).unDo();
            }
        });
        //重置
        TextView resetButton = (TextView) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawSketchView) findViewById(R.id.draw_view)).reSet();
            }
        });
        //寻家
        TextView findHome = (TextView) findViewById(R.id.find_home);
        findHome.setOnClickListener(new View.OnClickListener() {
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

    private PopupWindow getPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
        }
        return popupWindow;
    }

    //打开相机和相册的返回函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_OPEN_CAMERA || requestCode == REQUEST_OPEN_ALBUM) {
                Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(selectedUri);
                } else {
                    Toast.makeText(this, "获取图像失败", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    ((DrawSketchView) findViewById(R.id.draw_view)).Recycle();
                    String picture_path = getRealPicturePath(resultUri);
                    ((DrawSketchView) findViewById(R.id.draw_view)).setBitmap(compressBitmap(findViewById(R.id.draw_view), picture_path));
                } else {
                    Toast.makeText(this, "裁剪失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "返回异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropActivity(@NonNull Uri uri) {

        UCrop uCrop = UCrop.of(uri, mDestinationUri);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        uCrop.withOptions(options);

        uCrop.start(BabyHomeActivity.this);

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

    class RecyclerViewAdapter extends RecyclerView.Adapter {

        private Context context;
        private Integer[] ids;

        public RecyclerViewAdapter(Context context, Integer[] ids) {
            this.context = context;
            this.ids = ids;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.baby_home_graphic_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindData(ids[position]);
        }

        @Override
        public int getItemCount() {
            return ids.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView = (ImageView) itemView.findViewById(R.id.graphic_image);

            public ViewHolder(View itemView) {
                super(itemView);
            }

            public void bindData(int id) {
                imageView.setImageResource(id);
            }
        }
    }
}
