package com.sibo.fastsport.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.domain.MyCollections;
import com.sibo.fastsport.domain.WXItem;
import com.sibo.fastsport.ui.NewsActivity;
import com.sibo.fastsport.ui.WxCollectedActivity;
import com.sibo.fastsport.utils.DateTransformUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yyuand on 2016.11.28.
 */

public class WXitemAdapter extends BaseAdapter {
    public static SparseBooleanArray collected = new SparseBooleanArray();
    private List<WXItem> list = new ArrayList<>();
    private Context context;
    public WXitemAdapter(Context context,List<WXItem> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.wx_items,null);
            holder.author = (TextView) convertView.findViewById(R.id.wx_author);
            holder.collected = (ImageView) convertView.findViewById(R.id.wx_collect);
            holder.title = (TextView) convertView.findViewById(R.id.wx_item_title);
            holder.updataTime = (TextView) convertView.findViewById(R.id.wx_time);
            holder.img1 = (ImageView) convertView.findViewById(R.id.wx_img1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.wx_img2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.wx_img3);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(list.get(position).getTitle());
        //holder.updataTime.setText(DateTransformUtils.transfromDate(list.get(position).getUpdateTime()));
        if (list.get(position).getImg().size()>=3){
            Picasso.with(context).load(list.get(position).getImg().get(0))
                    .resize(NewsActivity.screen_width,100)
                    .centerCrop()
                    .placeholder(R.mipmap.loading)
                    .error(R.drawable.failed)
                    .into(holder.img1);
            Picasso.with(context).load(list.get(position).getImg().get(1))
                    .resize(NewsActivity.screen_width,100)
                    .centerCrop()
                    .placeholder(R.mipmap.loading)
                    .error(R.drawable.failed)
                    .into(holder.img2);
            Picasso.with(context).load(list.get(position).getImg().get(2))
                    .resize(NewsActivity.screen_width,100)
                    .centerCrop()
                    .placeholder(R.mipmap.loading)
                    .error(R.drawable.failed)
                    .into(holder.img3);
        }else if(list.get(position).getImg().size()==2){
            Picasso.with(context).load(list.get(position).getImg().get(0))
                    .resize(NewsActivity.screen_width,100)
                    .centerCrop()
                    .placeholder(R.mipmap.loading)
                    .error(R.drawable.failed)
                    .into(holder.img1);
            Picasso.with(context).load(list.get(position).getImg().get(1))
                    .resize(NewsActivity.screen_width,100)
                    .centerCrop()
                    .placeholder(R.mipmap.loading)
                    .error(R.drawable.failed)
                    .into(holder.img2);
        }else if (list.get(position).getImg().size()==1){
            Picasso.with(context).load(list.get(position).getImg().get(0))
                    .resize(NewsActivity.screen_width,100)
                    .centerCrop()
                    .placeholder(R.mipmap.loading)
                    .error(R.drawable.failed)
                    .into(holder.img1);
        }
        final ViewHolder finalHolder = holder;
        holder.collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //true表示已收藏，false表示未收藏
                if (!collected.get(position,false)){
                    finalHolder.collected.setImageResource(R.mipmap.quanxing);
                    finalHolder.author.setText(R.string.wx_collect_success);
                    collected.put(position,true);
                    ((NewsActivity)context).collectionList.add(list.get(position));
                }else {
                    finalHolder.collected.setImageResource(R.mipmap.banxing);
                    finalHolder.author.setText(R.string.wx_collect_failed);
                    collected.delete(position);
                    ((NewsActivity)context).collectionList.remove(list.get(position));
                }
            }
        });
        //检测是否收藏过
//        Log.e("WXitemAdapter",WxCollectedActivity.collectionList.size()+"");
        for (MyCollections m : WxCollectedActivity.collectionList) {
            if (m.getTitle().equals(list.get(position).getTitle())) {
                collected.put(position, true);
                break;
            }
        }
        if (collected.get(position,false)){
            holder.collected.setImageResource(R.mipmap.quanxing);
            holder.author.setText(R.string.wx_collect_success);
        }else {
            holder.collected.setImageResource(R.mipmap.banxing);
            holder.author.setText(R.string.wx_collect_failed);
        }

        return convertView;
    }

    class ViewHolder{
        TextView title;
        ImageView img1,img2,img3;
        TextView author;
        TextView updataTime;
        ImageView collected;
    }
}
