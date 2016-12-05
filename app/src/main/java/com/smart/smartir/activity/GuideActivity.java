package com.smart.smartir.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smart.smartir.R;
import com.smart.smartir.utils.Constants;
import com.smart.smartir.utils.SPUtils;
import com.smart.smartir.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager viewpager_guide;
    private int[] pages_id = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private List<ImageView> mIV_datas = new ArrayList<ImageView>();
    private LinearLayout ll_gray_points;
    private ImageView view_redpoint;
    private int mPointDis;
    private Button button_guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        viewpager_guide = (ViewPager) findViewById(R.id.viewpager_guide);
        ll_gray_points = (LinearLayout) findViewById(R.id.ll_guid_graypoints);
        view_redpoint = (ImageView) findViewById(R.id.view_redpoint);
        button_guide = (Button) findViewById(R.id.button_guide);
    }

    private void initData() {
        for (int i  = 0; i < pages_id.length ; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(pages_id[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            mIV_datas.add(iv);

            //加点

            //设备像素到普通像素的转换
            int dis = UIUtils.dip2Px(10);
            View v = new View(this);
            v.setBackgroundResource(R.drawable.v_point_gray);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dis, dis);
            if (i != 0) {
                //不是第一个点
                //设置左边距10
                lp.leftMargin = dis;
            }
            v.setLayoutParams(lp);
            //添加到容器中
            ll_gray_points.addView(v);
        }

        MyPagerAdapter mAdapter = new MyPagerAdapter();
        viewpager_guide.setAdapter(mAdapter);
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mIV_datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mIV_datas.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initEvent() {
        //添加体验的事件
        button_guide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 修改状态值
                SPUtils.putBoolean(getApplicationContext(), Constants.ISSETUPFINISH, true);
                //进入主界面
                Intent home = new Intent(GuideActivity.this,LoginActivity.class);
                startActivity(home);
                finish();
            }
        });

        //监听布局完成
        view_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {



            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                //PrintLog.print("布局变化");

                mPointDis = ll_gray_points.getChildAt(1).getLeft() - ll_gray_points.getChildAt(0).getLeft();
                //取消观察
                view_redpoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        // 给ViewPager添加滑动事件
        viewpager_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 页面选择完成的调用
                if (position == mIV_datas.size() - 1) {
                    //显示按钮
                    button_guide.setVisibility(View.VISIBLE);
                } else {
                    //隐藏按钮
                    button_guide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // 页面滑动的调用
                //position 页面的当前位置
                //positionOffset 比例值
                //positionOffsetPixels 偏移的像素

                //确定红点的位置
                FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) view_redpoint.getLayoutParams();
                layoutParams.leftMargin = Math.round(mPointDis * (position + positionOffset));
                view_redpoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态改变的回调
                //ViewPager.SCROLL_STATE_DRAGGING
                //ViewPager.SCROLL_STATE_IDLE
                //ViewPager.SCROLL_STATE_SETTLING
                //System.out.println("ViewPager.SCROLL_STATE_DRAGGING" + (state == ViewPager.SCROLL_STATE_DRAGGING));
                //System.out.println("ViewPager.SCROLL_STATE_IDLE" + (state == ViewPager.SCROLL_STATE_IDLE));
                //System.out.println("ViewPager.SCROLL_STATE_SETTLING" + (state == ViewPager.SCROLL_STATE_SETTLING));
            }
        });

    }
}
