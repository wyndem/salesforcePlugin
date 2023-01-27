package cn.wenhaha.plugin.data.salesforce.http;

import cn.wenhaha.plugin.data.salesforce.http.interceptor.RemoveHeaderInterceptor;
import cn.wenhaha.plugin.data.salesforce.http.interceptor.RetryInterceptor;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.OkHttps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OkHttpUtil {

    private static final long CONNECT_TIMEOUT = 30;
    private static final long READ_TIMEOUT =10;
    private static final long WRITE_TIMEOUT = 30;


    @Bean
    public  HTTP okHttpClient(){
        return buildHttp();
    }


    public static HTTP buildHttp(){
        return HTTP.builder()
                .bodyType(OkHttps.FORM)
                .config(OkHttpUtil::build)
                .build();
    }




    public  static void  build(OkHttpClient.Builder builder)  {
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888));

        builder
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
//                .proxy(proxy)
//                .addInterceptor(new LogInterceptor())
                .addNetworkInterceptor(new RemoveHeaderInterceptor())
                .addInterceptor(new RetryInterceptor(3))
                .connectionPool(new ConnectionPool(10, 5L, TimeUnit.MINUTES));

        X509TrustManager myTrustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }

        };

        HostnameVerifier myHostnameVerifier = (hostname, session) -> true;

        try {
            SSLContext sslCtx = SSLContext.getInstance("TLS");
            sslCtx.init(null, new TrustManager[] { myTrustManager }, new SecureRandom());

            SSLSocketFactory mySSLSocketFactory = sslCtx.getSocketFactory();

            builder.sslSocketFactory(mySSLSocketFactory, myTrustManager)
                    .hostnameVerifier(myHostnameVerifier);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

    }






}
