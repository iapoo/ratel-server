package org.ivipa.ratel.rockie.server.api;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import retrofit2.http.GET;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "${retrofit.rockie.google-font-server.baseurl}")
//@Intercept(handler = TokenSignInterceptor.class, include = {"/**"})
public interface GoogleFontApi {
    @GET("/v1/webfonts")
    String getFontInfo(@Query("key") String key);
}
