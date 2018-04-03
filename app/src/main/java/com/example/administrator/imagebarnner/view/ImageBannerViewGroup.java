package com.example.administrator.imagebarnner.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/3/28.
 */

public class ImageBannerViewGroup extends ViewGroup {
    private int childrenCount;
    private int childrenWidth, height;

    private int x;
    private int index = 0;//每张图片的位置

    private Scroller mScroll;
    private ImageBannerListener mListener;
    private ImageBannerVGListener mVGListener;

    private boolean isClick;//true--点击事件

    public interface ImageBannerListener {
        void clickImageIndex(int pos);
    }

    public ImageBannerListener getImageBannerListener() {
        return mListener;
    }

    public void setImageBannerListener(ImageBannerListener mListener) {
        this.mListener = mListener;
    }

    public ImageBannerVGListener getVGListener() {
        return mVGListener;
    }

    public void setVGListener(ImageBannerVGListener mVGListener) {
        this.mVGListener = mVGListener;
    }

    //自动轮播
    private boolean isAuto;
    private Timer mTimer = new Timer();

    private TimerTask mTask;

    private Handler mAutoHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0: //自动轮播
                    if (++index >= childrenCount) {//最后一张重新滑动
                        index = 0;
                    }
                    scrollTo(childrenWidth * index, 0);
                    if (mVGListener != null) {
                        mVGListener.onSelect(index);
                    }
                    break;
            }
            return false;
        }
    });

    private void startAuto() {
        isAuto = true;
    }

    private void stopAuto() {
        isAuto = false;
    }


    /**
     * Timer TimerTask Handler
     *
     * @param context
     */

    public ImageBannerViewGroup(Context context) {
        super(context);
        initView();
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageBannerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mScroll = new Scroller(getContext());

        mTask = new TimerTask() {
            @Override
            public void run() {
                if (isAuto) {
                    mAutoHandler.sendEmptyMessage(0);
                }
            }
        };
        mTimer.schedule(mTask, 200, 3000);
        startAuto();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroll.computeScrollOffset()) {
            scrollTo(mScroll.getCurrX(), 0);
            invalidate();
        }
    }

    /**
     * 实现方法 ： 测量 布局 绘制
     * <p>
     * 测量 onMeasure
     * 布局 onLayout
     * 绘制 针对于容器的绘制就是容器内子控件绘制过程
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 测量viewGroup宽高 必须先测子视图的宽高 才能知道 ViewGroup宽高
         */
        // 1.求出子视图个数
        childrenCount = getChildCount();
        if (0 == childrenCount) {
            setMeasuredDimension(0, 0);
        } else {
            //2.测量子视图宽高
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //3.求出viewGroup宽高
            View view = getChildAt(0);
            childrenWidth = view.getMeasuredWidth();

            //高度等于子视图高度
            height = view.getMeasuredHeight();
            int width = childrenWidth * childrenCount;
            setMeasuredDimension(width, height);
        }
    }

    /**
     * 必须实现onLayout
     *
     * @param changed 当viewGroup布局位置发生改变时为true
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int leftMargin = 0;
        for (int i = 0; i < childrenCount; i++) {
            View v = getChildAt(i);
            v.layout(leftMargin, t, leftMargin + childrenWidth, height);
            leftMargin += childrenWidth;
        }
    }

    /**
     * 事件传递过程 ：
     * 调用 容器拦截方法 onInterceptTouchEvent
     */
    /**
     * @param ev
     * @return true:拦截; false ：不拦截向下传递该事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroll.isFinished()) {
                    mScroll.abortAnimation();
                }
                x = (int) event.getX();
                isClick = true;
                stopAuto();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int distance = moveX - x;

                if (Math.abs(distance) > 20) {
                    isClick = false;
                }
                Log.d("ACTION_MOVE", "moveX =" + moveX + ";x=" + x);
                if ((index == 0 && distance > 0) || (index == childrenCount - 1 && distance < 0)) {
                    distance = 0;
                }
                scrollBy(-distance, 0);
                x = moveX;


//                isClick = false;
                break;
            case MotionEvent.ACTION_UP:

                int scrollX = getScrollX();
                index = (scrollX + childrenWidth / 2) / childrenWidth;
                if (index < 0) {
                    index = 0;
                } else if (index > childrenCount - 1) {
                    index = childrenCount - 1;
                }
                if (isClick) {
                    if (mListener != null) {
                        mListener.clickImageIndex(index);
                    }
                } else {

                    int dx = index * childrenWidth - scrollX;

//                scrollTo(index * childrenWidth, 0);
                    mScroll.startScroll(scrollX, 0, dx, 0);
                    postInvalidate();
                    if (mVGListener != null) {
                        mVGListener.onSelect(index);
                    }
                }
                startAuto();
                break;
            default:
                break;
        }
        return true; //viewGroup告诉父view已处理了该事件
    }


    public interface ImageBannerVGListener {
        void onSelect(int index);
    }

    /**
     * 用2种方式 实现 轮播图的手动绘制
     * 1.scrollTo scrollBy
     * 2.Scroller对象
     *
     */
}
