package com.sibo.fastsport.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.ui.MainActivity;
import com.sibo.fastsport.utils.MyBombUtils;

/**
 * 扫码获得健身计划
 * Created by zhouchuan on 2017/3/2.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    private static MyBroadcastReceiver receiver = null;

    public static MyBroadcastReceiver newInstancce() {
        if (receiver == null) {
            receiver = new MyBroadcastReceiver();
        }
        return receiver;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("onReceive","ok");
        if (intent.getAction().equals("scannerFinish")) {
            // Log.e("scannerFinish","ok");
            if (intent.getIntExtra("finish",0) == 1){
                //  Log.e("finish","ok");
                MyBombUtils.COUNT++;
                if (MyBombUtils.COUNT == 8){
                    ((MainActivity) context).myPlanFragment.handler.sendEmptyMessage(Constant.SUCCESS);
                }
            }

        }
    }
}
