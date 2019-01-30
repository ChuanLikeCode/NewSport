package com.sibo.fastsport.adapter;

/**
 * Created by Administrator on 2016/10/24.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {


    List<Fragment> list = new ArrayList<Fragment>();

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }


    public MyFragmentAdapter(FragmentManager fm, List<Fragment> list) {
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
