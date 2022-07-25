package com.ytx.erp.base;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：
 */
public class BasePresenter<V> {

    public V view;

    //加载View,建立连接
    public void addView(V v) {
        this.view = v;
    }

    //断开连接
    public void detattch() {
        if (view != null) {
            view = null;
        }
    }

    /**
     * View是否绑定
     *
     * @return
     */
    public boolean isViewAttached() {
        return view != null;
    }
}
