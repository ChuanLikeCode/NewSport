package com.sibo.fastsport.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sibo.fastsport.R;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.ui.ChooseActionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29.
 */
public class MyChooseActionAdapter extends BaseAdapter {
    public static SparseBooleanArray status = new SparseBooleanArray();
    private int levelIds[] = {R.id.choose_item_iv_level1, R.id.choose_item_iv_level2,
            R.id.choose_item_iv_level3, R.id.choose_item_iv_level4, R.id.choose_item_iv_level5};
    private List<SportName> list = new ArrayList<SportName>();
    private Context context;
    public MyChooseActionAdapter(Context context, List<SportName> list) {
        this.context = context;
        this.list = list;
        status.clear();//每一次创建适配器都清楚已选择的状态
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
        ChooseViewHolder holder = null;
        if (convertView == null) {
            holder = new ChooseViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.choose_action_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.choose_item_img);
            holder.name = (TextView) convertView.findViewById(R.id.choose_item_name);
            holder.checkBox = (ImageView) convertView.findViewById(R.id.choose_item_checkbox);
            for (int i = 0; i < levelIds.length; i++) {
                holder.level[i] = (ImageView) convertView.findViewById(levelIds[i]);
            }
            convertView.setTag(holder);
        } else {
            holder = (ChooseViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        //使用Glide加载动态图gif
        Glide.with(context).load(list.get(position).getIcon().getFileUrl())
                .asGif()//判断是否为gif
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存图片
                .placeholder(R.mipmap.loading)//正在加载时候的显示图片
                .centerCrop()//填满整个控件
                .error(R.drawable.failed).into(holder.img);//加载出错时显示的图片
        for (int j = 0; j < holder.level.length; j++) {
            holder.level[j].setVisibility(View.GONE);
        }
        for (int i = 0; i < list.get(position).getLevel(); i++) {
            holder.level[i].setVisibility(View.VISIBLE);
        }
        final ChooseViewHolder finalHolder = holder;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status.get(position, false)) {
                    finalHolder.checkBox.setImageResource(R.mipmap.icon_ok);
                    status.put(position, true);
                    //添加选择的健身动作
                    ChooseActionActivity.list_result.add(list.get(position));
                } else {
                    finalHolder.checkBox.setImageResource(R.mipmap.icon_select_default);
                    status.put(position, false);
                    //移除未选择的健身动作
                    ChooseActionActivity.list_result.add(list.get(position));
                }
            }
        });
        if (!status.get(position, false)) {
            holder.checkBox.setImageResource(R.mipmap.icon_select_default);
            //Log.e("status",status.get(position)+"");
        } else {
            holder.checkBox.setImageResource(R.mipmap.icon_ok);
            // Log.e("status",status.get(position)+"");
        }
        return convertView;
    }

    class ChooseViewHolder {
        ImageView img;
        TextView name;
        ImageView checkBox;
        ImageView[] level = new ImageView[5];
    }
}
