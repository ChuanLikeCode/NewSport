package com.sibo.fastsport.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sibo.fastsport.R;
import com.sibo.fastsport.adapter.MyChooseActionAdapter;
import com.sibo.fastsport.base.BaseActivity;
import com.sibo.fastsport.domain.SportDetail;
import com.sibo.fastsport.domain.SportName;
import com.sibo.fastsport.utils.MakePlanUtils;
import com.sibo.fastsport.utils.MyBombUtils;
import com.sibo.fastsport.utils.StatusBarUtil;
import com.sibo.fastsport.view.WhorlView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseActionActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    //完成网络请求标志 sportName = 1 sportDetail = 2
    private final static int SPORT_NAME_FINISH = 1;
    private final static int SPORT_DETAIL_FINISH = 2;
    //返回选择的动作集合
    public static List<SportName> list_result = new ArrayList<>();
    //进度条
    private WhorlView whorlView;
    //未找到健身动作时显示提示
    private TextView tips;
    //part--不限部位 equipment--不限机械  action_type--动作类型  level---全部难度
    private HashMap<Integer, String[]> dialogItemMap = new HashMap<>();
    private String[] part_tpye = {"不限部位", "胸部", "背部", "腿部", "肱二头肌", "肱三头肌", "肩部", "前臂", "腹部"};
    private String[] equipment_type = {"不限器械", "哑铃", "健身房器械", "无器械", "杠铃"};
    private String[] action_type = {"不限类型", "热身动作", "拉伸", "具体动作", "放松动作"};
    private String[] level_type = {"全部难度", "零基础", "初学", "进阶", "强化", "挑战"};
    //定义搜索文字显示与图片
    private TextView[] peal = new TextView[4];
    private ImageView[] iv_peal = new ImageView[4];
    private int[] pealIds = {R.id.choose_part, R.id.choose_equipment,
            R.id.choose_type, R.id.choose_level};
    private int[] iv_pealIds = {R.id.choose_part_down, R.id.choose_equipment_down,
            R.id.choose_type_down, R.id.choose_level_down};
    //显示健身动作的listView
    private ListView listView;
    //动作列表集合
    private List<SportName> list = new ArrayList<>();
    //用来筛选动作列表显示的集合
    private List<SportName> temp_list = new ArrayList<>();
    //bmob服务器的工具类
    private MyBombUtils myBombUtils;
    //listView的适配器
    private MyChooseActionAdapter adapter;
    //消息处理机制，主要用来从网络获取的数据完成之后发送消息显示内容功能
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg2 == SPORT_DETAIL_FINISH) {
                whorlView.stop();
                whorlView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                list.addAll(MyBombUtils.list_sportName);
                adapter = new MyChooseActionAdapter(ChooseActionActivity.this, list);
                listView.setAdapter(adapter);
            }
        }
    };
    //筛选的文字提示
    private String part, equipment, action, level;
    //对话框
    private AlertDialog.Builder builder;
    //选择的是第几个筛选器
    private int selected;
    //顶部标题栏
    private Toolbar toolbar;
    private ImageView back, close;
    private TextView title, ok;
    /**
     * 返回监听事件
     */
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MakePlanUtils.isFirst = true;
            list_result.clear();
            finish();
        }
    };
    /**
     * 选择完成后执行的方法，将选择的动作加入list中
     * 清空list_result，执行跳转
     */
    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!list_result.isEmpty()){
                MakePlanUtils.list.addAll(list_result);
                list_result.clear();
                //MyChooseActionAdapter.status.clear();
                Intent intent = new Intent(ChooseActionActivity.this, MakePlanActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(ChooseActionActivity.this,"还没有选择健身动作哦~~",Toast.LENGTH_SHORT).show();
            }

        }
    };
    /**
     * 筛选动作的监听事件
     */
    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            peal[selected].setText(dialogItemMap.get(selected)[which]);
            whorlView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            MyChooseActionAdapter.status.clear();//清空选择状态标识
            showList();
        }
    };

    @Override
    protected void findViewByIDS() {
        listView = (ListView) findViewById(R.id.choose_action_listView);
        whorlView = (WhorlView) findViewById(R.id.loading);
        tips = (TextView) findViewById(R.id.choose_action_tips);
        toolbar = (Toolbar) findViewById(R.id.choose_title);
        title = (TextView) toolbar.findViewById(R.id.tv_title_bar);
        ok = (TextView) toolbar.findViewById(R.id.tv_complete_titlebar);
        back = (ImageView) toolbar.findViewById(R.id.iv_back_titlebar);
        close = (ImageView) toolbar.findViewById(R.id.iv_close_titlebar);
        for (int i = 0; i < pealIds.length; i++) {
            peal[i] = (TextView) findViewById(pealIds[i]);
            iv_peal[i] = (ImageView) findViewById(iv_pealIds[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarDarkMode(this, StatusBarUtil.StatusBarLightMode(this));
        setContentView(R.layout.activity_choose_action);
        initData();
        initListener();
        MakePlanUtils.list.clear();
    }

    /**
     * 设置监听事件
     */
    private void initListener() {
        for (int i = 0; i < pealIds.length; i++) {
            peal[i].setOnClickListener(this);
            iv_peal[i].setOnClickListener(this);
        }
        back.setOnClickListener(backListener);
        ok.setOnClickListener(okListener);
        listView.setOnItemLongClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
//        for (int i = 0;i<MakePlanAdapter.status.size();i++){
//            Log.e("status",MakePlanAdapter.status.get(i)+"");
//        }
        whorlView.start();//使进度条动起来
        close.setVisibility(View.GONE);
        title.setText(R.string.addDongZuo);
        ok.setText(R.string.select);
        //判断是否是第一次运行加载网络数据
        //是：加载
        //否：直接调用MyBombUtils中静态的数据
        if (MyBombUtils.isFirst) {
            MyBombUtils.isFirst = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //初始化bmob
                    myBombUtils = new MyBombUtils(ChooseActionActivity.this);
                    //里面是网络请求会有延迟所以应该在请求的地方设置一个handler发送请求完成的消息
                    //然后再来更新UI界面
                    myBombUtils.initSportNameData();
                    myBombUtils.initSportDetailData();
                }
            }).start();
        } else {
            whorlView.stop();
            whorlView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            list.addAll(MyBombUtils.list_sportName);
            adapter = new MyChooseActionAdapter(ChooseActionActivity.this, list);
            listView.setAdapter(adapter);
        }
        //设置map数据，对话框使用
        dialogItemMap.put(0, part_tpye);
        dialogItemMap.put(1, equipment_type);
        dialogItemMap.put(2, action_type);
        dialogItemMap.put(3, level_type);
        builder = new AlertDialog.Builder(this);
    }


    /**
     * 点击显示对话框
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_part:
            case R.id.choose_part_down:
                selected = 0;
                builder.setItems(dialogItemMap.get(0), listener);
                break;
            case R.id.choose_equipment:
            case R.id.choose_equipment_down:
                selected = 1;
                builder.setItems(dialogItemMap.get(1), listener);
                break;
            case R.id.choose_type:
            case R.id.choose_type_down:
                selected = 2;
                builder.setItems(dialogItemMap.get(2), listener);
                break;
            case R.id.choose_level:
            case R.id.choose_level_down:
                selected = 3;
                builder.setItems(dialogItemMap.get(3), listener);
                break;
        }
        builder.show();
    }

    /**
     * 筛选动作
     */
    private void showList() {
        part = peal[0].getText().toString();
        equipment = peal[1].getText().toString();
        action = peal[2].getText().toString();
        level = peal[3].getText().toString();
        list.clear();
        list.addAll(MyBombUtils.list_sportName);
        deletePart();//去除没有选择的锻炼部位
        deleteEquipment();//去除没有选择的器械
        deleteAction();//去除没有选择的动作类型
        deleteLevel();//去除没有选择的难度等级
        adapter.notifyDataSetChanged();
        //若筛选之后list元素个数为0，则显示提示框
        if (list.size() == 0) {
            listView.setVisibility(View.GONE);
            tips.setVisibility(View.VISIBLE);
            whorlView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tips.setVisibility(View.GONE);
            whorlView.setVisibility(View.GONE);
        }
    }

    /**
     * 去除不需要的level
     */
    private void deleteLevel() {
        temp_list.clear();
        int temp_level = -1;
        switch (level) {
            case "全部难度":
                temp_level = 0;
                break;
            case "零基础":
                temp_level = 1;
                break;
            case "初学":
                temp_level = 2;
                break;
            case "进阶":
                temp_level = 3;
                break;
            case "强化":
                temp_level = 4;
                break;
            case "挑战":
                temp_level = 5;
                break;
        }
        for (SportName s :
                MyBombUtils.list_sportName) {
            if (s.getLevel() != temp_level) {
                if (temp_level != 0) {
                    temp_list.add(s);
                }
            }
        }
        list.removeAll(temp_list);
    }

    /**
     * 去除不需要的动作类型
     */
    private void deleteAction() {
        temp_list.clear();
        for (SportName s :
                MyBombUtils.list_sportName) {
            if (!s.getType().equals(action)) {
                if (!action.equals("不限类型")) {
                    temp_list.add(s);
                }
            }
        }
        list.removeAll(temp_list);
    }

    /**
     * 去除不需要的器械
     */
    private void deleteEquipment() {
        temp_list.clear();
        for (SportDetail s :
                MyBombUtils.list_sportDetail) {
            if (!s.getNeed_equipment().equals(equipment)) {
                if (!equipment.equals("不限器械")) {
                    temp_list.add(matcher(s.getName()));
                }
            }
        }
        list.removeAll(temp_list);
    }

    /**
     * 去除不需要的锻炼部位
     */
    private void deletePart() {
        temp_list.clear();
        for (SportDetail s :
                MyBombUtils.list_sportDetail) {
            if (!s.getExercise_part().equals(part)) {
                if (!part.equals("不限部位")) {
                    temp_list.add(matcher(s.getName()));
                }
            }
        }
        list.removeAll(temp_list);
    }

    /**
     * 匹配SportName和SportDetail
     *
     * @param name
     * @return
     */
    private SportName matcher(String name) {
        for (SportName s :
                MyBombUtils.list_sportName) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * 长按显示动作详情
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ChooseActionActivity.this, ActionDetailsActivity.class);
        SportName sportName = list.get(position);
        SportDetail sportDetail = null;
        //筛选出需要传递的SportDetail
        for (SportDetail sd :
                MyBombUtils.list_sportDetail) {
            if (sd.getName().equals(sportName.getName())) {
                sportDetail = sd;
                break;
            }
        }
        //传递数据
        Bundle bundle = new Bundle();
        bundle.putSerializable("sportName", sportName);
        bundle.putSerializable("sportDetail", sportDetail);
        intent.putExtra("details", bundle);
        startActivity(intent);
        return false;
    }
}
