package cug.school.sketchingtools.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import cug.school.sketchingtools.R;

/**
 * Created by Mr_Bai on 2016/1/20.
 * 图形选择的适配器
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private String TAG;

    //道路图集
    private int[] road_images = {R.mipmap.road_01, R.mipmap.road_02, R.mipmap.road_03, R.mipmap.road_04,
            R.mipmap.road_05, R.mipmap.road_06, R.mipmap.road_07, R.mipmap.road_08, R.mipmap.road_09,
            R.mipmap.road_10, R.mipmap.road_11, R.mipmap.road_12};
    //桥梁图集
    private int[] bridge_images = {R.mipmap.bridge_01, R.mipmap.bridge_02, R.mipmap.bridge_03, R.mipmap.bridge_04,
            R.mipmap.bridge_05, R.mipmap.bridge_06, R.mipmap.bridge_07, R.mipmap.bridge_08, R.mipmap.bridge_09};
    //建筑图集
    private int[] building_images = {R.mipmap.building_01, R.mipmap.building_02, R.mipmap.building_03, R.mipmap.building_04,
            R.mipmap.building_05, R.mipmap.building_06, R.mipmap.building_07, R.mipmap.building_08, R.mipmap.building_09,
            R.mipmap.building_10, R.mipmap.building_11, R.mipmap.building_12};
    //河流图集
    private int[] river_images = {};

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    public String setTAG(String TAG) {
        return this.TAG = TAG;
    }

    @Override
    public int getCount() {
        switch (TAG) {
            case "ROAD":
                return road_images.length;
            case "BRIDGE":
                return bridge_images.length;
            case "BUILDING":
                return building_images.length;
            default:
                break;
        }
        return -1;
    }

    @Override
    public Object getItem(int position) {
        switch (TAG) {
            case "ROAD":
                return road_images[position];
            case "BRIDGE":
                return bridge_images[position];
            case "BUILDING":
                return building_images[position];
            default:
                break;
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.graphoc_item, null);
            imageView = (ImageView) convertView.findViewById(R.id.graphic_image);
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }
        switch (TAG) {
            case "ROAD":
                imageView.setImageResource(road_images[position]);
                break;
            case "BRIDGE":
                imageView.setImageResource(bridge_images[position]);
                break;
            case "BUILDING":
                imageView.setImageResource(building_images[position]);
            default:
                break;
        }
        return convertView;
    }
}
