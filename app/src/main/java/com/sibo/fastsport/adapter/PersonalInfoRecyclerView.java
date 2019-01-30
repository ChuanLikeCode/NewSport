package com.sibo.fastsport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.domain.PersonalInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class PersonalInfoRecyclerView extends RecyclerView.Adapter<PersonalInfoRecyclerView.ViewHolder>{
    public List<PersonalInfo> info_list = new ArrayList<PersonalInfo>();
    public String[] info = {" ","斯帛","男","13590001234","30岁","180cm","75kg","8年"};
    public String[] infoTitle = {"头像","名字","性别","电话","年龄","身高","体重","教龄"};
    private LayoutInflater mInflater;
    public PersonalInfoRecyclerView(Context context) {
        this.mInflater = LayoutInflater.from(context);
        add();
    }
    public void add(){
        for (int i = 0;i < info.length; i++){
            PersonalInfo info1 = new PersonalInfo(infoTitle[i], info[i]);
            info_list.add(info1);
        }
    }

    @Override
    public PersonalInfoRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.personalinfo_item_recyclerview,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(PersonalInfoRecyclerView.ViewHolder holder, int position) {
        if (position == 0){
            holder.tv_info.setVisibility(View.GONE);
            holder.iv_info.setVisibility(View.VISIBLE);
        }else{
            holder.iv_info.setVisibility(View.GONE);
            holder.tv_info.setVisibility(View.VISIBLE);
        }
        holder.tv_info.setText(info_list.get(position).info);
        holder.tv_infoTitle.setText(info_list.get(position).infoTitle);
    }

    @Override
    public int getItemCount() {
        return info.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_info;
        public TextView tv_infoTitle;
        public ImageView iv_info;
        public ViewHolder(View view){
            super(view);
            tv_infoTitle = (TextView)view.findViewById(R.id.personalinfo_item_infotitle);
            tv_info = (TextView)view.findViewById(R.id.personalinfo_item_info);
            iv_info = (ImageView)view.findViewById(R.id.personalinfo_item_touxiang );
        }
    }
}
