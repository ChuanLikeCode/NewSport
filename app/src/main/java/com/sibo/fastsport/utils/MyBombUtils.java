package com.sibo.fastsport.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.application.MyApplication;
import com.sibo.fastsport.domain.MyCollections;
import com.sibo.fastsport.domain.SportDetail;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.model.DayPlan;
import com.sibo.fastsport.model.MainAction;
import com.sibo.fastsport.model.RelaxAction;
import com.sibo.fastsport.model.Stretching;
import com.sibo.fastsport.model.UserInfo;
import com.sibo.fastsport.model.UserSportPlan;
import com.sibo.fastsport.model.WarmUp;
import com.sibo.fastsport.receiver.MyBroadcastReceiver;
import com.sibo.fastsport.ui.ChooseActionActivity;
import com.sibo.fastsport.ui.EditHomePageActivity;
import com.sibo.fastsport.ui.EditMyInfoActivity;
import com.sibo.fastsport.ui.LoginActivity;
import com.sibo.fastsport.ui.MainActivity;
import com.sibo.fastsport.ui.MakePlanActivity;
import com.sibo.fastsport.ui.RegisterActivity;
import com.sibo.fastsport.ui.SettingActivity;
import com.sibo.fastsport.ui.WxCollectedActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Bomb服务器的工具类
 * Created by Administrator on 2016/10/31.
 */
public class MyBombUtils {


    private final static int SPORT_NAME_FINISH = 1;
    private final static int SPORT_DETAIL_FINISH = 2;
    public static int COUNT = 0;
    public static int MAKE = 0;
    public static int addDayPlan = 0;
    public static List<SportName> list_sportName = new ArrayList<SportName>();
    public static List<SportDetail> list_sportDetail = new ArrayList<SportDetail>();
    public static boolean isFirst = true;
    public static boolean loginSuccess = false;
    public static boolean userHead = false;
    public static boolean isSame = false;

    /**
     * 获取用户的健身名字和id
     */
    public static UserSportPlan userSportPlan;
    /**
     * 获取用户的每天健身计划
     */
    public static List<DayPlan> list_userDayPlan = new ArrayList<>();
    /**
     * 获取热身动作
     */
    public static List<WarmUp> list_warmUp = new ArrayList<>();
    /**
     * 获取拉伸动作
     */
    public static List<Stretching> list_stretching = new ArrayList<>();
    /**
     * 获取具体动作
     */
    public static List<MainAction> list_mainAction = new ArrayList<>();
    /**
     * 获取放松动作
     */
    public static List<RelaxAction> list_relaxAction = new ArrayList<>();

    public static List<UserSportPlan> studentList = new ArrayList<>();
    public static List<UserInfo> userInfoList = new ArrayList<>();
    public Context context;
    private boolean registerSuccess = true;
    private MyBroadcastReceiver receiver;
    public MyBombUtils(Context context) {
        this.context = context;

    }


