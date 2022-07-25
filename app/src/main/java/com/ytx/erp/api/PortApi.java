package com.ytx.erp.api;

import com.ytx.erp.bean.BaseBean;
import com.ytx.erp.bean.LoginBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * @author KePeiKun
 * @time 2022/7/22
 * @describe
 */
public interface PortApi {

    /*账号密码登录*/
    @POST("sso/login")
    Observable<BaseBean<LoginBean>> getLoginPsw(@QueryMap Map<String, String> queryParams);
}
