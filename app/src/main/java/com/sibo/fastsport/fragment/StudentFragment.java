package com.sibo.fastsport.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.internal.Utils;
import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.TeacherAndStudentAdapter;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.listener.OnItemClickListener;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.model.UserSportPlan;
import com.sibo.fastsport.ui.TeaAndStdDetailActivity;
import com.sibo.fastsport.utils.LogUtils;
import com.sibo.fastsport.utils.MyBombUtils;

import java.util.List;


public class StudentFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TeacherAndStudentAdapter adapter;
    private View studentFragment;
    private List<UserSportPlan> list;
    public List<UserInfo> userInfoList;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.SUCCESS) {
                userInfoList = MyBombUtils.userInfoList;
                adapter.setUserInfoList(userInfoList);
                list = MyBombUtils.studentList;
                adapter.setList_usp(list);
                adapter.notifyDataSetChanged();
                Log.e("lsit", list.size() + "");
                if (list.size() == 0) {
                    tip.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tip.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, 1000);
            }
        }
    };
    private TextView title, tip;
    private MyBombUtils bombUtils;
    private RelativeLayout back;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        studentFragment = inflater.inflate(R.layout.fragment_student, container, false);
//        getData();
        initView();
//        initData();
        return studentFragment;
    }

    public void getInfo() {
        if (loginuser.getType().equals("1")) {
            title.setText("学员");
            bombUtils.getStudentInfo(loginuser.getAccount());//获取学员，使用账号
        } else {
            title.setText("教练");
            tip.setText("暂无教练");
            bombUtils.getTeacherInfo(loginuser.getId());//获取教练，使用学员的ID
        }
    }

    @Override
    protected void initData() {

        bombUtils = new MyBombUtils(getActivity());
        recyclerView.setVisibility(View.GONE);
        tip.setVisibility(View.VISIBLE);
        adapter = new TeacherAndStudentAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                LogUtils.e(userInfoList.get(position).getNikeName());
                bundle.putSerializable("st", userInfoList.get(position));
                Intent intent = new Intent(getActivity(), TeaAndStdDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        getInfo();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (loginuser.getType().equals("1")){
                    bombUtils.getStudentInfo(loginuser.getAccount());//获取学员，使用账号
                }else {
                    bombUtils.getTeacherInfo(loginuser.getId());//获取教练，使用学员的ID
                }
            }

        });
    }


    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) studentFragment.findViewById( R.id.commend_mrl);
        //设置加载图标的颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorRed));
        recyclerView = (RecyclerView) studentFragment.findViewById(R.id.student_recycler_view);
        title = (TextView) studentFragment.findViewById(R.id.top_tv_title);
        tip = (TextView) studentFragment.findViewById(R.id.tip);
        back = (RelativeLayout) studentFragment.findViewById(R.id.top_rl_back);
        back.setVisibility(View.GONE);
    }

}
