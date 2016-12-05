package com.smart.smartir.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.smart.smartir.activity.MainActivity;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    BaseActivity mTopActivity;
    private List<BaseActivity> activitys = new LinkedList<BaseActivity>();
    private long mPreClickTime;

    public BaseActivity getTopActivity() {
        return mTopActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        mTopActivity = this;
        activitys.add(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        activitys.remove(this);
        super.onDestroy();
    }

    protected abstract void initView();

    protected void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void initData() {

    }

    protected void initListener() {

    }

    /**
     * 完全退出
     */
    public void exit() {
        for (BaseActivity baseActivity : activitys) {
            baseActivity.finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {// 主页
            if (System.currentTimeMillis() - mPreClickTime > 2000) {// 两次点击的间隔大于2s中
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                mPreClickTime = System.currentTimeMillis();
                return;
            } else {
                // 完全退出
                exit();
            }
        } else {
            super.onBackPressed();// finish
        }
    }
}
