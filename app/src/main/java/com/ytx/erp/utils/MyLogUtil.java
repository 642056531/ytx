package com.ytx.erp.utils;

import android.util.Log;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：打印
 */
public class MyLogUtil {
    private static final String TAG = "YangLogUtil";

    /**类名*/
    static String className;
    /**方法名*/
    static String methodName;
    /**行数*/
    static int lineNumber;
    static boolean isDebug = false;


    public static void setDebug(boolean isDebugs){
        isDebug = isDebugs;
    }
    /**
     * 判断是否可以调试
     * @return
     */
    public static boolean isDebuggable() {
        return isDebug;
    }


    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
//        buffer.append(methodName);
//        buffer.append("(").append(className).append(":").append(lineNumber).append(")\nYangLogUtil:");
//        buffer.append("\n");
        buffer.append(log);
        return buffer.toString();
    }

    /**
     * 获取文件名、方法名、所在行数
     * @param sElements
     */
    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longe(className, message);
    }

    public static void e(String tag,String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longe(tag, message);
    }

    public static void longe(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0) {
            return;
        }
        int segmentSize = 3*1024;
        long length = msg.length();
        Log.e(tag,"----------------------------------------------------------------------------------------------------");
        /**长度小于等于限制直接打印*/
//        if (length <= segmentSize ) {
//            Log.e(tag,msg);
//        }else {
//            SanaJsonFormat.formatLog(tag,msg);
//        }
        SanaJsonFormat.formatLog(tag,msg);
        Log.e(tag,"----------------------------------------------------------------------------------------------------");
    }


    public static void i(String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longi(className,message);
    }

    public static void i(String tag,String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longi(tag,message);
    }

    public static void longi(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.i(tag, createLog(msg));
        }else {
            boolean isFirst = true;
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                if(isFirst){
                    isFirst = false;
                    Log.i(tag,createLog(logContent));
                }else{
                    Log.i(tag,logContent);
                }
            }
            Log.i(tag,msg);// 打印剩余日志
        }
    }


    public static void d(String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longd(className, message);
    }

    public static void d(String tag,String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longd(tag,message);
    }

    public static void longd(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.d(tag, createLog(msg));
        }else {
            boolean isFirst = true;
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                if(isFirst){
                    isFirst = false;
                    Log.d(tag,createLog(logContent));
                }else{
                    Log.d(tag,logContent);
                }
            }
            Log.d(tag,msg);// 打印剩余日志
        }
    }

    public static void v(String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longv(className, message);
    }

    public static void v(String tag,String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longv(tag, message);
    }

    public static void longv(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.v(tag, createLog(msg));
        }else {
            boolean isFirst = true;
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                if(isFirst){
                    isFirst = false;
                    Log.v(tag,createLog(logContent));
                }else{
                    Log.v(tag,logContent);
                }
            }
            Log.v(tag,msg);// 打印剩余日志
        }
    }

    public static void w(String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longw(className, message);
    }

    public static void w(String tag,String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        longw(tag, message);
    }

    public static void longw(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.w(tag, createLog(msg));
        }else {
            boolean isFirst = true;
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                if(isFirst){
                    isFirst = false;
                    Log.w(tag,createLog(logContent));
                }else{
                    Log.w(tag,logContent);
                }
            }
            Log.w(tag,msg);// 打印剩余日志
        }
    }
}
