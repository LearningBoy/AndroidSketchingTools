package cug.school.sketching.home;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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
import cug.school.sketching.common.RecyclerViewAdapter;
import cug.school.sketching.common.ResourceImage;
import cug.school.sketching.common.XmlSave;

/**
 * 宝贝寻家主界面
 */
public class BabyHomeActivity extends BaseActivity {

    private View popupView;
    private PopupWindow popupWindow;
    private static final int REQUEST_OPEN_CAMERA = 0x01;
    private static final int REQUEST_OPEN_ALBUM = 0x02;

    private DrawSketchView drawSketchView;
    private Uri mDestinationUri;
    private static final String CROPPED_IMAGE_NAME = "CropImage.png";
    private TextView textView;

    private LinearLayout chooseGraphicLl;
    private RecyclerView recyclerView;
    private Boolean flag = false;
    private String tag = "road";
    private RecyclerViewAdapter adapter;
    private ResourceImage resourceImage = ResourceImage.getResourceImageInstance();

    private LinearLayout operationLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_home_layout);

        mDestinationUri = Uri.fromFile(new File(getCacheDir(), CROPPED_IMAGE_NAME));

        initView();
    }

    private void initView() {

        drawSketchView = (DrawSketchView) findViewById(R.id.draw_view);
        textView = (TextView) findViewById(R.id.dealing);

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
        //选择道路
        TextView roadTv = (TextView) findViewById(R.id.road_tv);
        roadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("road")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, resourceImage.roadImages);
                    recyclerView.setAdapter(adapter);
                    tag = "road";
                }
            }
        });
        //选择桥梁
        TextView bridgeTv = (TextView) findViewById(R.id.bridge_tv);
        bridgeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("bridge")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, resourceImage.bridgeImages);
                    recyclerView.setAdapter(adapter);
                    tag = "bridge";
                }
            }
        });
        //选择建筑
        TextView buildingTv = (TextView) findViewById(R.id.building_tv);
        buildingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("building")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, resourceImage.buildingImages);
                    recyclerView.setAdapter(adapter);
                    tag = "building";
                }
            }
        });
        //选择河流
        TextView riverTv = (TextView) findViewById(R.id.river_tv);
        riverTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("river")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, resourceImage.riverImages);
                    recyclerView.setAdapter(adapter);
                    tag = "river";
                }
            }
        });
        //选择自定义
        TextView designTv = (TextView) findViewById(R.id.design_tv);
        designTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag.equals("design")) {
                    adapter = new RecyclerViewAdapter(BabyHomeActivity.this, resourceImage.designImages);
                    recyclerView.setAdapter(adapter);
                    tag = "design";
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerViewAdapter(this, resourceImage.roadImages);
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
                drawSketchView.unDo();
            }
        });
        //重置
        TextView resetButton = (TextView) findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSketchView.reSet();
            }
        });
        //寻家
        TextView findHome = (TextView) findViewById(R.id.find_home);
        findHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView XmlContentView = (TextView) findViewById(R.id.xml_content);
                XmlSave xmlSave = new XmlSave();
                XmlContentView.setText(xmlSave.Save_as_XmlString(1, "123456", "test", drawSketchView.getAllPoint()));
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

    public void getRecyclerViewItem(String string) {
        switch (tag) {
            case "road":
                break;
            case "bridge":
                break;
            case "building":
                break;
            case "river":
                break;
            case "design":
                break;
            default:
                break;
        }
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
                    drawSketchView.Recycle();
                    String picture_path = getRealPicturePath(resultUri);
                    drawSketchView.setBitmap(compressBitmap(drawSketchView, picture_path));
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
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
        imageAsyncTask.execute(BitmapFactory.decodeFile(path, options));
        return BitmapFactory.decodeFile(path, options);
    }

    //异步处理图像的灰度化
    class ImageAsyncTask extends AsyncTask<Bitmap, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Bitmap[] params) {
            int width, height;
            height = params[0].getHeight();
            width = params[0].getWidth();
            int[] colorArray = new int[height * width];
            int[] grayArray = new int[height * width];
            params[0].getPixels(colorArray, 0, width, 0, 0, width, height);
            int n = 0;
            //gray
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int red = (colorArray[n] >> 16) & 0xff;
                    int green = (colorArray[n] >> 8) & 0xff;
                    int blue = colorArray[n] & 0xff;
                    if (red == green && green == blue) {
                        grayArray[n] = red;
                        n++;
                    } else {
                        int gray = (red + green + blue) / 3;
                        grayArray[n] = gray;
                        colorArray[n] = 0xff000000 | (gray << 16) | (gray << 8) | gray;
                        n++;
                    }
                }
            }
            //gauss
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    double gray_i_1_j_1 = 0.0751 * grayArray[width * (i - 1) + (j - 1)];
                    double gray_i_j_1 = 0.1238 * grayArray[width * (i - 1) + j];
                    double gray_i1_j_1 = 0.0751 * grayArray[width * (i - 1) + (j + 1)];
                    double gray_i_1_j = 0.1238 * grayArray[width * i + (j - 1)];
                    double gray_i_j = 0.2043 * grayArray[width * i + j];
                    double gray_i1_j = 0.1238 * grayArray[width * i + (j + 1)];
                    double gray_i_1_j1 = 0.0751 * grayArray[width * (i + 1) + (j - 1)];
                    double gray_i_j1 = 0.1238 * grayArray[width * (i + 1) + j];
                    double gray_i1_j1 = 0.0751 * grayArray[width * (i + 1) + (j + 1)];
                    int gray = (int) (gray_i_1_j_1 + gray_i_j_1 + gray_i1_j_1 + gray_i_1_j + gray_i_j
                            + gray_i1_j + gray_i_1_j1 + gray_i_j1 + gray_i1_j1);
                    grayArray[width * i + j] = gray;
                    colorArray[width * i + j] = 0xff000000 | (gray << 16) | (gray << 8) | gray;
                }
            }
            //幅值与梯度
            double[] M = new double[width * height];
            double[] O = new double[width * height];
            for (int i = 0; i < height - 1; i++) {
                for (int j = 0; j < width - 1; j++) {
                    int G_x_i_j = (grayArray[width * i + j] - grayArray[width * (i + 1) + j]) / 2
                            + (grayArray[width * i + j + 1] - grayArray[width * (i + 1) + j + 1]) / 2;
                    int G_y_i_j = (grayArray[width * i + j] - grayArray[width * (i) + j + 1]) / 2
                            + (grayArray[width * (i + 1) + j] - grayArray[width * (i + 1) + j + 1]) / 2;
                    double M_i_j = Math.sqrt(G_x_i_j * G_x_i_j + G_y_i_j * G_y_i_j);
                    M[width * i + j] = M_i_j;
                    if (G_y_i_j != 0) {
                        double O_i_j = Math.atan(G_x_i_j / G_y_i_j);
                        O[width * i + j] = O_i_j;
                    } else {
                        O[width * i + j] = 0;
                    }
                }
            }
            //非极大值抑制
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if (O[width * i + j] == 0) {
                        if (O[width * (i - 1) + j - 1] == 0 && O[width * (i - 1) + j] == 0
                                && O[width * i + j - 1] == 0) {
                            grayArray[width * i + j] = 255;
                            colorArray[width * i + j] = 0xff000000 | (255 << 16) | (255 << 8) | 255;
                        }
                    }
                }
            }
            //双阀值
            for (int i = 1; i < height; i++) {
                for (int j = 1; j < width; j++) {
                    if (grayArray[width * i + j] > 90) {
                        if (grayArray[width * i + j] >= 180) {
                            grayArray[width * i + j] = 255;
                            colorArray[width * i + j] = 0xff000000 | (255 << 16) | (255 << 8) | 255;
                        } else {
                            if (grayArray[width * (i - 1) + j - 1] >= 180 && grayArray[width * (i - 1) + j] >= 180
                                    && grayArray[width * i + j - 1] >= 180) {
                                grayArray[width * i + j] = 255;
                                colorArray[width * i + j] = 0xff000000 | (255 << 16) | (255 << 8) | 255;
                            }
                        }
                    }
                }
            }
            // Change bitmap to use new array
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(colorArray, 0, width, 0, 0, width, height);
            params[0] = null;
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap o) {
            super.onPostExecute(o);
            textView.setVisibility(View.GONE);
            drawSketchView.Recycle();
            drawSketchView.setBitmap(o);
        }
    }
}
