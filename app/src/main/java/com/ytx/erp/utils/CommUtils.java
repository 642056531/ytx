package com.ytx.erp.utils;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：工具类
 */

public class CommUtils {

    /**
     * 判断点击事件是否过快（1秒内过快的多次点击）
     *
     * @return true:过快点击；false：正常点击
     */
    // 最后的点击时间
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        if (Math.abs(System.currentTimeMillis() - lastClickTime) < 1000) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        return false;
    }

    /**
     * 字符串是否为空
     *
     * @param content
     * @return
     */
    public static boolean isStringEmpty(String content) {
        if (null == content)
            return true;
        if (content.length() == 0)
            return true;
        if (content.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 判断集合对象是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isCollectionEmpty(Collection<?> collection) {
        return !(collection != null && collection.size() > 0);
    }

    /**
     * 判断手机号码
     */
    public static boolean isMobileNO(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }
        String expression = "^[1][3456789][0-9]{9}$";
//		String expression = "^1[0-9]{10}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context context 对象
     * @param pxValue dp 值
     * @return int 转换后的值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id) {
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较真实完整的判断身份证号码的工具
     *
     * @param IdCard 用户输入的身份证号码
     * @return [true符合规范, false不符合规范]
     */
    public static boolean isRealIDCard(String IdCard) {
        if (IdCard != null) {
            int correct = new IdCardUtil(IdCard).isCorrect();
            if (0 == correct) {// 符合规范
                return true;
            }
        }
        return false;
    }

    /**
     * 去除多余的0
     *
     * @param number
     * @return
     */
    public static String getPrettyNumber(String number) {
        String plainString = BigDecimal.valueOf(Double.parseDouble(number))
                .stripTrailingZeros().toPlainString();
        if (plainString.equals("0.0")) {
            plainString = "0";
        }
        return plainString;
    }

    /**
     * 获取当前系统时间(年/月/日)
     */
    public static String NowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取当前系统时间(时/分/秒)
     */
    public static String NowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取当前系统时间
     */
    public static String getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取当前系统时间戳
     */
    public static String getTime() {
        long time = System.currentTimeMillis();//获取系统时间的13位的时间戳(/100,取10位的时间戳)
        String str = String.valueOf(time);
        return str;
    }

    /**
     * 设备id号
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 设备类型
     */
    public static String getDeviceType() {
        String deviceType = android.os.Build.MODEL;
        return deviceType;
    }

    /**
     * 设备设备版本号
     */
    public static int getVersionNumber() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        return currentapiVersion;
    }

    /**
     * 操作系统版本
     */
    public static String getOperatingSystemVersion() {
        String operatingSystemVersion = android.os.Build.VERSION.RELEASE;
        return operatingSystemVersion;
    }

    /**
     * 获取屏幕分辨率
     */
    public static String getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        int mScreenHeigh = dm.heightPixels;
        return mScreenHeigh + "*" + mScreenWidth + "像素";
    }


    /**
     * 获取版本号 * @param context 上下文 * @return 版本号
     */
    public static String getLocalVersion(Context ctx) {
        String localVersion = "0";
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 对字符串处理:将指定位置到指定位置的字符以星号代替
     *
     * @param content 传入的字符串
     * @param begin   开始位置
     * @param end     结束位置
     * @return
     */
    public static String getStarString(String content, int begin, int end) {

        if (begin >= content.length() || begin < 0) {
            return content;
        }
        if (end >= content.length() || end < 0) {
            return content;
        }
        if (begin >= end) {
            return content;
        }
        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

    }

    /**
     * 保留姓名的第一个字，其他用星号代替（张**）
     */
    public static String userNameReplaceWithStar(String userName) {
        //保留姓氏
        char str2 = userName.charAt(0);
        //截取名字
        String str1 = userName.substring(1);
        //用正则表达式替换(包括汉字，数字，大小写字母)
        str1 = str1.replaceAll("[^x00-xff]|\\w", "*");
        //输出替换后的名字
        return str2 + str1;
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    public static String getAPNType(Context context) {
        //结果返回值
        String netType = "0";
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = "WiFi";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "4G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "3G";
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "2G";
            } else {
                netType = "2G";
            }
        }
        return netType;
    }

    /**
     * 复制
     */
    public static void Copy(Context context, String str) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    /**
     * map转json
     */
    public static String mapToJson(Map<String, String> map) {
        Set<String> keys = map.keySet();
        String key = "";
        String value = "";
        StringBuffer jsonBuffer = new StringBuffer();
        jsonBuffer.append("{");
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            key = (String) it.next();
            value = map.get(key);
            jsonBuffer.append(key + ":" + "\"" + value + "\"");
            if (it.hasNext()) {
                jsonBuffer.append(",");
            }
        }
        jsonBuffer.append("}");
        return jsonBuffer.toString();
    }

    /**
     * 以万为单位保留小数点后2位
     */
    public static String intChange2Str(String number) {
        DecimalFormat df = new DecimalFormat("0.00");
        String str = "";
        Double money = Double.valueOf(number).doubleValue();
        if (CommUtils.isStringEmpty(number)) {
            str = "";
        } else if ("0".equals(number) || "0.0".equals(number) || "0.00".equals(number)) {
            str = "0.00";
        } else if (money <= 0) {
            str = "";
        } else if (money < 10000) {
            str = df.format(money);
        } else {
            double num = money / 10000;//1.将数字转换成以万为单位的数字
            String db = df.format(num);
//            BigDecimal b = new BigDecimal(num);
//            double f1 = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后2位;
            str = db + "万";
        }
        return str;
    }

    /**
     * 给数字每三位加一个逗号
     */
    public static String formatTosepara(Double data) {
        DecimalFormat df = null;
        if (data > 1000) {
            df = new DecimalFormat("#,###.00");
        } else {
            df = new DecimalFormat("0.00");
        }
        return df.format(data);
    }
    /**
     * 取消小数后面的0
     * @param num
     * @return
     */
    private String deleteO(String num) {
        if (num.contains(".")) {
            if (num.endsWith("0")) {
                num = num.substring(0, num.length() - 1);
                deleteO(num);
                return "";
            }
        }
        return num;
    }
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
}
