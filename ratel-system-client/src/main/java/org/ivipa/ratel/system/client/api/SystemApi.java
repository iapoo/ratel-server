package org.ivipa.ratel.system.client.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.system.common.model.Customer;
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerDelete;
import org.ivipa.ratel.system.common.model.CustomerInfo;
import org.ivipa.ratel.system.common.model.CustomerOperatorPage;
import org.ivipa.ratel.system.common.model.CustomerPage;
import org.ivipa.ratel.system.common.model.CustomerPassword;
import org.ivipa.ratel.system.common.model.CustomerQuery;
import org.ivipa.ratel.system.common.model.CustomerUpdate;
import org.ivipa.ratel.system.common.model.License;
import org.ivipa.ratel.system.common.model.LicenseAdd;
import org.ivipa.ratel.system.common.model.LicensePage;
import org.ivipa.ratel.system.common.model.LicenseUpdate;
import org.ivipa.ratel.system.common.model.Login;
import org.ivipa.ratel.system.common.model.Operator;
import org.ivipa.ratel.system.common.model.OperatorAdd;
import org.ivipa.ratel.system.common.model.OperatorDelete;
import org.ivipa.ratel.system.common.model.OperatorDetail;
import org.ivipa.ratel.system.common.model.OperatorPage;
import org.ivipa.ratel.system.common.model.OperatorQuery;
import org.ivipa.ratel.system.common.model.OperatorUpdate;
import org.ivipa.ratel.system.common.model.Order;
import org.ivipa.ratel.system.common.model.Product;
import org.ivipa.ratel.system.common.model.ProductAdd;
import org.ivipa.ratel.system.common.model.ProductDelete;
import org.ivipa.ratel.system.common.model.ProductPage;
import org.ivipa.ratel.system.common.model.ProductQuery;
import org.ivipa.ratel.system.common.model.ProductUpdate;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.Properties;

@RetrofitClient(baseUrl = "${retrofit.system.baseUrl}")
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

    @POST("info")
    Result<CustomerInfo> info();

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

    @POST("product/products")
    Result<Page<Product>> getProducts(@Body ProductPage productPage);

    @POST("product/add")
    Result addProduct(@Body ProductAdd productAdd);

    @POST("product/update")
    Result updateProduct(@Body ProductUpdate productUpdate);

    @POST("product/delete")
    Result deleteProduct(@Body ProductDelete productDelete);

    @POST("product/product")
    Result getProduct(@Body ProductQuery productQuery);

    @POST("license/licenses")
    Result<Page<License>> getLicenses(@Body LicensePage licensePage);

    @POST("license/add")
    Result addLicense(@Body LicenseAdd licenseAdd);

    @POST("license/update")
    Result updateLicense(@Body LicenseUpdate licenseUpdate);

    @POST("operator/operators")
    public Result<Page<Operator>> getOperators(@Body OperatorPage operatorPage);

    @POST("operator/operatorDetails")
    public Result<Page<OperatorDetail>> getOperatorDetails(@Body OperatorPage operatorPage);

    @POST("operator/operator")
    public Result<Operator> getOperator(@Body OperatorQuery operatorQuery);

    @POST("operator/add")
    public Result<Operator> addOperator(@Body OperatorAdd operatorAdd);

    @POST("operator/update")
    public Result updateOperator(@Body OperatorUpdate operatorUpdate);

    @POST("operator/delete")
    public Result deleteOperators(@Body OperatorDelete operatorDelete);

    @POST("customer/customers")
    public Result<Page<Customer>> getCustomers(@Body CustomerPage customerPage);

    @POST("customer/operatorCustomers")
    public Result<Page<Customer>> getOperatorCustomers(@Body CustomerOperatorPage customerOperatorPage);

    @POST("customer/customer")
    public Result<Customer> getCustomer(@Body CustomerQuery customerQuery);

    @POST("customer/add")
    public Result<Customer> addCustomer(@Body CustomerAdd customerAdd);

    @POST("customer/update")
    public Result updateCustomer(@Body CustomerUpdate customerUpdate);

    @POST("customer/delete")
    public Result deleteCustomers(@Body CustomerDelete customerDelete);

    @POST("admin")
    public Result admin();

    @POST("properties")
    public Result<Properties> properties();
}
