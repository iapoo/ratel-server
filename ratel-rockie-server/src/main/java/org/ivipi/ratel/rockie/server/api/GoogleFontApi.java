package org.ivipi.ratel.rockie.server.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.Intercept;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.common.model.GoogleFont;
import org.ivipi.ratel.rockie.common.model.GoogleFonts;
import org.ivipi.ratel.system.client.api.TokenSignInterceptor;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@RetrofitClient(baseUrl = "${retrofit.rockie.google-font-server.baseurl}")
//@Intercept(handler = TokenSignInterceptor.class, include = {"/**"})
public interface GoogleFontApi {
    @GET("/v1/webfonts")
    String getFontInfo(@Query("key") String key);
}
