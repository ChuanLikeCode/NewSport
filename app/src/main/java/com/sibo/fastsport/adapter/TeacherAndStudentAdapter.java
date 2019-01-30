package com.sibo.fastsport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.listener.OnItemClickListener;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.model.UserSportPlan;
import com.sibo.fastsport.utils.ImageLoaderUtils;
import com.sibo.fastsport.view.CircleImageView;

import java.util.List;

/**
 * Created by chuan on 2017/3/28.
 */

public class TeacherAndStudentAdapter extends RecyclerView.Adapter<TeacherAndStudentAdapter.MyViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<UserSportPlan> list_usp;
    private UserInfo loginUser;
    private List<UserInfo> userInfoList;

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public TeacherAndStudentAdapter(Context context, List<UserSportPlan> list_usp) {
        this.context = context;
        this.list_usp = list_usp;
        loginUser = MyApplication.getInstance().readLoginUser();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList_usp(List<UserSportPlan> list_usp) {
        this.list_usp = list_usp;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.student_item_recycleview, null), onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (loginUser.getType().equals("2")) {
            Log.e("getType", "2");
            holder.jirou.setVisibility(View.GONE);
            holder.zhifang.setVisibility(View.GONE);
            holder.yundong.setVisibility(View.GONE);
            holder.type.setVisibility(View.GONE);
            holder.tv_zhifang.setVisibility(View.GONE);
            holder.tv_jirou.setVisibility(View.GONE);
            holder.name.setText(userInfoList.get(position).getNikeName());
            ImageLoaderUtils.initImage(context,
                    userInfoList.get(position).getHead().getFileUrl(), holder.head, R.mipmap.loading);
        } else {
            Log.e("getType", "1");
            holder.name.setText(userInfoList.get(position).getNikeName());
            holder.jirou.setText(list_usp.get(position).getJirou());
            holder.zhifang.setText(list_usp.get(position).getZhifang());
            holder.yundong.setText(list_usp.get(position).getYundong());
            holder.type.setText(list_usp.get(position).getBody());
            ImageLoaderUtils.initImage(context,
                    userInfoList.get(position).getHead().getFileUrl(), holder.head, R.mipmap.loading);
        }
    }

    @Override
    public int getItemCount() {
        if (list_usp == null || list_usp.size() == 0) {
            return 0;
        }
        return list_usp.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener onItemClickListener;
        private CircleImageView head;
        private TextView name, jirou, zhifang, yundong, type;
        private TextView tv_jirou, tv_zhifang;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
            head = (CircleImageView) itemView.findViewById(R.id.student_item_touxiang);
            name = (TextView) itemView.findViewById(R.id.student_item_name);
            jirou = (TextView) itemView.findViewById(R.id.student_item_tv_lastClass);
            zhifang = (TextView) itemView.findViewById(R.id.student_item_tv_completeClass);
            yundong = (TextView) itemView.findViewById(R.id.student_item_yundong);
            type = (TextView) itemView.findViewById(R.id.student_item_type);
            tv_jirou = (TextView) itemView.findViewById(R.id.student_item_lastClass);
            tv_zhifang = (TextView) itemView.findViewById(R.id.student_item_completeClass);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
