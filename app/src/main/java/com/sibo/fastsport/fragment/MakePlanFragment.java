package com.sibo.fastsport.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MakePlanLabelAdapter;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.domain.Pickers;
import com.sibo.fastsport.listener.OnItemClickListener;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.ui.MakePlanActivity;
import com.sibo.fastsport.ui.ScannerActivity;
import com.sibo.fastsport.utils.CollectPlan;
import com.sibo.fastsport.utils.ImageLoaderUtils;
import com.sibo.fastsport.utils.MakePlanUtils;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.ToastUtils;
import com.sibo.fastsport.view.CircleImageView;
import com.sibo.fastsport.view.PickerScrollView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class MakePlanFragment extends BaseFragment implements View.OnClickListener, PickerScrollView.onSelectListener {
    public static List<Pickers> exercise, pickerMuscleMass, pickerBodyFat;
    public UserInfo student;
    public String scanner_id;
    private View makePlanFragment;
    private TextView nextStep, userHeight, userWeight, usermuscleMass, userBodyFat, exerciseMass;
    private EditText name, sex;
    private LinearLayout llUsermuscleMass, llUserBodyFat;
    private CircleImageView touxiang;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.SUCCESS) {
                ImageLoaderUtils.initImage(getActivity(), student.getHead().getFileUrl(), touxiang, R.mipmap.loading);
                name.setText(student.getNikeName());
                sex.setText(student.getSex());
                userHeight.setText(student.getHeight());
                userWeight.setText(student.getWeight());
                dialog.dismiss();
            }else if (msg.what == Constant.FAILED){
                dialog.dismiss();
                ToastUtils.shortToast(getActivity(),"无此学员");
            }
        }
    };
    private RelativeLayout chooseExercise;
    private int llSelected;
    private RecyclerView recyclerView;
    private MakePlanLabelAdapter adapter;
    private List<String> list_label;
    private ImageView scanner;//扫描学员二维码获得学员资料
    private int select = 0;
    private MyBombUtils bombUtils;

    @Override
    public void onResume() {

        super.onResume();
        MakePlanUtils.isFirst = true;//修改选择动作界面为第一次执行
        MakePlanUtils.sp_relaxAction.clear();
        MakePlanUtils.sp_warmUp.clear();
        MakePlanUtils.sp_mainAction.clear();
        MakePlanUtils.sp_stretching.clear();
        Bundle bundle = getArguments();

        if (bundle != null){

            if (bundle.getBoolean("finish",false)){

                name.setText("");
                sex.setText("");
                touxiang.setImageResource(R.mipmap.icon_camera02);
                userHeight.setText("0CM");
                userBodyFat.setText("0%");
                usermuscleMass.setText("0KG");
                userWeight.setText("0KG");
            }
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        makePlanFragment = inflater.inflate(R.layout.fragment_makeplan, container, false);
        initView();
        initData();
        initListener();
        return makePlanFragment;
    }

    @Override
    protected void initData() {
        bombUtils = new MyBombUtils(getActivity());
        exercise = new ArrayList<>();
        pickerMuscleMass = new ArrayList<>();
        pickerBodyFat = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            String j = i + "%";
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(j);
            pickerBodyFat.add(pickers);
        }
        for (int i = 0; i < 101; i++) {
            String j = i + "KG";
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(j);
            pickerMuscleMass.add(pickers);
        }
        String[] str = {"从不运动", "偶尔运动", "经常运动"};
        for (int i = 0; i < 3; i++) {
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(str[i]);
            exercise.add(pickers);
        }
        list_label = new ArrayList<>();
        list_label.add("隐形肥胖");
        list_label.add("脂肪过多");
        list_label.add("肥胖型");
        list_label.add("肌肉不足");
        list_label.add("健康匀称");
        list_label.add("超重肌肉");
        list_label.add("消瘦型");
        list_label.add("低脂肪");
    }


    private void initListener() {
        nextStep.setOnClickListener(this);
        scanner.setOnClickListener(this);
        llUserBodyFat.setOnClickListener(this);
        llUsermuscleMass.setOnClickListener(this);
        pickers.setOnSelectListener(this);
        chooseExercise.setOnClickListener(this);
        adapter = new MakePlanLabelAdapter(getActivity(), list_label);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Log.e("onItemClick",position+"");
                select = position;
                int pre = adapter.pos;
                adapter.pos = position;
                if (pre != -1) {
                    adapter.notifyItemChanged(pre);
                }
                adapter.notifyItemChanged(position);
            }
        });
    }

    private void initView() {
        scanner = (ImageView) makePlanFragment.findViewById(R.id.scanner);
        recyclerView = (RecyclerView) makePlanFragment.findViewById(R.id.recycler);
        nextStep = (TextView) makePlanFragment.findViewById(R.id.plan_tv_nextStep);
        name = (EditText) makePlanFragment.findViewById(R.id.plan_et_name);
        sex = (EditText) makePlanFragment.findViewById(R.id.plan_et_sex);
        userBodyFat = (TextView) makePlanFragment.findViewById(R.id.plan_tv_bodyFat);
        userHeight = (TextView) makePlanFragment.findViewById(R.id.plan_tv_height);
        usermuscleMass = (TextView) makePlanFragment.findViewById(R.id.plan_tv_muscleMass);
        userWeight = (TextView) makePlanFragment.findViewById(R.id.plan_tv_weight);
        llUserBodyFat = (LinearLayout) makePlanFragment.findViewById(R.id.plan_ll_userBodyFat);
        llUsermuscleMass = (LinearLayout) makePlanFragment.findViewById(R.id.plan_ll_userMuscleMass);
        touxiang = (CircleImageView) makePlanFragment.findViewById(R.id.plan_iv_camera);
        chooseExercise = (RelativeLayout) makePlanFragment.findViewById(R.id.plan_rl_exercise);
        exerciseMass = (TextView) makePlanFragment.findViewById(R.id.plan_tv_exerciseMass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plan_rl_exercise:
                llSelected = 5;
                showDialog(exercise);
                break;
            case R.id.plan_tv_nextStep:
                String str = name.getText().toString();
                String str1 = sex.getText().toString();
                if (str.equals("")||str1.equals("")){
                    Toast.makeText(getActivity(),"请输入名字和性别",Toast.LENGTH_SHORT).show();
                }else {
                    CollectPlan.userSportPlan.setPlanName(str+"的健身计划");
                    CollectPlan.userSportPlan.setAccount(loginuser.getAccount());
                    CollectPlan.userSportPlan.setBody(list_label.get(select));
                    CollectPlan.userSportPlan.setYundong(exerciseMass.getText().toString());
                    CollectPlan.userSportPlan.setImg(student.getHead());
                    CollectPlan.userSportPlan.setName(student.getNikeName());
                    CollectPlan.userSportPlan.setJirou(usermuscleMass.getText().toString());
                    CollectPlan.userSportPlan.setZhifang(userBodyFat.getText().toString());
                    CollectPlan.userSportPlan.setTeacherImg(loginuser.getHead());
                    CollectPlan.userSportPlan.setTeacherName(loginuser.getNikeName());
                    startActivity(new Intent(getActivity(), MakePlanActivity.class));
                }

                break;
            case R.id.plan_ll_userBodyFat:
                llSelected = 4;
                showDialog(pickerBodyFat);
                break;
            case R.id.plan_ll_userMuscleMass:
                llSelected = 3;
                showDialog(pickerMuscleMass);
                break;
            case R.id.scanner:
                if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA)) {
                    Intent intent = new Intent(getActivity(), ScannerActivity.class);
                    MyBombUtils.COUNT = 0;
                    startActivityForResult(intent, 456);
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 456:
                if (data!=null){
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        dialog = ProgressDialog.show(getActivity(), null, "正在加载数据...");
                        scanner_id = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_SHORT).show();
                        CollectPlan.userSportPlan.setStudentId(scanner_id);
                        bombUtils.queryStudent(scanner_id);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }

    }

    @Override
    public void onSelect(Pickers pickers) {
        switch (llSelected) {
            case 3:
                usermuscleMass.setText(pickers.getShowConetnt());
                break;
            case 4:
                userBodyFat.setText(pickers.getShowConetnt());
                break;
            case 5:
                exerciseMass.setText(pickers.getShowConetnt());
                break;
        }
    }
}
