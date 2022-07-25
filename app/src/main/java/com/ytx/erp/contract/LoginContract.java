package com.ytx.erp.contract;

import com.ytx.erp.bean.BaseBean;
import com.ytx.erp.bean.LoginBean;

import java.util.Map;

/**
 * @author KePeiKun
 * @time 2022/7/22
 * @describe
 */
public interface LoginContract {
    interface Model {
        //账号密码登陆
        void getLoginPsw(io.reactivex.Observer<BaseBean<LoginBean>> observer, Map<String, String> queryParams);
    }

    interface View {
        void getLoginPsw(LoginBean loginBean);
    }

    interface Presenter {
        void getLoginPsw(Map<String, String> queryParams);
    }
}
