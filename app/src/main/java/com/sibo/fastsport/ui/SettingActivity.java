package com.sibo.fastsport.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.domain.Pickers;
import com.sibo.fastsport.utils.AppManager;
import com.sibo.fastsport.utils.ImageLoaderUtils;
import com.sibo.fastsport.utils.ImageUploadUtil;
import com.sibo.fastsport.utils.ImageUtil;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.StatusBarUtil;
import com.sibo.fastsport.utils.ToastUtils;
import com.sibo.fastsport.view.ActionSheet;
import com.sibo.fastsport.view.CircleImageView;
import com.sibo.fastsport.view.MyAlertDialog;
import com.sibo.fastsport.view.PickerScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class SettingActivity extends BaseActivity implements ActionSheet.OnSheetItemClickListener, View.OnClickListener, PickerScrollView.onSelectListener {
    //拍照、从相册选择、照片保存成功
    private final int TAKE_PHOTO = 1, CHOOSE_PHOTO = 2, SAVE_IMAGE_SUCCESS = 3;
    private TextView sex, age, height, weight, jiaoling, phone;
    private RelativeLayout rl_jiaoling, rl_head, rl_sex, rl_age, rl_height, rl_weight, rl_save;
    private EditText name;
    private CircleImageView head;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCESS:
                    dialog.dismiss();
                    ToastUtils.shortToast(SettingActivity.this, "保存信息成功");
//                    startActivity(MainActivity.class)
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("save", loginuser);
                    intent.putExtras(bundle);
                    setResult(456, intent);
                    finish();
                    break;
                case Constant.UPLOAD_SUCCESS:
                    dialog.dismiss();
                    ImageLoaderUtils.initImage(SettingActivity.this, loginuser.getHead().getFileUrl(), head, R.mipmap.loading);
                    ToastUtils.shortToast(SettingActivity.this, "图片上传完成");
                    break;
                case Constant.FAILED:
                    dialog.dismiss();
                    ToastUtils.shortToast(SettingActivity.this, "图片上传失败");
                    break;
                case 789:
                    dialog.dismiss();
                    ToastUtils.shortToast(SettingActivity.this, "信息保存失败请重试");
                    break;
            }

        }
    };
    private Button exit;
    private TextView title;
    private TextView save;
    private List<Pickers> pick_jiaoling, pick_sex, pick_age, pick_height, pick_weight;
    private int select = 0;
    private MyBombUtils bombUtils;
    private String[] items = {"拍照", "我的相册"};
    // 头像文件、上传头像的名称、本地图片uri
    private File imageFile;
    private Uri imageUri; // 图片路径

    @Override
    protected void findViewByIDS() {
        sex = findViewsById(R.id.edit_myinfo_tv_sex);
        age = findViewsById(R.id.edit_myinfo_tv_age);
        height = findViewsById(R.id.edit_myinfo_tv_height);
        weight = findViewsById(R.id.edit_myinfo_tv_weight);
        jiaoling = findViewsById(R.id.edit_myinfo_tv_jiaoling);
        rl_jiaoling = findViewsById(R.id.rl_jiaoling);
        rl_head = findViewsById(R.id.rl_head);
        rl_sex = findViewsById(R.id.rl_sex);
        rl_age = findViewsById(R.id.rl_age);
        rl_height = findViewsById(R.id.rl_height);
        rl_weight = findViewsById(R.id.rl_weight);
        name = findViewsById(R.id.edit_myinfo_et_name);
        head = findViewsById(R.id.edit_myinfo_civ_head);
        title = findViewsById(R.id.top_tv_title);
        phone = findViewsById(R.id.edit_myinfo_tv_telephone);
        save = findViewsById(R.id.top_tv_right);
        rl_save = findViewsById(R.id.top_rl_right);
        exit = (Button) findViewById(R.id.activity_setting_btn_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_setting);
        initData();
        bind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageLoaderUtils.initImage(this, loginuser.getHead().getFileUrl(), head, R.mipmap.loading);
        name.setText(loginuser.getNikeName());
        sex.setText(loginuser.getSex());
        phone.setText(loginuser.getAccount());
        age.setText(loginuser.getAge());
        height.setText(loginuser.getHeight());
        weight.setText(loginuser.getWeight());
        jiaoling.setText(loginuser.getJiaoling());
    }

    private void bind() {
        rl_age.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_weight.setOnClickListener(this);
        rl_jiaoling.setOnClickListener(this);
        rl_height.setOnClickListener(this);
        pickers.setOnSelectListener(this);
        rl_save.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void initData() {
        phone.setText(loginuser.getAccount());
        bombUtils = new MyBombUtils(this);
        save.setText("保存");
        save.setVisibility(View.VISIBLE);
        title.setText("编辑个人信息");
        phone.setText(loginuser.getAccount());
        pick_age = new ArrayList<>();
        pick_height = new ArrayList<>();
        pick_weight = new ArrayList<>();
        pick_sex = new ArrayList<>();
        pick_jiaoling = new ArrayList<>();
        for (int i = 0; i < 121; i++) {
            String j = i + 30 + "KG";
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(j);
            pick_weight.add(pickers);
        }
        for (int i = 0; i < 71; i++) {
            String j = i + 150 + "CM";
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(j);
            pick_height.add(pickers);
        }
        for (int i = 0; i < 101; i++) {
            String j = i + "岁";
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(j);
            pick_age.add(pickers);
        }
        for (int i = 0; i < 101; i++) {
            String j = i + "年";
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(j);
            pick_jiaoling.add(pickers);
        }
        String[] str1 = {"男", "女"};
        for (int i = 0; i < 2; i++) {
            Pickers pickers = new Pickers();
            pickers.setShowId(i + "");
            pickers.setShowConetnt(str1[i]);
            pick_sex.add(pickers);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head://头像
                showActionSheetDialog(items);
                break;
            case R.id.rl_sex://性别
                select = 1;
                showDialog(pick_sex);
                break;
            case R.id.rl_age://年龄
                select = 2;
                showDialog(pick_age);
                break;
            case R.id.rl_height://身高
                select = 3;
                showDialog(pick_height);
                break;
            case R.id.rl_weight://体重
                select = 4;
                showDialog(pick_weight);
                break;
            case R.id.rl_jiaoling://教龄
                select = 5;
                showDialog(pick_jiaoling);
                break;
            case R.id.top_rl_right://保存
                saveUserInfo();
                break;
            case R.id.activity_setting_btn_exit:
                logoutDialog();
                break;

        }
    }

    /**
     * 退出登录提示框
     */
    private void logoutDialog() {
        MyAlertDialog logDialog = new MyAlertDialog(this);
        logDialog.builder()
                .setTitle("退出提示")
                .setMsg("是否要退出登录？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.getInstance().saveUserInfo(null);
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        AppManager.getInstance().finishActivity();
                    }
                })
                .show();
    }

    private void saveUserInfo() {
        if (!name.getText().toString().equals("")) {
            dialog = ProgressDialog.show(this, null, "正在保存用户信息...");
            loginuser.setAge(age.getText().toString());
            loginuser.setHeight(height.getText().toString());
            loginuser.setWeight(weight.getText().toString());
            loginuser.setJiaoling(jiaoling.getText().toString());
            loginuser.setNikeName(name.getText().toString());
            loginuser.setSex(sex.getText().toString());
            loginuser.setNewInfo("false");
            MyApplication.getInstance().saveUserInfo(loginuser);
            bombUtils.updateUser(loginuser);
        } else {
            ToastUtils.shortToast(this, "请输入昵称");
        }
    }

    //头像选择对话框
    private void showActionSheetDialog(String[] items) {
        ActionSheet actionSheet = new ActionSheet(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true);
        for (int i = 0; i < items.length; i++) {
            actionSheet.addSheetItem(items[i], ActionSheet.SheetItemColor.Blue, SettingActivity.this);
        }
        actionSheet.show();

    }

    @Override
    public void onClick(int which) {
        switch (which) {
            case TAKE_PHOTO:
                //拍照
                imageFile = ImageUploadUtil.createImageFile(ImageUploadUtil.CAMERA_SAVEDIR2, ImageUploadUtil.createImageName());
                startActivityForResult(ImageUploadUtil.intentImageCapture(imageFile), ImageUploadUtil.TAKE_PHOTO);
                break;
            case CHOOSE_PHOTO:
                //从相册选择
                startActivityForResult(ImageUtil.intentChooseImg(),
                        CHOOSE_PHOTO);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == 0) {
                    return;
                }
                //拍照
                ImageUtil.paizhaocreateImagefile(this, imageFile);
                if (imageFile == null) {
                    ToastUtils.shortToast(this, "图片太大无法上传");
                    return;
                }
                doSendThread();//上传图片
                break;
            case CHOOSE_PHOTO:
                //相册
                if (resultCode == 0) return;
                imageUri = data.getData();
                imageFile = MyApplication.getInstance().createimagefile(imageUri, getVmWidth(), getVmHeight());
                if (imageFile == null) {
                    ToastUtils.shortToast(this, "imageFile为空2");
                    return;
                }
                doSendThread();//上传图片
                break;
        }

    }

    /**
     * 上传头像
     */
    private void doSendThread() {
        dialog = ProgressDialog.show(this, null, "正在上传图片...");
//        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
//        head.setImageBitmap(bitmap);
        BmobFile bmobFile = new BmobFile(imageFile);
        bombUtils.upLoadHeadFile(bmobFile, loginuser);
    }

    @Override
    public void onSelect(Pickers pickers) {
        switch (select) {
            case 1:
                sex.setText(pickers.getShowConetnt());
                break;
            case 2:
                age.setText(pickers.getShowConetnt());
                break;
            case 3:
                height.setText(pickers.getShowConetnt());
                break;
            case 4:
                weight.setText(pickers.getShowConetnt());
                break;
            case 5:
                jiaoling.setText(pickers.getShowConetnt());
                break;
        }
    }
}
