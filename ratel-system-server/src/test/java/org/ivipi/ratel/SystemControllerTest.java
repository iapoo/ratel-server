package org.ivipi.ratel;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerInfo;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.model.CustomerUpdate;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicenseAdd;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.common.model.Login;
import org.ivipi.ratel.system.common.model.Order;
import org.ivipi.ratel.system.common.model.Product;
import org.ivipi.ratel.system.common.model.ProductAdd;
import org.ivipi.ratel.system.common.model.ProductDelete;
import org.ivipi.ratel.system.common.model.ProductPage;
import org.ivipi.ratel.system.server.api.SystemApi;
import org.ivipi.ratel.system.server.api.TokenSignService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RetrofitScan("org.ivipi.ratel")
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SystemControllerTest {

    @Autowired
    private SystemApi systemApi;

    @Autowired
    private TokenSignService tokenSignService;

    private String productName;

    private Long productId;

    private Long licenseId;

    private Long customerId;

    @BeforeAll
    public void beforeAll(){
        log.info("Start test now ...");
        String name = generateName();
        String password = generatePassword();

        register(name, password);
        login(name, password);
        addProducts();
        addLicenses();
    }

    @BeforeEach
    public  void beforeEach(){

    }

    @AfterAll
    public void afterAll(){
        log.info("End test now ...");
        deleteProducts();
        Result result = systemApi.logout();
        assertNotNull(result);
        assertTrue(result.isSuccess());

    }

    @AfterEach
    public void afterEach(){

    }

    private void login(String name, String password) {
        Login login = new Login();
        login.setName(name);
        login.setPassword(password);
        Result result = systemApi.login(login);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        String token = result.getData().toString();
        tokenSignService.setToken(token);
        Result<CustomerInfo> customerInfoResult = systemApi.info();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        customerId = customerInfoResult.getData().getCustomerId();

    }

    private String getTimeString() {
        return DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
    }

    private String generateName() {
        String namePrefix = "Customer";
        String nameSuffix = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        String name = namePrefix + nameSuffix;
        return name;
    }

    private void register(String name, String password) {
        CustomerAdd customerAdd = new CustomerAdd();
        customerAdd.setCustomerName(name);
        customerAdd.setPassword(password);
        Result result = systemApi.register(customerAdd);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    private String generatePassword() {
        return "Password1";
    }

    private void addProducts() {
        String productName = "Product" + getTimeString();
        this.productName = productName;
        ProductAdd productAdd = new ProductAdd();
        productAdd.setProductName(productName);
        Result result = systemApi.addProduct(productAdd);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        ProductPage productPage = new ProductPage();
        productPage.setPageSize(99999);
        Result<Page<Product>> productResult = systemApi.getProducts(productPage);
        long size = productResult.getData().getRecords().size();
        boolean checkProduct = false;
        for(long i = size - 1; i >= 0; i --) {
            Product product = productResult.getData().getRecords().get((int)i);
            if(product.getProductName().equals(productName)) {
                checkProduct = true;
                productId = product.getProductId();
                break;
            }
        }
        assertTrue(checkProduct);
    }

    private void deleteProducts() {
        ProductPage productPage = new ProductPage();
        productPage.setPageSize(99999);
        Result<Page<Product>> productResult = systemApi.getProducts(productPage);
        assertNotNull(productResult);
        assertTrue(productResult.isSuccess());
        long size = productResult.getData().getRecords().size();
        boolean checkProduct = false;
        for(long i = size - 1; i >= 0; i --) {
            Product product = productResult.getData().getRecords().get((int)i);
            if(product.getProductName().equals(productName)) {
                checkProduct = true;
                ProductDelete productDelete = new ProductDelete();
                productDelete.setProductId(product.getProductId());
                Result deleteResult = systemApi.deleteProduct(productDelete);
                assertNotNull(deleteResult);
                assertTrue(deleteResult.isSuccess());
            }
        }
        assertTrue(checkProduct);
    }

    private void addLicenses() {
        LicenseAdd licenseAdd = new LicenseAdd();
        licenseAdd.setProductId(productId);
        licenseAdd.setRemark("License Test");
        Result result = systemApi.addLicense(licenseAdd);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        LicensePage licensePage = new LicensePage();
        licensePage.setPageSize(99999);
        Result<Page<License>> licenseResult = systemApi.getLicenses(licensePage);
        long size = licenseResult.getData().getRecords().size();
        boolean checkLicense = false;
        for(long i = size - 1; i >= 0; i --) {
            License license = licenseResult.getData().getRecords().get((int)i);
            if(license.getProductId().equals(productId) && license.getCustomerId().equals(customerId) ) {
                checkLicense = true;
                licenseId = license.getLicenseId();
                break;
            }
        }
        assertTrue(checkLicense);
    }
    @Test
    public void testUpdate() {
        CustomerUpdate customerUpdate = new CustomerUpdate();
        customerUpdate.setNickName("Nick name is updated");
        Result result = systemApi.update(customerUpdate);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testUpdatePassword() {
        CustomerPassword customerPassword = new CustomerPassword();
        customerPassword.setOldPassword(generatePassword());
        customerPassword.setNewPassword(generatePassword());
        Result result = systemApi.updatePassword(customerPassword);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testSubscribe() {
        Order order = new Order();
        order.setProductId(productId);
        order.setRemark("Subscribe");
        Result result = systemApi.subscribe(order);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testRenew() {
        Order order = new Order();
        order.setProductId(productId);
        order.setRemark("Renew");
        Result result = systemApi.renew(order);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testAddProduct() {
        ProductAdd productAdd = new ProductAdd();
        productAdd.setProductName("Test Product" + getTimeString());
        Result result = systemApi.addProduct(productAdd);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        ProductPage productPage = new ProductPage();
        productPage.setPageSize(99999);
        Result<Page<Product>> productResult = systemApi.getProducts(productPage);
        assertNotNull(productResult);
        assertTrue(productResult.isSuccess());
        long size = productResult.getData().getRecords().size();
        boolean checkProduct = false;
        for(long i = size - 1; i >= 0; i --) {
            Product product = productResult.getData().getRecords().get((int)i);
            if(product.getProductName().equals(productAdd.getProductName())) {
                checkProduct = true;
                ProductDelete productDelete = new ProductDelete();
                productDelete.setProductId(product.getProductId());
                Result deleteResult = systemApi.deleteProduct(productDelete);
                assertNotNull(deleteResult);
                assertTrue(deleteResult.isSuccess());
            }
        }
        assertTrue(checkProduct);
    }
}
