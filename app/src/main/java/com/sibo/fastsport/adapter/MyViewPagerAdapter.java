package com.sibo.fastsport.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/20.
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private List<ImageView> list_image = new ArrayList<>();
    private Context context;

    public MyViewPagerAdapter(Context context, List<ImageView> list_image) {
        this.context = context;
        this.list_image = list_image;
    }

    @Override
    public int getCount() {
        return list_image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 加载子视图：将子视图添加到ViewGroup中
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        container.addView(list_image.get(position));

        return list_image.get(position);// 一定要返回子视图
    }

    /**
     * 销毁子视图
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        // ！super.destroyItem(container, position, object);一定要注释掉

        container.removeView(list_image.get(position));
    }
}
