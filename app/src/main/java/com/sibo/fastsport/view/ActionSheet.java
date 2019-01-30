package com.sibo.fastsport.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sibo.fastsport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的actionSheet,有三部分组成（1.title   2.item  3.cancle按钮）
 * 当item 的条目多过8条时，item就能滚动显示
 */
public class ActionSheet {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;//标题，默认是没有标题的
    private TextView txt_cancel;//取消按钮
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<SheetItem> sheetItems;
    private Display display;

    public ActionSheet(Context context) {
        this.context = context;
        // 取得window对象
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        //得到窗口的尺寸对象
        display = windowManager.getDefaultDisplay();
    }

    // 为actionSheet构建自定义的控件
    public ActionSheet builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_actionsheet, null);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        // 获取自定义Dialog布局中的控件
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view
                .findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    //为actionSheet设置标题
    public ActionSheet setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    // 设置dialog是否能够取消
    public ActionSheet setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    // 设置dialog在屏幕外部是否能够取消
    public ActionSheet setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param strItem  条目名称
     * @param color    条目字体颜色，设置null则默认蓝色
     * @param listener
     * @return actionSheet
     */
    public ActionSheet addSheetItem(String strItem, SheetItemColor color,
                                    OnSheetItemClickListener listener) {
        if (sheetItems == null) {
            sheetItems = new ArrayList<SheetItem>();
        }
        sheetItems.add(new SheetItem(strItem, color, listener));
        return this;
    }

    /**
     * 设置条目布局
     */
    @SuppressWarnings("deprecation")
    private void setSheetItems() {
        if (sheetItems == null || sheetItems.size() <= 0) {
            return;
        }
        int size = sheetItems.size();//除去title和cancle 按钮显示的总条目数
        // 添加条目过多的时候控制高度
        if (size >= 8) {//当条目多余八条时就可以滚动显示
            LayoutParams params = (LayoutParams) sLayout_content
                    .getLayoutParams();
            //我这里设置的item的课显示总高度为屏幕高度的3/5
            params.height = display.getHeight() * 3 / 5;
            sLayout_content.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItems.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;
            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }

            // 字体颜色
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }

            // 高度
            float scale = context.getResources().getDisplayMetrics().density;//获取屏幕密度
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, height));//textView设置宽高

            // 点击事件
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index);
                    dialog.dismiss();
                }
            });

            lLayout_content.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E");
        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;

        public SheetItem(String name, SheetItemColor color,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }
}
