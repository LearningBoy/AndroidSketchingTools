package cug.school.sketchingtools.popwindows;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cug.school.sketchingtools.R;

/**
 * Created by Mr_Bai on 2016/1/11.
 */
public class PopWindowForGraphic extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {

    private int[] images = {R.drawable.pic_test};
    //PopWindow添加的视图
    private View WindowView;

    public PopWindowForGraphic(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置加载PopWindow的视图
        WindowView = inflater.inflate(R.layout.popup_window_for_graphic, null);
        //加载GridView
        GridView gridView = (GridView) WindowView.findViewById(R.id.gridView);
        gridView.setAdapter(getAdapter());
        gridView.setOnItemClickListener(this);
        //道路按钮
        Button Road = (Button) WindowView.findViewById(R.id.road);
        Road.setOnClickListener(this);
        //桥梁按钮
        Button Bridge = (Button) WindowView.findViewById(R.id.bridge);
        Bridge.setOnClickListener(this);
        //建筑按钮
        Button Building = (Button) WindowView.findViewById(R.id.building);
        Building.setOnClickListener(this);
        //河流按钮
        Button River = (Button) WindowView.findViewById(R.id.river);
        River.setOnClickListener(this);
        //自定义按钮
        Button Design = (Button) WindowView.findViewById(R.id.design);
        Design.setOnClickListener(this);

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

    private ListAdapter getAdapter() {
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("image", images[i]);
            data.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(WindowView.getContext(), data,
                R.layout.popup_window_for_graphoc_item, new String[]{"image"},
                new int[]{R.id.graphic_image});
        return simpleAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.road:
                break;
            case R.id.bridge:
                break;
            case R.id.building:
                break;
            case R.id.river:
                break;
            case R.id.design:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
