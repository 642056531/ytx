package com.ytx.erp.base;


import com.ytx.erp.bean.BaseBean;

import io.reactivex.disposables.Disposable;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：
 */
public abstract class ObserverNoToast<T> implements io.reactivex.Observer<BaseBean<T>> {
    @Override
    public void onSubscribe(Disposable d) {
        //添加订阅关系
        OnDisposable(d);
    }

    @Override
    public void onNext(BaseBean<T> baseBean) {
        if (baseBean != null) {
            if (baseBean.getCode() == 200) {
                OnSuccess(baseBean);
            } else {
                Throwable throwable = new Throwable(baseBean.getMessage());
                onError(throwable);
                if(baseBean.getMessage()!=null && !baseBean.getMessage().equals("")){
//                    MyToast.show(BaseApplication.getInstance(), baseBean.getMessage());
                    OnGetFail(baseBean);
                }

            }
        }

    }

    @Override
    public void onError(Throwable e) {
        //自定义异常的传递
        OnFail(ExceptionHandle.handleException(e));
    }

    @Override
    public void onComplete() {
        OnCompleted();
    }

    public abstract void OnSuccess(BaseBean<T> baseBean);

    public abstract void OnFail(ExceptionHandle.ResponeThrowable e);

    public abstract void OnGetFail(BaseBean<T> baseBean);

    public abstract void OnCompleted();

    public abstract void OnDisposable(Disposable d);
}