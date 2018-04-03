package com.example.administrator.imagebarnner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.imagebarnner.view.ImageBannerFrameLayout;
import com.example.administrator.imagebarnner.view.ImageBannerViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageBannerFrameLayout.FrameLayoutItemClickListener {
    private ImageBannerFrameLayout mImageGroup;
    private int[] ids = new int[]{
            R.mipmap.banner,
            R.mipmap.banner1,
            R.mipmap.banner2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        C.WIDTH = dm.widthPixels;

        mImageGroup = (ImageBannerFrameLayout) findViewById(R.id.image_group);

        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[i]);
            list.add(bitmap);
        }
        mImageGroup.addBitmaps(list);

        mImageGroup.setItemClickListener(this);

    }

    @Override
    public void clickImageIndex(int pos) {
        Toast.makeText(getApplicationContext(), "pos=" + pos, Toast.LENGTH_SHORT).show();
    }
}
