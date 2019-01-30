package com.sibo.fastsport.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MyCollectionAdapter;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.domain.MyCollections;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.StatusBarUtil;
import com.sibo.fastsport.view.WhorlView;

import java.util.ArrayList;
import java.util.List;

public class WxCollectedActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    //收藏的集合
    public static List<MyCollections> collectionList = new ArrayList<>();
    //标题栏控件
    private ImageView back;
    private TextView title;
    //ListView控件
    private ListView listView;
    //进度条控件
    private WhorlView whorlView;
    //适配器
    private MyCollectionAdapter adapter;
    //提示文字
    private TextView tips;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.SUCCESS:
                    if (collectionList.size()==0){
                        whorlView.setVisibility(View.GONE);
                        tips.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }else {
                        tips.setVisibility(View.GONE);
                        whorlView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                    whorlView.stop();
                    break;
            }
        }
    };

    @Override
    protected void findViewByIDS() {
        whorlView = (WhorlView) findViewById(R.id.collected_loading);
        tips = (TextView) findViewById(R.id.collected_tip);
        back = (ImageView) findViewById(R.id.collected_back);
        title = (TextView) findViewById(R.id.collected_title);
        listView = (ListView) findViewById(R.id.collected_listView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_wx_collected);
        initData();
        initListener();
    }

    private void initListener() {
        listView.setOnItemClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        tips.setVisibility(View.GONE);
        whorlView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        whorlView.start();
        /*获取收藏列表*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyBombUtils myBombUtils = new MyBombUtils(WxCollectedActivity.this);
                myBombUtils.queryCollection(loginuser);
            }
        }).start();
        title.setText(R.string.myCollection);
        adapter = new MyCollectionAdapter(this,collectionList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(WxCollectedActivity.this,ShowWXActivity.class);
        intent.putExtra("url",collectionList.get(position).getUrl());
        startActivity(intent);
    }
}
