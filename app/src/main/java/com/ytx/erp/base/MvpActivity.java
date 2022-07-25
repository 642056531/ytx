package com.ytx.erp.base;


import android.os.Bundle;

import androidx.annotation.Nullable;


/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：S
 */

public abstract class MvpActivity<p extends BasePresenter> extends BaseActivity {
    public p presener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presener = initPresener();
        //把所有继承此类的Activity都绑定到这里了，这样View就和Present联系起来了。
        presener.addView(this);
    }

    protected abstract p initPresener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presener.detattch();
        //View消除时取消订阅关系
        SubscriptionManager.getInstance().cancelall();
    }
}

