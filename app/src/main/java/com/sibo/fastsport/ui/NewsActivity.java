package com.sibo.fastsport.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.WXitemAdapter;
import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.domain.MyCollections;
import com.sibo.fastsport.domain.WXItem;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.StatusBarUtil;
import com.sibo.fastsport.utils.WXArticleUtils;
import com.sibo.fastsport.view.WhorlView;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static int screen_height;
    public static int screen_width;
    //用于显示的列表集合
    private static List<WXItem> wxItemList = new ArrayList<>();
    private static int TAG = 0;//标记上拉下拉
    private static int offset = 0;//标记每次获取文章的偏移量
    private static int count = 1;//标记第几次获取文章
    //文章的收藏集合
    public List<WXItem> collectionList = new ArrayList<>();
    //返回键
    private ImageView back;
    //下拉刷新，上拉加载控件  适配器
    private PullToRefreshListView pfl;
    //PullToRefreshListView得到的ListView
    private ListView listView;
    //微信文章适配器
    private WXitemAdapter adapter;
    private WhorlView whorlView;
    private WXArticleUtils wxArticleUtils;//获取微信文章的工具类
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.SUCCESS:
                    pfl.setVisibility(View.VISIBLE);
                    whorlView.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    pfl.onRefreshComplete();
                    break;
                case Constant.NO_MORE:
                    Toast.makeText(NewsActivity.this,R.string.noMore,Toast.LENGTH_SHORT).show();
                    pfl.onRefreshComplete();
                    break;
            }
        }
    };


    @Override
    protected void findViewByIDS() {
        pfl = (PullToRefreshListView) findViewById(R.id.wx_pfl);
        back = (ImageView) findViewById(R.id.news_back);
        whorlView = (WhorlView) findViewById(R.id.news_loading);
        pfl.setMode(PullToRefreshBase.Mode.BOTH);
        listView = pfl.getRefreshableView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenWH();
        setContentView(R.layout.activity_news);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        initListener();
        initData();
        if (WXArticleUtils.isFirst){
            getDataWX();
            WXArticleUtils.isFirst = false;
        }else{
            pfl.setVisibility(View.VISIBLE);
            whorlView.setVisibility(View.GONE);
        }
        collectionList.clear();
    }

    private void getDataWX(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<WXItem> list = wxArticleUtils.getArticle(5,offset);
                if (list.size() != 0){
                    if (TAG == 0){
                        wxItemList.clear();
                        wxItemList.addAll(list);
                    }else {
                        wxItemList.addAll(list);
                    }
                    handler.sendEmptyMessage(Constant.SUCCESS);
                }else {
                    handler.sendEmptyMessage(Constant.NO_MORE);
                }

            }
        }).start();
    }

    private void initData() {
        wxArticleUtils = new WXArticleUtils();
        whorlView.start();
        pfl.setVisibility(View.GONE);
        adapter = new WXitemAdapter(this,wxItemList);
        listView.setAdapter(adapter);
    }

    private void initListener() {
        listView.setOnItemClickListener(this);
        back.setOnClickListener(this);
        pfl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                TAG = 0;
                count = 1;
                offset = 0;
                getDataWX();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                TAG = 1;
                offset = count*5;
                count++;
                getDataWX();
            }
        });
    }

    private void getScreenWH() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screen_height = metrics.heightPixels/3-80;
        screen_width = (metrics.widthPixels-50)/3;
    }

    /**
     * 用户点击返回之前将收藏的文章上传bmob
     * @param v
     */
    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MyCollections> myCollectionses = new ArrayList<>();
                for (WXItem w :
                        collectionList) {
                    MyCollections collections = new MyCollections();
                    collections.setAccount(loginuser.getAccount());
                    collections.setTitle(w.getTitle());
                    collections.setUpdateTime(w.getUpdateTime());
                    collections.setImgUrl1(w.getImg().get(0));
                    collections.setImgUrl2(w.getImg().get(1));
                    collections.setImgUrl3(w.getImg().get(2));
                    collections.setUrl(w.getUrl());
                    myCollectionses.add(collections);
                }
                MyBombUtils myBombUtils = new MyBombUtils(NewsActivity.this);
                myBombUtils.addCollection(myCollectionses);
            }
        }).start();
        finish();
    }

    /**
     * 点击文章显示详细信息
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Log.e("positoin",position+"----"+wxItemList.get(position).getTitle());
        Intent intent = new Intent(NewsActivity.this,ShowWXActivity.class);
        intent.putExtra("url",wxItemList.get(position-1).getUrl());
        startActivity(intent);
    }
}









