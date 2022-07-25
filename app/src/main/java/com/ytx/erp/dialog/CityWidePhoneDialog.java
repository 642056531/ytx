package com.ytx.erp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytx.erp.R;


/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：对话框
 */
public class CityWidePhoneDialog extends Dialog {
    private Context context;
    private Boolean cancelAble = true;
    private LinearLayout llCitywidePhone;
    private TextView tvConfirm;
    private TextView tvCancel;
    String phone = "";

    public CityWidePhoneDialog(Context context, Boolean cancelAble) {
        super(context, R.style.comm_load_dialog);
        this.context = context;
        this.cancelAble = cancelAble;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_citywide_phone);
        int width = context.getResources().getDisplayMetrics().widthPixels;
        llCitywidePhone = (LinearLayout) findViewById(R.id.ll_citywide_phone);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        llCitywidePhone.setGravity(Gravity.BOTTOM);
        llCitywidePhone.getLayoutParams().width = (int) (width * 1);


    }
    /**
     * 含确认按钮弹出框
     */
    public void showCityWidePhoneDialog(android.view.View.OnClickListener posClicker) {
        show();
        // 处理修改按钮的绑定事
        tvConfirm.setOnClickListener(posClicker);
        tvCancel.setOnClickListener(posClicker);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!cancelAble)
            return false;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!cancelAble)
                return false;
        }
        return super.onKeyDown(keyCode, event);
//        return false;
    }
}

