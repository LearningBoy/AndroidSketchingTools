package cug.school.sketching.common;

import cug.school.sketching.R;

/**
 * Created by Mr_Bai on 2016/5/31.
 */
public class ResourceImage {

    private ResourceImage() {
    }

    private static ResourceImage resourceImage = null;

    public static ResourceImage getResourceImageInstance() {
        if (resourceImage == null) {
            resourceImage = new ResourceImage();
        }
        return resourceImage;
    }

    //道路图集
    public Integer[] roadImages = {R.mipmap.road_01, R.mipmap.road_02,
            R.mipmap.road_03, R.mipmap.road_04,
            R.mipmap.road_05, R.mipmap.road_06,
            R.mipmap.road_07, R.mipmap.road_08,
            R.mipmap.road_09, R.mipmap.road_10,
            R.mipmap.road_11, R.mipmap.road_12};
    //桥梁图集
    public Integer[] bridgeImages = {R.mipmap.bridge_01, R.mipmap.bridge_02,
            R.mipmap.bridge_03, R.mipmap.bridge_04,
            R.mipmap.bridge_05, R.mipmap.bridge_06,
            R.mipmap.bridge_07, R.mipmap.bridge_08,
            R.mipmap.bridge_09, R.mipmap.bridge_09,
            R.mipmap.bridge_09};
    //建筑图集
    public Integer[] buildingImages = {R.mipmap.building_01, R.mipmap.building_02,
            R.mipmap.building_03, R.mipmap.building_04,
            R.mipmap.building_05, R.mipmap.building_06,
            R.mipmap.building_07, R.mipmap.building_08,
            R.mipmap.building_09, R.mipmap.building_10,
            R.mipmap.building_11, R.mipmap.building_12};
    //河流图集
    public Integer[] riverImages = {R.mipmap.river_01, R.mipmap.river_02,
            R.mipmap.river_02, R.mipmap.river_02,
            R.mipmap.river_02, R.mipmap.river_02,
            R.mipmap.river_02, R.mipmap.river_02,
            R.mipmap.river_02, R.mipmap.river_02,
            R.mipmap.river_02, R.mipmap.river_02,};
    //自定义图集
    public Integer[] designImages = {R.mipmap.design_01, R.mipmap.design_02,
            R.mipmap.design_03, R.mipmap.design_04,
            R.mipmap.design_04, R.mipmap.design_04,
            R.mipmap.design_04, R.mipmap.design_04,
            R.mipmap.design_04, R.mipmap.design_04,
            R.mipmap.design_04, R.mipmap.design_04,};
}
