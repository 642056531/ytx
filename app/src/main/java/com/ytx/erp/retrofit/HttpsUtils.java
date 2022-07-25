package com.ytx.erp.retrofit;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：
 */
public class HttpsUtils {

        private static final String KEY_STORE_TYPE_BKS = "bks";
        private static final String KEY_STORE_TYPE_P12 = "PKCS12";
        public static final String KEY_STORE_PASSWORD = "123456";//P12文件密码
        public static final String BKS_STORE_PASSWORD = "123456";//BKS文件密码
        public static SSLSocketFactory sSLSocketFactory;
        public static X509TrustManager trustManager;

        /**
         * 双向校验中SSLSocketFactory X509TrustManager 参数的生成
         * @param application
         */
        public static void initSslSocketFactory(Context application) {
            try {
                InputStream bksStream = application.getAssets().open("xxxx.bks");//客户端信任的服务器端证书流
                InputStream p12Stream = application.getAssets().open("xxxx.p12");//服务器需要验证的客户端证书流

                // 客户端信任的服务器端证书
                KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
                // 服务器端需要验证的客户端证书
                KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);

                try {
                    trustStore.load(bksStream, BKS_STORE_PASSWORD.toCharArray());//加载客户端信任的服务器证书
                    keyStore.load(p12Stream, KEY_STORE_PASSWORD.toCharArray());//加载服务器信任的客户端证书
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bksStream.close();
                        p12Stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                SSLContext sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                trustManager = chooseTrustManager(trustManagerFactory.getTrustManagers());//生成用来校验服务器真实性的trustManager


                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
                keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());//生成服务器用来校验客户端真实性的KeyManager
                //初始化SSLContext
                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                sSLSocketFactory = sslContext.getSocketFactory();//通过sslContext获取到SocketFactory

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        /**
         * 单向校验中SSLSocketFactory X509TrustManager 参数的生成
         * 通常单向校验一般都是服务器不校验客户端的真实性，客户端去校验服务器的真实性
         * @param application
         */
        public static void initSslSocketFactorySingle(Application application) {
            try {
                InputStream bksStream = application.getAssets().open("xxxx.bks");//客户端信任的服务器端证书流

                // 客户端信任的服务器端证书
                KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

                try {
                    trustStore.load(bksStream, BKS_STORE_PASSWORD.toCharArray());//加载客户端信任的服务器证书
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bksStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                trustManager = chooseTrustManager(trustManagerFactory.getTrustManagers());//生成用来校验服务器真实性的trustManager

                SSLContext sslContext = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                //初始化SSLContext
                sSLSocketFactory = sslContext.getSocketFactory();//通过sslContext获取到SocketFactory

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        /**
         * 单向校验中,通过crt格式的证书生成SSLSocketFactory X509TrustManager 参数的生成
         * 通常在Android中，客户端用于校验服务器真实性的证书是支持BKS格式的，但是往往后台给的证书都是crt格式的
         * 当然我们可以自己生成BKS，但是想更方便一些我们也是可以直接使用crt格式的证书的
         * @param application
         */
        public static void initSslSocketFactorySingleBuyCrt(Application application) {
            try {
                InputStream crtStream = application.getAssets().open("tomcat.crt");//客户端信任的服务器端证书流
                CertificateFactory cf;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    //适配Android P及以后版本，否则报错NoSuchAlgorithmException
                    cf = CertificateFactory.getInstance("X.509", "AndroidOpenSSL");//
                } else {
                    cf = CertificateFactory.getInstance("X.509", "BC");
                }
//CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509","BC");//, "BC"
//                Logger.e("TAG", " certificates.length: " + certificates.length);

//                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate ca = cf.generateCertificate(crtStream);
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore trustStore = KeyStore.getInstance(keyStoreType);

                try {
                    trustStore.load(null, null);
                    trustStore.setCertificateEntry("ca", ca);
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        crtStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                trustManager = chooseTrustManager(trustManagerFactory.getTrustManagers());//生成用来校验服务器真实性的trustManager

                SSLContext sslContext = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
                //初始化SSLContext
                sSLSocketFactory = sslContext.getSocketFactory();//通过sslContext获取到SocketFactory
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }
}

