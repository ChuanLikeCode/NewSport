package com.sibo.fastsport.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.utils.StatusBarUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MyCodeActivity extends BaseActivity {

    private TextView title;
    private ImageView code;

    @Override
    protected void findViewByIDS() {
        title = findViewsById(R.id.top_tv_title);
        code = findViewsById(R.id.tv_myCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_my_code);
        title.setText("我的二维码");
        Bitmap bitmap = CodeUtils.createImage(loginuser.getId(), 250, 250, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        code.setImageBitmap(bitmap);
    }
}