    /**
     * 扫描学员二维码，填写资料
     * 将学员的信息获取到
     *
     * @param scanner_id
     */
    public void queryStudent(final String scanner_id) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    Log.e("queryStudent", "ok");
                    for (UserInfo info : list) {
                        if (info.getObjectId().equals(scanner_id)) {
                            ((MainActivity) context).makePlan.student = info;
                            ((MainActivity) context).makePlan.handler.sendEmptyMessage(Constant.SUCCESS);
                            break;
                        }
                    }
                } else {
                    Log.e("queryStudent", e.getMessage());
                    ((MainActivity) context).makePlan.handler.sendEmptyMessage(Constant.FAILED);
                }

            }
        });
    }

    /**
     * 学员列表
     *
     * @param account
     */
    public void getStudentInfo(final String account) {
        BmobQuery<UserSportPlan> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserSportPlan>() {
            @Override
            public void done(List<UserSportPlan> list, BmobException e) {
                if (e == null) {
                    studentList.clear();
                    Log.e("getStudentInfo", "ok");
                    for (UserSportPlan u : list) {
                        if (u.getAccount().equals(account)) {
//                            if (studentList.size()==0){
//                                studentList.add(u);
//                            }else {
                            boolean same = false;
                            for (UserSportPlan s : studentList) {
                                if (!s.getStudentId().equals(u.getStudentId())) {
                                    same = false;
                                } else {
                                    same = true;
                                }
                            }
                            if (!same) {
                                studentList.add(u);
                            }
//                            }

                        }
                    }


                    queryStudentUserInfo(studentList);

                } else {
                    Log.e("getStudentInfo", e.getMessage());
                }
            }
        });
    }

    /**
     * 获取教练列表
     *
     * @param id
     */
    public void getTeacherInfo(final String id) {
        BmobQuery<UserSportPlan> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserSportPlan>() {
            @Override
            public void done(List<UserSportPlan> list, BmobException e) {
                if (e == null) {
                    studentList.clear();
                    Log.e("getTeacherInfo", "ok");
                    for (UserSportPlan u : list) {
                        if (u.getStudentId().equals(id)) {
//                            LogUtils.e(u.getStudentId()+"----"+id);
                            boolean same = false;
                            for (UserSportPlan s : studentList) {
                                if (!s.getAccount().equals(u.getAccount())) {
                                    same = false;
//                                    LogUtils.e(s.getAccount());
                                } else {
                                    same = true;
//                                    LogUtils.e(s.getAccount());
                                }
                            }
//                            LogUtils.e(same+"");
                            if (!same) {

                                studentList.add(u);
                            }
//                            }

                        }
                    }


                    queryTeacherUserInfo(studentList);

                } else {
                    Log.e("getTeacherInfo", e.getMessage());
                }
            }
        });
    }

    /**
     * 获取教练的详细信息
     *
     * @param studentList
     */
    public void queryTeacherUserInfo(final List<UserSportPlan> studentList) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    Log.e("queryStudentUserInfo", "ok");
                    for (UserInfo u : list) {
                        for (UserSportPlan s : studentList) {
                            if (u.getAccount().equals(s.getAccount())) {
                                userInfoList.add(u);
                            }
                        }
                    }
                    ((MainActivity) context).student.handler.sendEmptyMessage(Constant.SUCCESS);
                } else {
                    Log.e("queryStudentUserInfo", e.getMessage());
                }

            }
        });
    }

    /**
     * 获取学员的详细信息
     *
     * @param studentList
     */
    public void queryStudentUserInfo(final List<UserSportPlan> studentList) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    Log.e("queryStudentUserInfo", "ok");
                    for (UserInfo u : list) {
                        for (UserSportPlan s : studentList) {
                            if (u.getObjectId().equals(s.getStudentId())) {
                                userInfoList.add(u);
                            }
                        }
                    }
                    ((MainActivity) context).student.handler.sendEmptyMessage(Constant.SUCCESS);
                } else {
                    Log.e("queryStudentUserInfo", e.getMessage());
                }

            }
        });
    }

    /**
     * 编辑个人信息上传图片
     *
     * @param bmobFile
     */
    public void upLoadImg(final BmobFile bmobFile) {
        final int size = ((EditHomePageActivity) context).ImgList.size();
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ((EditHomePageActivity) context).ImgList.add(size - 1, bmobFile);
                    Log.e("ImgList", ((EditHomePageActivity) context).ImgList.size() + "");
                    ((EditHomePageActivity) context).handler.sendEmptyMessage(Constant.UPLOAD_SUCCESS);
                    Log.e("upLoadImg", "ok");
                } else {
                    ((EditHomePageActivity) context).handler.sendEmptyMessage(Constant.FAILED);
                    Log.e("upLoadImg", e.getMessage());
                }
            }
        });
    }

    /**
     * 开始编辑个人信息时的上传图片
     *
     * @param bmobFile
     * @param loginuser
     */
    public void upLoadUserHeadFile(final BmobFile bmobFile, final UserInfo loginuser) {
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    userHead = true;
                    loginuser.setHead(bmobFile);//设置头像
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(Constant.UPLOAD_SUCCESS);
                    //updateUserInfo(loginuser);//更新用户信息
                    Log.e("upLoadUserHeadFile", "ok");
                } else {
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(Constant.FAILED);
                    Log.e("upLoadUserHeadFile", e.getMessage());
                }
            }
        });
    }

    /**
     * 更新User信息--新用户编辑信息
     *
     * @param userInfo
     */
    public void updateFirstInfo(UserInfo userInfo) {
        userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateUserInfo", "ok");
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(Constant.SUCCESS);
                } else {
                    ((EditMyInfoActivity) context).handler.sendEmptyMessage(789);
                    e.printStackTrace();
                    Log.e("updateUserInfo", "failed");
                }
            }
        });
    }

    /**
     * 从bmob获取收藏的文章，比较是否收藏过
     *
     * @param loginuser
     */
    public void queryCollect(final UserInfo loginuser) {
        BmobQuery<MyCollections> query = new BmobQuery<>();
        query.findObjects(new FindListener<MyCollections>() {
            @Override
            public void done(List<MyCollections> list, BmobException e) {
                WxCollectedActivity.collectionList.clear();
                for (MyCollections m :
                        list) {
                    if (m.getAccount().equals(loginuser.getAccount())) {
                        WxCollectedActivity.collectionList.add(m);
                    }
                }
            }
        });
    }
    /**
     * 从bmob获取收藏的文章,以后的修改方案，加入本地数据库中，速度更快
     * @param loginuser
     */
    public void queryCollection(final UserInfo loginuser){
        BmobQuery<MyCollections> query = new BmobQuery<>();
        query.findObjects(new FindListener<MyCollections>() {
            @Override
            public void done(List<MyCollections> list, BmobException e) {
                WxCollectedActivity.collectionList.clear();
                for (MyCollections m:
                        list) {
                    if (m.getAccount().equals(loginuser.getAccount())){
                        WxCollectedActivity.collectionList.add(m);
                    }
                }
                ((WxCollectedActivity)context).handler.sendEmptyMessage(Constant.SUCCESS);
            }
        });
    }

    public void getBackYourAccount(final String phone, final String userPassword){
        final BmobQuery<UserInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null){
                    Log.e("getBackYourAccount", "ok");
                    for (UserInfo a :
                            list) {
                        if (a.getAccount().equals(phone)){
                            UserInfo account = new UserInfo();
                            account.setId(a.getObjectId());
                            account.setAccount(a.getAccount());
                            account.setPassword(userPassword);
                            findPassword(account);
                            break;
                        }
                    }
                }else {
                    Log.e("getBackYourAccount", e.getMessage());
                }
            }
        });
    }
    /**
     * 更新用户密码
     *
     * @param account
     */
    public void findPassword(UserInfo account) {
        account.update(account.getId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateAccountInfo", "ok");
                    ((RegisterActivity) context).handler.sendEmptyMessage(Constant.SUCCESS);
                } else {
                    e.printStackTrace();
                    Log.e("updateAccountInfo", e.getMessage());
                }
            }
        });
    }
    /**
     * 增加收藏文章
     * @param list
     */
    public void addCollection(List<MyCollections> list){
        for (MyCollections m :
                list) {
            m.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        Log.e("addCollection",s);
                    }else {
                        Log.e("addCollection","failed");
                    }
                }
            });
        }
    }

    /**
     * 增加健身计划名字
     * @param userSportPlan
     */
    public void addPlan(UserSportPlan userSportPlan) {
//        String id = "";//数据库唯一标识
//        for (UserSportPlan s:studentList) {
//            if (s.getStudentId().equals(userSportPlan.getStudentId())) {
//                isSame = true;
//                id = s.getObjectId();
//            } else {
//                isSame = false;
//            }
//        }
        /**
         * 判断以前是否有给同样的学员创建健身计划
         * 有：直接更新  无：增加新的计划
         */
//            if (isSame){
//                final String finalId = id;
//                userSportPlan.update(id,new UpdateListener() {
//                    @Override
//                    public void done(BmobException e) {
//                        if (e == null){
//                            CollectPlan.id = finalId;
//                            CollectPlan.prepareToPush();
//                            updateDayPlan();
//                            Log.e("updatePlan", finalId);
//                        }else {
//                            e.printStackTrace();
//                            Log.e("updatePlan","failed");
//                        }
//                    }
//                });
//            }else {
        userSportPlan.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    CollectPlan.id = s;
                    CollectPlan.prepareToPush();
                    addDayPlan();
                    Log.e("addPlan", s);
                } else {
                    e.printStackTrace();
                    Log.e("addPlan", "failed");
                }
            }
                });
