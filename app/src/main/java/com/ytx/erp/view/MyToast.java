package com.ytx.erp.view;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.ytx.erp.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：自定义Toast
 */
public class MyToast {
    public static Toast toast = null;
    /**
     * 检查通知栏权限有没有开启
     */
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager manager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
            if (manager != null) {
                return manager.areNotificationsEnabled();
            } else {
                return false;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return (Integer) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == 0;
            } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | RuntimeException | ClassNotFoundException ignored) {
                return true;
            }
        } else {
            return true;
        }
    }

    private static Object iNotificationManagerObj;
    /**
     * 强制显示系统Toast
     */
    @SuppressLint("SoonBlockedPrivateApi")
    private static void showSystemToast(Toast toast) {
        try {
            Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
            getServiceMethod.setAccessible(true);
            //hook INotificationManager
            if (iNotificationManagerObj == null) {
                iNotificationManagerObj = getServiceMethod.invoke(null);

                Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
                Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //强制使用系统Toast
                        if ("enqueueToast".equals(method.getName())
                                || "enqueueToastEx".equals(method.getName())) {  //华为p20 pro上为enqueueToastEx
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    }
                });
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(null, iNotificationManagerProxy);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void show(final Context context, final String info){

        //如果没有通知权限 就强制Toast弹框
//        if (!isNotificationEnabled(context)) {
////            MyLogUtil.e("没有toast权限");
//            MyToastDialog.showMyToastDialog(context,info);
//            return;
//        }
//        MyLogUtil.e("有toast权限");

        //下面走正常弹框
        if (toast!= null) {
            toast.setDuration(Toast.LENGTH_SHORT);
            TextView textView=toast.getView().findViewById(R.id.tv_dlg_my_toast_text);
            textView.setText(info);
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view =inflater.inflate(R.layout.dlg_my_toast,null);
            TextView textView=view.findViewById(R.id.tv_dlg_my_toast_text);
            textView.setText(info);
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(view);
        }
        toast.cancel();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        },100);
    }


    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }


}
