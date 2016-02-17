package cug.school.sketchingtools.baby_home;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cug.school.sketchingtools.R;

/**
 * Created by Mr_Bai on 2016/1/19.
 * 添加标签界面
 */
public class AddNotesActivity extends Activity {

    private String[] notices = {"冬天下雪", "吃辣", "以面食为主", "以米饭为主"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_home_add_notes);

        //加载GridView
        GridView gridView = (GridView) findViewById(R.id.add_notices);
        gridView.setAdapter(getAdapter());
    }

    private ListAdapter getAdapter() {
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (String string : notices) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("notice", string);
            data.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(AddNotesActivity.this, data,
                R.layout.add_notes_item, new String[]{"notice"},
                new int[]{R.id.add_notices_item});
        return simpleAdapter;
    }
}
