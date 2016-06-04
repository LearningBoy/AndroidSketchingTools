package cug.school.sketching.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cug.school.sketching.R;
import cug.school.sketching.home.BabyHomeActivity;

/**
 * Created by Mr_Bai on 2016/5/30.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {

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

        public void bindData(final int id) {
            imageView.setImageResource(id);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = context.getResources().getResourceEntryName(id);
                    BabyHomeActivity babyHomeActivity = new BabyHomeActivity();
                    babyHomeActivity.getRecyclerViewItem(name);
                }
            });
        }
    }
}