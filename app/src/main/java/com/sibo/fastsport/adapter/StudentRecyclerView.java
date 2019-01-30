package com.sibo.fastsport.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.domain.StudentCourseInfo;

import java.util.ArrayList;
import java.util.List;

public class StudentRecyclerView extends RecyclerView.Adapter<StudentRecyclerView.ViewHolder> {

    public int[] pic = {R.drawable.imagefirst, R.drawable.imagesecond,
            R.drawable.imagethree, R.drawable.imagefour,
            R.drawable.imagefive, R.drawable.imagesix,
            R.drawable.imageseven};
    public List<StudentCourseInfo> infos = new ArrayList<StudentCourseInfo>();
    private LayoutInflater mInflater;

    public StudentRecyclerView(Context context) {
        this.mInflater = LayoutInflater.from(context);
        add();
    }

    private void add() {
        for (int i = 0; i < pic.length; i++) {
            StudentCourseInfo info = new StudentCourseInfo();
            info.setTouxiang(pic[i]);
            info.setName("豆豆" + i + "号");
            info.setLastClass("15");
            info.setCompleteClass("4");
            infos.add(info);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.student_item_recycleview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.touxiang.setImageResource(infos.get(position).getTouxiang());
        holder.name.setText(infos.get(position).getName());
        holder.lastCourse.setText(infos.get(position).getLastClass());
        holder.completeCourse.setText(infos.get(position).getCompleteClass());
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView touxiang;
        public TextView name;
        public TextView lastCourse;
        public TextView completeCourse;

        public ViewHolder(View view) {
            super(view);
            touxiang = (ImageView) view.findViewById(R.id.student_item_touxiang);
            name = (TextView) view.findViewById(R.id.student_item_name);
            lastCourse = (TextView) view.findViewById(R.id.student_item_tv_lastClass);
            completeCourse = (TextView) view.findViewById(R.id.student_item_tv_completeClass);
        }
    }
}