package com.ytx.erp.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ytx.erp.R;
import com.ytx.erp.base.MvpActivity;
import com.ytx.erp.bean.LoginBean;
import com.ytx.erp.contract.LoginContract;
import com.ytx.erp.dialog.CityWidePhoneDialog;
import com.ytx.erp.presenter.LoginPresenter;
import com.ytx.erp.utils.CommUtils;
import com.ytx.erp.view.MyToast;

import java.util.HashMap;

/**
 * @author KePeiKun
 * @time 2022/7/22
 * @describe 登录页
 */
public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener, TextWatcher {

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvLogin;
    private TextView tvCodeLogin;
    private TextView tvForget;
    private ImageView tvLoginWechat;
    private ImageView ivLoginUserAgreement;
    private TextView ivLoginAgreementText;
    private LinearLayout llLoginUserAgreement;

    /*是否同意协议*/
    private boolean isAgree = false;


    @Override
    protected int layout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏为白色 图标显示深色

//        setLeftIco(R.mipmap.icon_back);
//        setTitleText("我的地盘");
//        setLeftIcoListening(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyToast.show(LoginActivity.this, "aaaa");
//            }
//        });
//        getWindow().setStatusBarColor(getResources().getColor(R.color.color_2c92f9));
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏为白色 图标显示深色

        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        tvCodeLogin = (TextView) findViewById(R.id.code_login);
        tvForget = (TextView) findViewById(R.id.forget);
        tvLoginWechat = (ImageView) findViewById(R.id.login_wechat);
        ivLoginUserAgreement = (ImageView) findViewById(R.id.iv_login_user_agreement);
        ivLoginAgreementText = (TextView) findViewById(R.id.iv_login_agreement_text);
        llLoginUserAgreement = (LinearLayout) findViewById(R.id.ll_login_user_agreement);
        tvLogin = (TextView) findViewById(R.id.tv_login);

    }

    @Override
    protected void initData() {
        tvCodeLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        ivLoginUserAgreement.setOnClickListener(this);
        ivLoginAgreementText.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvLoginWechat.setOnClickListener(this);
//        showCityRankDialog();
    }

    CityWidePhoneDialog cityWidePhoneDialog;

    public void showCityRankDialog() {
        cityWidePhoneDialog = new CityWidePhoneDialog(this, true);
        cityWidePhoneDialog.setCanceledOnTouchOutside(false);
        Window window = cityWidePhoneDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        //确定
        cityWidePhoneDialog.showCityWidePhoneDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tv_confirm) {//拨打电话
                    CommUtils.callPhone(LoginActivity.this, "123456");
                } else if (v.getId() == R.id.tv_cancel) {//取消

                }
                cityWidePhoneDialog.dismiss();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //<editor-fold desc="没有选择动效">
    private void noSelectAnimal(){
        AnimatorSet set = new AnimatorSet();
        Animator animator1 = ObjectAnimator.ofFloat(llLoginUserAgreement, "translationX", 0, 50);
        Animator animator2 = ObjectAnimator.ofFloat(llLoginUserAgreement, "translationX", 50, -50);
        Animator animator3 = ObjectAnimator.ofFloat(llLoginUserAgreement, "translationX", -50, 20);
        Animator animator4 = ObjectAnimator.ofFloat(llLoginUserAgreement, "translationX", 20, 0);
        set.play(animator1).before(animator2);
        set.play(animator2).before(animator3);
        set.play(animator3).before(animator4);
        set.play(animator4);
        set.setDuration(100).start();
    }
    //</editor-fold>

    private boolean isCanLogin(){
        if(!isAgree){
            noSelectAnimal();
            MyToast.show(this, getResources().getString(R.string.login_to_agree_tip));
        }
        return isAgree;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.code_login) {//短信验证码登录

        }else if (view.getId() == R.id.forget) {//忘记密码

        }else if (view.getId() == R.id.iv_login_user_agreement) {
            /*点击同意协议*/
            isAgree = !isAgree;
            if (isAgree) {
                ivLoginUserAgreement.setImageResource(R.mipmap.icon_login_agree_select);
            } else {
                ivLoginUserAgreement.setImageResource(R.mipmap.icon_login_agree_unselect);
            }
        } else if (view.getId() == R.id.iv_login_agreement_text) {//同意的文字
            /*点击同意协议*/
            isAgree = !isAgree;
            if (isAgree) {
                ivLoginUserAgreement.setImageResource(R.mipmap.icon_login_agree_select);
            } else {
                ivLoginUserAgreement.setImageResource(R.mipmap.icon_login_agree_unselect);
            }
        }else if(view.getId() == R.id.tv_login){//登录
            if(TextUtils.isEmpty(etUsername.getText().toString())){
                MyToast.show(this, "用户名不能为空");
                return;
            }else if(TextUtils.isEmpty(etPassword.getText().toString())){
                MyToast.show(this, "密码不能为空");
                return;
            }else if(isCanLogin()){
//                HashMap<String, String> queryParams = new HashMap<>();
//                queryParams.put("username", etUsername.getText().toString().trim());
//                queryParams.put("password", etPassword.getText().toString().trim());
//                presener.getLoginPsw(queryParams);
            }
        }else if(view.getId() == R.id.login_wechat){//微信登录

        }
    }

    @Override
    protected LoginPresenter initPresener() {
        return new LoginPresenter(this);
    }

    @Override
    public void getLoginPsw(LoginBean loginBean) {

    }
}
