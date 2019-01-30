package com.sibo.fastsport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.listener.OnItemClickListener;
import com.sibo.fastsport.utils.ImageLoaderUtils;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 *
 * Created by chuan on 2017/3/25.
 */

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.MyViewHolder> {
    private Context context;
    private List<BmobFile> list;
    private OnItemClickListener onItemClickListener;

    public ImgAdapter(Context context, List<BmobFile> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<BmobFile> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_img, null), onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (list.size() - 1 == position) {
            holder.delete.setVisibility(View.GONE);
            ImageLoaderUtils.initImage(context,
                    "http://bmob-cdn-6840.b0.upaiyun.com/2017/04/04/35a7790b3af24bd585c8cdc67ee44d24.png",
                    holder.img, R.drawable.chat_zx_icon2_03);
        } else {
            holder.delete.setVisibility(View.VISIBLE);
            ImageLoaderUtils.initImage(context, list.get(position).getFileUrl(), holder.img, R.mipmap.loading);
        }

    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img, delete;
        private OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            img = (ImageView) itemView.findViewById(R.id.item_iv_img);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());

            }
        }
    }
}
