package com.sibo.fastsport.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.domain.MyCollections;
import com.sibo.fastsport.ui.NewsActivity;
import com.sibo.fastsport.utils.DateTransformUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyuand on 2016.11.30.
 */

public class MyCollectionAdapter extends BaseAdapter {
    private Context context;
    private List<MyCollections> list = new ArrayList<>();

    public MyCollectionAdapter(Context context, List<MyCollections> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.wx_items, null);
            holder.author = (TextView) convertView.findViewById(R.id.wx_author);
            holder.collected = (ImageView) convertView.findViewById(R.id.wx_collect);
            holder.title = (TextView) convertView.findViewById(R.id.wx_item_title);
            holder.updataTime = (TextView) convertView.findViewById(R.id.wx_time);
            holder.img1 = (ImageView) convertView.findViewById(R.id.wx_img1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.wx_img2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.wx_img3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(list.get(position).getTitle());
        holder.updataTime.setText(DateTransformUtils.transfromDate(list.get(position).getUpdateTime()));
        Log.e("screenwidth",NewsActivity.screen_width+"");
        Picasso.with(context).load(list.get(position).getImgUrl1())
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.mipmap.loading)
                .error(R.drawable.failed)
                .into(holder.img1);
        Picasso.with(context).load(list.get(position).getImgUrl2())
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.mipmap.loading)
                .error(R.drawable.failed)
                .into(holder.img2);
        Picasso.with(context).load(list.get(position).getImgUrl3())
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.mipmap.loading)
                .error(R.drawable.failed)
                .into(holder.img3);
        holder.author.setText(R.string.wx_collect_success);
        holder.collected.setImageResource(R.mipmap.quanxing);
        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView img1, img2, img3;
        TextView author;
        TextView updataTime;
        ImageView collected;
    }
}
