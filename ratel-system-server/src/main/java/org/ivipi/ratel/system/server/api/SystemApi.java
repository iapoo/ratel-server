package org.ivipi.ratel.system.server.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.Intercept;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.model.CustomerUpdate;
import org.ivipi.ratel.system.common.model.Login;
import org.ivipi.ratel.system.common.model.Order;
import retrofit2.http.Body;
import retrofit2.http.POST;

@RetrofitClient(baseUrl = "http://127.0.0.1:8080/")
@Intercept(handler = TokenSignInterceptor.class, include = {"/**"})
public interface SystemApi {

    @POST("login")
    Result login(@Body Login login);

    @POST("register")
    Result register(@Body CustomerAdd customerAdd);


    @POST("logout")
    Result logout();


    @POST("update")
    Result update(@Body CustomerUpdate customerUpdate);

    @POST("updatePassword")
    Result updatePassword(@Body CustomerPassword customerPassword);

    @POST("subscribe")
    Result subscribe(@Body Order order);

    @POST("renew")
    Result renew(@Body Order order);

    @POST("getLicenses")
    Result getLicenses(@Body CustomerPassword customerPassword);

    @POST("getLicense")
    Result getLicense(@Body CustomerPassword customerPassword);


    @POST("getProducts")
    Result getProducts();

    @POST("getProduct")
    Result getProduct();

}