//            }

    }

    /**
     * 增加计划列表DayPlan
     */
    public void addDayPlan(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (DayPlan d : CollectPlan.dayPlan){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    d.setId(CollectPlan.id);
                    d.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                Log.e("addDayPlan",s);
                                ((MakePlanActivity) context).handler2.sendEmptyMessage(Constant.SHOW);
                            }else {
                                Log.e("addDayPlan","failed");
                            }
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * 更新计划列表DayPlan
     */
    public void updateDayPlan() {
        for (DayPlan d : CollectPlan.dayPlan) {
            d.setId(CollectPlan.id);
            d.update(CollectPlan.id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.e("updateDayPlan", "ok");
                        ((MakePlanActivity) context).handler2.sendEmptyMessage(Constant.SHOW);
                    } else {
                        Log.e("updateDayPlan", "failed");
                    }
                }
            });
        }
    }

    /**************************************************************************************************/
    /**
     * 创建健身计划，增加热身动作
     */
    public void addWarmUp() {
        Log.e("Bmobsp_warmUp", CollectPlan.warmUps.size()+"");
        if (CollectPlan.warmUps.size()!=0){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (WarmUp w : CollectPlan.warmUps){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        w.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    Log.e("addWarmUp", s);
                                    Intent intent = new Intent("makePlan");
                                    intent.putExtra("up", 1);
                                    context.sendBroadcast(intent);
                                }else {
                                    addWarmUp();
                                    Log.e("addWarmUp","failed");
                                }
                            }
                        });
                    }
                }
            }).start();

        }

    }

    /**
     * 更新热身动作
     */
    public void updateWarmUp() {
        Log.e("Bmobsp_warmUp", CollectPlan.warmUps.size() + "");
        if (CollectPlan.warmUps.size() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (WarmUp w : CollectPlan.warmUps) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        w.update(w.getId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.e("updateWarmUp", "ok");
                                    Intent intent = new Intent("makePlan");
                                    intent.putExtra("up", 1);
                                    context.sendBroadcast(intent);
                                } else {
                                    Log.e("updateWarmUp", "failed");
                                }
                            }
                        });
                    }
                }
            }).start();

        }

    }
    /**
     * 创建健身计划，增加拉伸动作
     */
    public void addStretching(){
        //Log.e("addStretching",CollectPlan.stretchings.size()+"");
        if (CollectPlan.stretchings.size() != 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Stretching s : CollectPlan.stretchings){
                        //stre++;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        s.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    Log.e("addStretching", s);
                                    Intent intent = new Intent("makePlan");
                                    intent.putExtra("up", 1);
                                    context.sendBroadcast(intent);
                                }else {
                                    addStretching();
                                    Log.e("addStretching","failed");
                                }
                            }
                        });
                    }
                }
            }).start();

        }

    }

    /**
     * 更新拉伸动作
     */
    public void updateStretching() {
        //Log.e("addStretching",CollectPlan.stretchings.size()+"");
        if (CollectPlan.stretchings.size() != 0) {
            for (Stretching s : CollectPlan.stretchings) {
                //stre++;
                s.update(s.getId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("updateStretching", "ok");
                            Intent intent = new Intent("makePlan");
                            intent.putExtra("up", 1);
                            context.sendBroadcast(intent);
                        } else {
                            Log.e("updateStretching", "failed");
                        }
                    }
                });
            }
        }

    }

    /**
     * 创建健身计划，增加具体动作
     */
    public void addMainAction(){
        //Log.e("addMainAction",CollectPlan.mainActions.size()+"");
        if (CollectPlan.mainActions.size() != 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (MainAction m : CollectPlan.mainActions){
                        //mainAction++;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        m.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    Log.e("addMainAction", s);
                                    Intent intent = new Intent("makePlan");
                                    intent.putExtra("up", 1);
                                    context.sendBroadcast(intent);
                                }else {
                                    addMainAction();
                                    Log.e("addMainAction","failed");
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }

    /**
     * 更新具体动作
     */
    public void updateMainAction() {
        //Log.e("addMainAction",CollectPlan.mainActions.size()+"");
        if (CollectPlan.mainActions.size() != 0) {
            for (MainAction m : CollectPlan.mainActions) {
                //mainAction++;
                m.update(m.getId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("updateMainAction", "ok");
                            Intent intent = new Intent("makePlan");
                            intent.putExtra("up", 1);
                            context.sendBroadcast(intent);
                        } else {
                            Log.e("updateMainAction", "failed");
                        }
                    }
                });
            }
        }
    }

    /**
     * 创建健身计划，增加放松动作
     */
    public void addRelaxAction(){
      //  Log.e("addRelaxAction",CollectPlan.relaxActions.size()+"");
        if (CollectPlan.relaxActions.size() != 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (RelaxAction r : CollectPlan.relaxActions){
                        //relaxAction++;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        r.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){

                                    Log.e("addRelaxAction", s);
                                    Intent intent = new Intent("makePlan");
                                    intent.putExtra("up", 1);
                                    context.sendBroadcast(intent);
                                }else {
                                    addRelaxAction();
                                    Log.e("addRelaxAction","failed");
                                }
                            }
                        });
                    }
                }
            }).start();

        }

    }

    /**
     * 更新放松动作
     */
    public void updateRelaxAction() {
        //  Log.e("addRelaxAction",CollectPlan.relaxActions.size()+"");
        if (CollectPlan.relaxActions.size() != 0) {
            for (RelaxAction r : CollectPlan.relaxActions) {
                //relaxAction++;
                r.update(r.getId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {

                            Log.e("updateRelaxAction", "ok");
                            Intent intent = new Intent("makePlan");
                            intent.putExtra("up", 1);
                            context.sendBroadcast(intent);
                        } else {
                            Log.e("updateRelaxAction", "failed");
                        }
                    }
                });
            }
        }

    }

    /**
     * 增加用户
     * @param account
     */
    public void addUserInfo(final UserInfo account) {
        account.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    ((RegisterActivity) context).userInfo.setId(s);
                    updateUserInfo(((RegisterActivity) context).userInfo);
                    ((RegisterActivity) context).handler.sendEmptyMessage(Constant.SUCCESS);
                }else {
                    Log.e("Bmob-updateInfoFailed", e.getMessage());
                }
            }
        });
    }

