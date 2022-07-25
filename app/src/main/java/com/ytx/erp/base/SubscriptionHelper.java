package com.ytx.erp.base;


import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public interface SubscriptionHelper<T> {
    void add(Disposable subscription);

    void cancel(Disposable t);

    void cancelall();
}