package com.example.administrator.imagebarnner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.imagebarnner.C;
import com.example.administrator.imagebarnner.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class ImageBannerFrameLayout extends FrameLayout implements ImageBannerViewGroup.ImageBannerVGListener, ImageBannerViewGroup.ImageBannerListener {
    private ImageBannerViewGroup imageBannerViewGroup;
    private LinearLayout linearLayout;

    private FrameLayoutItemClickListener mListener;

    public FrameLayoutItemClickListener getItemClickListener() {
        return mListener;
    }

    public void setItemClickListener(FrameLayoutItemClickListener mListener) {
        this.mListener = mListener;
    }

    public ImageBannerFrameLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public ImageBannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageBannerFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        initImageBannerViewGroup();
        initDotLinearLayout();
    }

    public void addBitmaps(List<Bitmap> list) {
        for (int i = 0; i < list.size(); i++) {
            Bitmap bitmap = list.get(i);
            addBitmapToImageBannerViewGroup(bitmap);
            addDotToLinearLayout();
        }
    }

    private void addDotToLinearLayout() {
        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        iv.setLayoutParams(lp);
        iv.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(iv);

    }

    private void addBitmapToImageBannerViewGroup(Bitmap bitmap) {
        ImageView iv = new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new ViewGroup.LayoutParams(C.WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        iv.setImageBitmap(bitmap);
        imageBannerViewGroup.addView(iv);
    }


    /**
     * 初始化 自定义轮播功能类
     */
    private void initImageBannerViewGroup() {
        imageBannerViewGroup = new ImageBannerViewGroup(getContext());
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageBannerViewGroup.setLayoutParams(lp);
        imageBannerViewGroup.setVGListener(this);
        imageBannerViewGroup.setImageBannerListener(this);
        addView(imageBannerViewGroup);
    }

    /**
     * 初始化底部原点布局
     */
    private void initDotLinearLayout() {
        linearLayout = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 40);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        linearLayout.setBackgroundColor(Color.RED);

        addView(linearLayout);

        LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            linearLayout.setAlpha(0.5f);
        } else {
            linearLayout.getBackground().setAlpha(127);
        }
    }

    @Override
    public void onSelect(int index) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if (i == index) {
                iv.setImageResource(R.drawable.dot_select);
            } else {
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }

    @Override
    public void clickImageIndex(int pos) {
        if (mListener != null) {
            mListener.clickImageIndex(pos);
        }
    }

    public interface FrameLayoutItemClickListener {
        void clickImageIndex(int pos);
    }
}
