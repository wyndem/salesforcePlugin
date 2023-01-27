package cn.wenhaha.plugin.data.salesforce.http.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class RemoveHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request =request.newBuilder().removeHeader("User-Agent").build();
        return chain.proceed(request);
    }
}
