package com.sibo.fastsport.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sibo.fastsport.fragment.BaseDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MyDayFragmentAdapter extends FragmentPagerAdapter {
    List<BaseDay> list = new ArrayList<BaseDay>();

    public MyDayFragmentAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    public MyDayFragmentAdapter(FragmentManager fm, List<BaseDay> list) {
        super(fm);
        this.list = list;
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
}
