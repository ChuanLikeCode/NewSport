package com.sibo.fastsport.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.ui.MakePlanActivity;
import com.sibo.fastsport.utils.CollectPlan;
import com.sibo.fastsport.utils.MyBombUtils;

/**
 *  制定计划上传完成之后显示二维码
 * Created by chuan on 2017/3/4.
 */

public class MakePlanBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("onReceive","ok");
        Log.e("onReceive", CollectPlan.warmUps.size()
                + CollectPlan.stretchings.size() + CollectPlan.mainActions.size() + CollectPlan.relaxActions.size() + "");
        if (intent.getAction().equals("makePlan")) {
            //Log.e("getAction","ok");
            if (intent.getIntExtra("up", 0) == 1) {
                // Log.e("getIntExtra","ok");
                MyBombUtils.MAKE++;
                if (MyBombUtils.MAKE == CollectPlan.warmUps.size()
                        + CollectPlan.stretchings.size() + CollectPlan.mainActions.size() + CollectPlan.relaxActions.size()) {
                    ((MakePlanActivity) context).handler.sendEmptyMessage(Constant.SUCCESS);
                }
            }
        }
    }
}