/**************************************************************************************/
    public void getUserSportPlan(final String id){
        BmobQuery<UserSportPlan> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserSportPlan>() {
            @Override
            public void done(List<UserSportPlan> list, BmobException e) {
                if (e == null) {
                    Log.e("Bmob-UserSportSuccess", "ok");
                    Intent intent = new Intent("scannerFinish");
                    intent.putExtra("finish",1);
                    context.sendBroadcast(intent);
                    for (UserSportPlan s : list){
                        if (id.equals(s.getObjectId())){
                            userSportPlan = s;
                            break;
                        }
                    }
                } else {
                    ((MainActivity) context).myPlanFragment.handler.sendEmptyMessage(Constant.FAILED);
                    Log.e("Bmob-UserSport", "Failed");
                }
            }
        });
    }

    public void getDayPlan(final String id){
        list_userDayPlan.clear();
        BmobQuery<DayPlan> query = new BmobQuery<>();
        query.findObjects(new FindListener<DayPlan>() {
            @Override
            public void done(List<DayPlan> list, BmobException e) {
                if (e == null) {
                    Log.e("Bmob-getDayPlanSuccess", "ok");
                    Intent intent = new Intent("scannerFinish");
                    intent.putExtra("finish",1);
                    context.sendBroadcast(intent);
                    for (DayPlan d : list){
                        if (d.getId().equals(id)){
                            list_userDayPlan.add(d);
                        }
                    }
                } else {
                    Log.e("Bmob-getDayPlanFailed", "Failed");
                }

            }
        });
    }
    /**
     * 获取所有的健身动作名字
     */
    public void getPlanAllName(){

        BmobQuery<SportName> query = new BmobQuery<>();
        query.findObjects(new FindListener<SportName>() {
            @Override
            public void done(List<SportName> list, BmobException e) {
                    if (e == null){
                        list_sportName.clear();
                        list_sportName.addAll(list);
                        Intent intent = new Intent("scannerFinish");
                        intent.putExtra("finish",1);
                        context.sendBroadcast(intent);
                        Log.e("getPlanAllName","ok");
                    }else {
                        Log.e("getPlanAllName","failed");
                    }
            }
        });
    }

    /**
     * 获取所有的健身动作细节
     */
    public void getPlanAllDetail(){
        BmobQuery<SportDetail> query = new BmobQuery<>();
        query.findObjects(new FindListener<SportDetail>() {
            @Override
            public void done(List<SportDetail> list, BmobException e) {
                if (e == null){
                    list_sportDetail.clear();
                    list_sportDetail.addAll(list);
                    Intent intent = new Intent("scannerFinish");
                    intent.putExtra("finish", 1);
                    context.sendBroadcast(intent);
                    Log.e("getPlanAllDetail", "ok");
                } else {
                    Log.e("getPlanAllDetail","failed");
                }
            }
        });
    }

    public void getWarmUp(final String id){
        list_warmUp.clear();
        BmobQuery<WarmUp> query = new BmobQuery<>();
        query.findObjects(new FindListener<WarmUp>() {
            @Override
            public void done(List<WarmUp> list, BmobException e) {
                if (e == null) {
                    Log.e("Bmob-getWarmUpSuccess", "ok");
                    Intent intent = new Intent("scannerFinish");
                    intent.putExtra("finish",1);
                    context.sendBroadcast(intent);
                    for (WarmUp w : list){
                        if (w.getId().equals(id)){
                            list_warmUp.add(w);
                        }
                    }
                } else {
                    Log.e("Bmob-getWarmUpFailed", "Failed");
                }
            }
        });
    }

    public void getStretching(final String id){
        list_stretching.clear();
        BmobQuery<Stretching> query = new BmobQuery<>();
        query.findObjects(new FindListener<Stretching>() {
            @Override
            public void done(List<Stretching> list, BmobException e) {
                if (e == null) {
                    Log.e("Bmob-getStretching", "ok");
                    Intent intent = new Intent("scannerFinish");
                    intent.putExtra("finish",1);
                    context.sendBroadcast(intent);
                    for (Stretching s : list){
                        if (s.getId().equals(id)){
                            list_stretching.add(s);
                        }
                    }
                } else {
                    Log.e("Bmob-getStretching", "Failed");
                }
            }
        });
    }

    public void getMainAction(final String id){
        list_mainAction.clear();
        BmobQuery<MainAction> query = new BmobQuery<>();
        query.findObjects(new FindListener<MainAction>() {
            @Override
            public void done(List<MainAction> list, BmobException e) {
                if (e == null) {
                    Log.e("Bmob-getMainAction", "ok");
                    Intent intent = new Intent("scannerFinish");
                    intent.putExtra("finish",1);
                    context.sendBroadcast(intent);
                    for (MainAction m : list){
                        if (m.getId().equals(id)){
                            list_mainAction.add(m);
                        }
                    }
                } else {
                    Log.e("Bmob-getMainAction", "Failed");
                }
            }
        });
    }

    public void getRelaxAction(final String id){
        list_relaxAction.clear();
        BmobQuery<RelaxAction> query = new BmobQuery<>();
        query.findObjects(new FindListener<RelaxAction>() {
            @Override
            public void done(List<RelaxAction> list, BmobException e) {
                if (e == null) {
                    Log.e("Bmob-getRelaxAction", "ok");
                    Intent intent = new Intent("scannerFinish");
                    intent.putExtra("finish",1);
                    context.sendBroadcast(intent);
                    for (RelaxAction r : list){
                        if (r.getId().equals(id)){
                            list_relaxAction.add(r);
                        }
                    }
                } else {
                    Log.e("Bmob-getRelaxAction", "Failed");
                }
            }
        });
    }
 /**************************************************************************************************/

    /**
     * 更新User信息
     * @param userInfo
     */
    public void updateUserInfo(UserInfo userInfo){
        userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateUserInfo", "ok");
                } else {
                    e.printStackTrace();
                    Log.e("updateUserInfo", "failed");
                }
            }
        });
    }

    /**
     * 更新User信息--设置专用
     *
     * @param userInfo
     */
    public void updateUser(UserInfo userInfo) {
        userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateUserInfo", "ok");
                    ((SettingActivity) context).handler.sendEmptyMessage(Constant.SUCCESS);
                } else {
                    ((SettingActivity) context).handler.sendEmptyMessage(789);
                    e.printStackTrace();
                    Log.e("updateUserInfo", e.getMessage());
                }
            }
        });
    }

    /**
     * 编辑个人信息时的上传图片---设置专用
     *
     * @param bmobFile
     * @param loginuser
     */
    public void upLoadHeadFile(final BmobFile bmobFile, final UserInfo loginuser) {
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    userHead = true;
                    loginuser.setHead(bmobFile);//设置头像
                    ((SettingActivity) context).handler.sendEmptyMessage(Constant.UPLOAD_SUCCESS);
                    //updateUserInfo(loginuser);//更新用户信息
                    Log.e("upLoadUserHeadFile", "ok");
                } else {
                    ((SettingActivity) context).handler.sendEmptyMessage(Constant.FAILED);
                    Log.e("upLoadUserHeadFile", e.getMessage());
                }
            }
        });
    }

    /**
     * 更新User信息---编辑我的主页
     *
     * @param userInfo
     */
    public void updateInfo(UserInfo userInfo) {
        userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ((EditHomePageActivity) context).handler.sendEmptyMessage(123);
                    Log.e("updateUserInfo", "ok");
                } else {
                    e.printStackTrace();
                    Log.e("updateUserInfo", e.getMessage());
                }
            }
        });
    }
    /**
     * 注册新用户验证
     * @param userPhone 账号
     */
    public void registerChecked(final String userPhone) {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                for (UserInfo a :
                        list) {
                    if (a.getAccount().equals(userPhone)) {
                        registerSuccess = false;
                        break;
                    }
                }
                if (!registerSuccess) {
                    ((RegisterActivity) context).handler.sendEmptyMessage(404);
                } else {
                    ((RegisterActivity) context).handler.sendEmptyMessage(200);
                }
            }
        });
    }

    /**
     * 登录查询是否账号密码输入错误
     */
    public void queryAccount(final UserInfo account) {
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    Log.e("queryAccount", "ok");
                    for (UserInfo a :
                            list) {
                        if (a.getAccount().equals(account.getAccount()) && a.getPassword().equals(account.getPassword())) {
                            loginSuccess = true;
//                            a.setId(a.getObjectId());
                            MyApplication.getInstance().saveUserInfo(a);
//                            updateAccountInfo(a);
//                            ((LoginActivity) context).account.setId(a.getObjectId());
                            break;
                        }
                    }
                } else {
                    e.printStackTrace();
                    Log.e("queryAccount", "Failed");
                }
                ((LoginActivity) context).handler.sendEmptyMessage(Constant.RESULT_SUCCESS);
            }
        });
    }
    /**
     * 更新用户信息
     *
     * @param account
     */
    public void updateAccountInfo(UserInfo account) {
        account.update(account.getId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("updateAccountInfo", "ok");
                } else {
                    e.printStackTrace();
                    Log.e("updateAccountInfo", e.getMessage());
                }
            }
        });
    }

    /**
     * 初始化动作细节
     */
    public void initSportDetailData() {
        BmobQuery<SportDetail> query = new BmobQuery<SportDetail>();
        query.findObjects(new FindListener<SportDetail>() {
            @Override
            public void done(List<SportDetail> list, BmobException e) {
                if (e == null){
                    list_sportDetail.clear();
                    list_sportDetail.addAll(list);
                    Message message = new Message();
                    message.arg2 = SPORT_DETAIL_FINISH;
                    ((ChooseActionActivity) context).handler.sendMessage(message);
                }else {
                    Toast.makeText(context,"获取失败，请返回重新获取",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    /**
     * 初始化动作名字
     */
    public void initSportNameData() {
        BmobQuery<SportName> query = new BmobQuery<SportName>();
        query.findObjects(new FindListener<SportName>() {
            @Override
            public void done(List<SportName> list, BmobException e) {
                if (e == null){
                    list_sportName.clear();
                    list_sportName.addAll(list);
                    Message message = new Message();
                    message.arg1 = SPORT_NAME_FINISH;
                    ((ChooseActionActivity) context).handler.sendMessage(message);
                }else {
                    Toast.makeText(context,"获取失败，请返回重新获取",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
