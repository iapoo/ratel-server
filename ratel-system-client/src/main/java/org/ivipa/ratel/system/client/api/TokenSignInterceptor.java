package org.ivipa.ratel.system.client.api;


import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenSignInterceptor extends BasePathMatchInterceptor {

    private final static String URL_LOGIN = "/login";
    private final static String URL_REGISTER = "/register";

    @Autowired
    private TokenSignService tokenSignService;

    @Override
    public Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        if(URL_LOGIN.equals(url.encodedPath()) || URL_REGISTER.equals(url.encodedPath())) {
            return chain.proceed(request);
        } else {
            String token = tokenSignService.getToken();
            Request newReq = request.newBuilder()
                    .addHeader("Token", token)
                    .build();
            return chain.proceed(newReq);
        }
    }
}
