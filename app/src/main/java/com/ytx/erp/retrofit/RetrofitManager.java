package com.ytx.erp.retrofit;


import android.content.Context;

import com.ytx.erp.api.Apiservice;
import com.ytx.erp.network.BaseApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author：KePeiKun
 * Time：2020/8/24
 * Description：
 */

public class RetrofitManager {
    private volatile static RetrofitManager retrofitManager;
    private Retrofit retrofit;
    private Retrofit retrofit2;
    //无参的单利模式
    public static RetrofitManager getSingleton() {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                retrofitManager = new RetrofitManager();
            }
        }
        return retrofitManager;
    }

    private static final int READ_TIMEOUT = 60;//读取超时时间,单位秒
    private static final int CONN_TIMEOUT = 50;//连接超时时间,单位秒
    /**
     * 初始化一个client,不然retrofit会自己默认添加一个
     */

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("Content-Type", "application/json");
//                    UserBean userBean = SpUtils.getLoginData(EStoreApplication.getInstance().getApplicationContext());
//                    if(userBean!=null){
//                        builder.addHeader("usertoken", userBean.getData().getToken());
//                    }

                    return chain.proceed(builder.build());
                }
            })
            .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)//设置连接时间为50s
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取时间为一分钟
            .build();


    public static OkHttpClient getOkHttpClient() {
        InputStream bksFile = null;
        String psd = "";
        Context context = BaseApplication.getInstance();
//        InputStream[] certificates = getCertificates(context, CERTIFICATES);
        InputStream[] certificates = new InputStream[0];
        try {
            certificates = getCertificates(context, String.valueOf(context.getAssets().open("tomcat.crt")));
//            Log.e("net","获取assets"+certificates);
        } catch (IOException e) {
//            Log.e("net","获取assets");
            e.printStackTrace();
        }
//        //获取证书
//        try {
//            bksFile = context.getAssets().open("skxy.cer");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(certificates, bksFile, psd);
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(certificates, null, null);
        OkHttpClient okHttpClient = new OkHttpClient();
        if (certificates == null) {
            okHttpClient = okHttpClient.newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new LogInterceptor())
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(getUnSafeHostnameVerifier()).build();
        } else {
            okHttpClient = okHttpClient.newBuilder()
                    .addInterceptor(new LogInterceptor())
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();
        }
        return okHttpClient;
    }

    /**
     * 获取服务端证书
     * <p>
     * 默认放在Assets目录下
     *
     * @param context
     * @return
     */
    public static InputStream[] getCertificates(Context context, String... fileNames) {
        if (context == null || fileNames == null || fileNames.length <= 0) {
            return null;
        }
        InputStream[] certificates = null;
        try {
            certificates = new InputStream[fileNames.length];
            for (int i = 0; i < fileNames.length; i++) {
                certificates[i] = context.getAssets().open(fileNames[i]);
            }
//            return certificates;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return certificates;
    }

    /**
     * 不验证，即信任所有证书时使用
     * 有安全隐患，慎用！！！
     *
     * @return
     */
    public static UnSafeHostnameVerifier getUnSafeHostnameVerifier() {
        return new UnSafeHostnameVerifier();
    }

    private static class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 获取自定义SSLSocketFactory
     * <p>
     * 单项验证时只需要certificates，其余null即可
     * 双向验证时，3个参数均需要
     * <p>
     * 不验证，即信任所有证书时全部传null，同时配合getUnSafeHostnameVerifier()
     * 有安全隐患，慎用！！！
     *
     * @param certificates 服务端证书（.crt）
     * @param bksFile      客户端证书请求文件（.jsk -> .bks)
     * @param password     生成jks时的密钥库口令
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            if (trustManagers == null || trustManagers.length <= 0) {
                trustManagers = new TrustManager[]{new UnSafeTrustManager()};
            }
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new AssertionError(e);
        }
    }

    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//            Log.i("authType: ", authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//            Log.i("authType: ", authType);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
//            Log.e("X509TrustManager", "X509Certificate checkServerTrusted");
            return new X509Certificate[]{};
        }
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream is : certificates) {
                String certificateAlias = Integer.toString(index++);
                Certificate certificate = certificateFactory.generateCertificate(is);
                keyStore.setCertificateEntry(certificateAlias, certificate);
                try {
                    if (is != null)
                        is.close();
                } catch (IOException ignored) {
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
            // TODO: 2016/11/11 针对有效期异常导致校验失败的情况，目前没有完美的解决方案
//            TrustManager[] keyStoreTrustManagers = trustManagerFactory.getTrustManagers();
//            return getNotValidateTimeTrustManagers((X509TrustManager[]) keyStoreTrustManagers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    //无参的构造方法
    private RetrofitManager() {
        initRetrofitManager();
    }

    //构造方法创建Retrofit实例
    private void initRetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Apiservice.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
//                .client(getOkHttpClient())
                .build();

    }


    public <T> T Apiservice(Class<T> service) {
        return retrofit.create(service);
    }


}
