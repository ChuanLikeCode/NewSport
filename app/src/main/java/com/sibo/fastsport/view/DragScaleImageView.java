package com.sibo.fastsport.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.sibo.fastsport.R;

public class DragScaleImageView extends RelativeLayout {
    private static final int BACK_SCALE = 1010;
    /**
     * 拖拉照片模式
     */
    private final int MODE_DRAG = 1;
    float scale = 1;
    private String TAG = "DragScaleImageView";
    private Context mContext;
    private AttributeSet attrs;
    private int displayWidth = 0;
    private int displayHeight = 0;
    private int mImageId;
    private Bitmap bmp;
    private ImageView imageView;
    /**
     * 是否处在回弹状态
     */
    private boolean isBacking = false;
    /**
     * 用于记录拖拉图片移动的坐标位置
     */
    private Matrix matrix = new Matrix();
    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private Matrix currentMatrix = new Matrix();
    private Matrix defaultMatrix = new Matrix();
    /**
     * 图片的宽高
     */
    private float imgHeight, imgWidth;
    /**
     * 初始状态
     */
    private int mode = 0;
    private float scaleY = 0;
    /**
     * 用于记录开始时候的坐标位置
     */
    private PointF startPoint = new PointF();
    /**
     * 用于记录开始时候的在整个屏幕中的Y坐标位置
     */
    private float startRawY = 0;
    /**
     * 用于记录结束时候的在整个屏幕中的Y坐标位置
     */
    private float endRawY = 510;
    private TouchEventListener touchEventListener = null;
    private BackScaleListener backScaleListener = null;
    /**
     * 逐步回弹
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case BACK_SCALE:
                    scale = (scaleY / 2 + imgHeight) / (imgHeight);// 得到缩放倍数
                    if (scaleY > 0) {
                        isBacking = true;
                        matrix.set(currentMatrix);
                        LayoutParams relativeLayout = new LayoutParams(
                                (int) (scale * imgWidth), (int) (scale * imgHeight));
                        imageView.setLayoutParams(relativeLayout);
                        matrix.postScale(scale, scale, imgWidth / 2, 0);
                        imageView.setImageMatrix(matrix);
                        scaleY = (float) (scaleY / 2 - 1);
                        mHandler.sendEmptyMessageDelayed(BACK_SCALE, 20);// 逐步回弹
                    } else {
                        scaleY = 0;
                        LayoutParams relativeLayout = new LayoutParams(
                                (int) imgWidth, (int) imgHeight);
                        imageView.setLayoutParams(relativeLayout);
                        matrix.set(defaultMatrix);
                        imageView.setImageMatrix(matrix);
                        isBacking = false;
                    }
                    if (backScaleListener != null) {
                        backScaleListener.onBackScale();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public DragScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.attrs = attrs;
        initView();
    }

    public DragScaleImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        initView();
    }

    public DragScaleImageView(Activity activity, Bitmap resBitmap, int width,
                              int height) {
        super(activity);
    }

    /**
     * 初始化图片
     */
    private void initView() {
        /* 取得屏幕分辨率大小 */
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(dm);
        displayWidth = dm.widthPixels;
        displayHeight = dm.heightPixels;

        TypedArray a = mContext.obtainStyledAttributes(attrs,
                R.styleable.DragScaleImageView);
        mImageId = a.getResourceId(R.styleable.DragScaleImageView_scale_image,
                0);
        a.recycle();
        if (null == bmp && mImageId != 0) {
            bmp = BitmapFactory.decodeResource(getResources(), mImageId);
            float scale = (float) displayWidth / (float) bmp.getWidth();// 1080/1800
            matrix.postScale(scale, scale, 0, 0);
            imgHeight = scale * bmp.getHeight();
            imgWidth = scale * bmp.getWidth();
        } else {
            imgHeight = displayWidth;
            imgWidth = displayWidth;
        }
        initImageView();
    }

    private void initImageView() {
        imageView = new ImageView(mContext);
        imageView.setImageMatrix(matrix);
        defaultMatrix.set(matrix);
        Log.w(TAG, "imgWidth :" + imgWidth);
        Log.w(TAG, "imgHeight :" + imgHeight);

        LayoutParams layoutParams = new LayoutParams(
                (int) imgWidth, (int) imgHeight);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageBitmap(bmp);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        this.addView(imageView);
    }

    /**
     * 设置ImageView的宽高
     *
     * @param width
     * @param height
     */
    public void setImageWidthAndHeight(int width, int height) {
        imgWidth = width;
        imgHeight = height;
        LayoutParams layoutParams = new LayoutParams(
                (int) imgWidth, (int) imgHeight);
        imageView.setLayoutParams(layoutParams);
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.w(TAG, "onTouchEvent :" + event.getAction());
        // 当该View放置在ScrollView里面时，会与父控件Touch事件冲突，所以touch该控件区域时，父控件不可用
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);// true表示父类的不可用;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                if (isBacking) {
                    return super.onTouchEvent(event);
                }
                int[] location = new int[2];
                imageView.getLocationInWindow(location);
                if (location[1] >= 0) {
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    startRawY = event.getRawY();
                    Log.w(TAG, "onTouchEvent startRawY:" + startRawY);
                }
                break;
            // 手指在屏幕上移动，改事件会被不断触发
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片
                if (mode == MODE_DRAG) {
//				float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                    float dy = event.getY() - startPoint.y; // 得到y轴的移动距离
                    // 在没有移动之前的位置上进行移动
                    if (dy > 0) {
                        matrix.set(currentMatrix);
                        Log.w(TAG, "onTouchEvent dy:" + dy);
                        scale = ((dy / (displayHeight - startRawY) * (displayHeight - imgHeight)) + imgHeight)
                                / imgHeight; // 得到缩放倍数，当手指移动到屏幕底部时，图片也达到屏幕底部
                        Log.w(TAG, "onTouchEvent scale:" + scale);

                        scaleY = dy;
                        LayoutParams relativeLayout = new LayoutParams(
                                (int) (scale * imgWidth), (int) (scale * imgHeight));
                        imageView.setLayoutParams(relativeLayout);
                        matrix.postScale(scale, scale, imgWidth / 2, 0);
                        imageView.setImageMatrix(matrix);
                    }

                }
                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，图片还原
                mHandler.sendEmptyMessage(BACK_SCALE);
            case MotionEvent.ACTION_POINTER_UP:
                // 当两个手指移动时，取消移动图片
                mode = 0;
                break;
        }
        // 设置的Touch监听事件
        if (touchEventListener != null) {
            touchEventListener.onTouchEvent(event);
        }
        return true;
    }

    public void setTouchEventListener(TouchEventListener touchEventListener) {
        this.touchEventListener = touchEventListener;
    }

    public void setBackScaleListener(BackScaleListener backScaleListener) {
        this.backScaleListener = backScaleListener;
    }

    /**
     * Touch事件监听
     */
    public interface TouchEventListener {
        public void onTouchEvent(MotionEvent event);
    }

    /**
     * 回弹事件监听
     */
    public interface BackScaleListener {
        public void onBackScale();
    }
}
