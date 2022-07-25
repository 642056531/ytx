package com.ytx.erp.presenter;

import android.content.Context;

import com.ytx.erp.base.BasePresenter;
import com.ytx.erp.base.ExceptionHandle;
import com.ytx.erp.base.Observer;
import com.ytx.erp.bean.BaseBean;
import com.ytx.erp.bean.LoginBean;
import com.ytx.erp.contract.LoginContract;
import com.ytx.erp.model.LoginModel;
import com.ytx.erp.utils.MyLogUtil;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author KePeiKun
 * @time 2022/7/22
 * @describe
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements  LoginContract.Presenter {

    private LoginModel mLoginModel;
    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
        mLoginModel = new LoginModel();
    }

    @Override
    public void getLoginPsw(Map<String, String> queryParams) {
        mLoginModel.getLoginPsw(new Observer<LoginBean>() {
            @Override
            public void OnSuccess(BaseBean<LoginBean> baseBean) {
                if(isViewAttached()) {
                    view.getLoginPsw(baseBean.getResult());
                }

            }

            @Override
            public void OnFail(ExceptionHandle.ResponeThrowable e) {
                MyLogUtil.e("错误信息====："+e.message);
            }

            @Override
            public void OnCompleted() {

            }

            @Override
            public void OnDisposable(Disposable d) {

            }
        }, queryParams);
    }
}
