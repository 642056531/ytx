package com.ytx.erp.base;



import android.widget.Toast;

import com.ytx.erp.bean.BaseBean;
import com.ytx.erp.network.BaseApplication;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public abstract class Observer<T> implements io.reactivex.Observer<BaseBean<T>> {
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
                if(baseBean.getMessage()!=null && !baseBean.getMessage().equals(""))
                    if(!baseBean.getMessage().equals("找不到用户信息")){
                        Toast.makeText(BaseApplication.getInstance(), baseBean.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                Throwable throwable = new Throwable(baseBean.getMessage());
                onError(throwable);
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

    public abstract void OnCompleted();

    public abstract void OnDisposable(Disposable d);
}
