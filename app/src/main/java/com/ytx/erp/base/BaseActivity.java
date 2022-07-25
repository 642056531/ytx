package com.ytx.erp.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.ytx.erp.R;
import com.ytx.erp.mgr.MyActivityManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 是否打开eventbus
     */
    private boolean isEventBus = false;
    private boolean isGetClipBoard = true;
    private boolean isShowFloat = true;

    private RelativeLayout titleBar;
    private RelativeLayout rlLeft;
    private TextView titleLeft;
    private ImageView imgLeftIco;
    private TextView titleText;
    private RelativeLayout rlRight;
    private TextView titleRight;
    private ImageView imgRightIco;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //将当前的Activity添加到栈中
        MyActivityManager.getManager().addActivity(this);
        setContentView(layout());
        /*状态栏跟界面颜色一样*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏为白色 图标显示深色
        }
        titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        rlLeft = (RelativeLayout) findViewById(R.id.rl_left);
        titleLeft = (TextView) findViewById(R.id.title_left);
        imgLeftIco = (ImageView) findViewById(R.id.img_leftIco);
        titleText = (TextView) findViewById(R.id.title_text);
        rlRight = (RelativeLayout) findViewById(R.id.rl_right);
        titleRight = (TextView) findViewById(R.id.title_right);
        imgRightIco = (ImageView) findViewById(R.id.img_rightIco);

        initView();
        initData();


    }


    /**
     * 获取布局Id
     */
    protected abstract int layout();

    /**
     * 设置布局文件，控件初始化
     */
    protected abstract void initView();

    /**
     * 初始化相关数据
     */
    protected abstract void initData();



    /**
     * 用于设置标题栏文字
     * */
    public void setTitleText(String sTitleText){
        if(!TextUtils.isEmpty(sTitleText)){
            titleText.setText(sTitleText);
        }
    }

    /**
     * 用于设置标题栏左边要显示的图片
     * */
    public void setLeftIco(int resId){
        imgLeftIco.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        imgLeftIco.setImageResource(resId);
    }
    /**
     * 用于设置标题栏左边要显示的文字
     * */
    public void setLeftTitleText(String sTitleText){
        if(!TextUtils.isEmpty(sTitleText)){
            titleLeft.setText(sTitleText);
        }
    }
    /**
     * 用于设置标题栏右边要显示的文字
     * */
    public void setRightTitleText(String sTitleText){
        if(!TextUtils.isEmpty(sTitleText)){
            titleRight.setText(sTitleText);
        }
    }

    /**
     * 用于设置标题栏右边要显示的图片
     * */
    public void setRightIco(int resId){
        imgRightIco.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        imgRightIco.setImageResource(resId);
    }

    /**
     * 用于设置标题栏左边图片的单击事件
     * */
    public void setLeftIcoListening(View.OnClickListener listener){
        if(imgLeftIco.getVisibility() == View.VISIBLE){
            rlLeft.setOnClickListener(listener);
        }
    }

    /**
     * 用于设置标题栏右边图片的单击事件
     * */
    public void setRightIcoListening(View.OnClickListener listener){
        if(imgRightIco.getVisibility() == View.VISIBLE){
            rlRight.setOnClickListener(listener);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isEventBus && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isEventBus) {
            EventBus.getDefault().unregister(this);
        }
        //移除当前栈中的Activity
        MyActivityManager.getManager().finishActivity(this);
    }
}
