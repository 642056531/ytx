package com.ytx.erp.model;

import com.ytx.erp.api.PortApi;
import com.ytx.erp.bean.BaseBean;
import com.ytx.erp.bean.LoginBean;
import com.ytx.erp.contract.LoginContract;
import com.ytx.erp.retrofit.RetrofitManager;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author KePeiKun
 * @time 2022/7/22
 * @describe
 */
public class LoginModel implements LoginContract.Model {

    /*账号密码登录*/
    @Override
    public void getLoginPsw(Observer<BaseBean<LoginBean>> observer, Map<String, String> queryParams) {
        Observable<BaseBean<LoginBean>> gSxin = RetrofitManager
                .getSingleton()
                .Apiservice(PortApi.class)
                .getLoginPsw(queryParams);
        gSxin.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
