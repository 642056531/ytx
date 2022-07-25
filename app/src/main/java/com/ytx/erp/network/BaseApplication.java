package com.ytx.erp.network;

import android.app.Application;
import android.os.Handler;

import com.ytx.erp.db.SharedPreferencesUtil;
import com.ytx.erp.mgr.MyActivityManager;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：application类
 */

public class BaseApplication extends Application {

    private static BaseApplication baseApplication = null;
    private Handler mHandler;

    public Handler getmHandler() {
        return mHandler;
    }

    //判断是否在前后台
    private int count = 0;
    //第一次不对程序进行处理里
    private boolean isFirst = true;
    private boolean isBack = false;
    private long time = -2;

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }
    //    @Override
    //    protected void attachBaseContext(Context base) {
    //        super.attachBaseContext(base);
    //        //you must install multiDex whatever tinker is installed!
    //        MultiDex.install(base);
    //    }

    @Override
    public void onCreate() {
        super.onCreate();
        //        UMConfigure.init(this,"5b04d7b58f4a9d6d900001c5"
        //                ,"umeng", UMConfigure.DEVICE_TYPE_PHONE,"");//友盟初始化
        baseApplication = this;
        init();

    }

    /**
     * 本地工具类初始化
     */
    private void init() {
//        ActivityMgr.init(this);
        //将当前的Activity添加到栈中
        //        ToastUtils.init(this);
        SharedPreferencesUtil.getInstance(this);
        //        JPushInterface.setDebugMode(true);// 设置为Debug 模式；
        //        // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        //        JPushInterface.init(getApplicationContext());
    }

    /**
     * 获取BaseApplication
     *
     * @return
     */
    //    public static BaseApplication getInstance() {
    //
    //        return baseApplication;
    //    }
    public static BaseApplication getInstance() {
        if (null == baseApplication) {
            baseApplication = new BaseApplication();
        }
        return baseApplication;
    }

}
