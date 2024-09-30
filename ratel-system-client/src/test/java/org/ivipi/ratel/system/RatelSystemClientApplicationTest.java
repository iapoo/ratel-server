package org.ivipa.ratel.system;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.system.client.api.SystemApi;
import org.ivipa.ratel.system.client.api.TokenSignService;
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerInfo;
import org.ivipa.ratel.system.common.model.CustomerPassword;
import org.ivipa.ratel.system.common.model.CustomerUpdate;
import org.ivipa.ratel.system.common.model.License;
import org.ivipa.ratel.system.common.model.LicenseAdd;
import org.ivipa.ratel.system.common.model.LicensePage;
import org.ivipa.ratel.system.common.model.Login;
import org.ivipa.ratel.system.common.model.Order;
import org.ivipa.ratel.system.common.model.Product;
import org.ivipa.ratel.system.common.model.ProductAdd;
import org.ivipa.ratel.system.common.model.ProductDelete;
import org.ivipa.ratel.system.common.model.ProductPage;
import org.ivipa.ratel.system.common.model.ProductUpdate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RetrofitScan("org.ivipa.ratel")
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatelSystemClientApplicationTest {

    @Autowired
    private SystemApi systemApi;

    @Autowired
    private TokenSignService tokenSignService;

    private CustomerInfo testCustomerInfo;
    private Product testProduct;
    private License testLicense;

    @BeforeAll
    public void beforeAll() {
        log.info("Start test now ...");
        String name = generateName();
        String password = generatePassword();

        register(name, password);
        login(name, password);
        addTestProduct();
        addTestLicense();
    }

    @BeforeEach
    public void beforeEach() {

    }

    @AfterAll
    public void afterAll() {
        log.info("End test now ...");
        Result result = systemApi.logout();
        assertNotNull(result);
        assertTrue(result.isSuccess());

    }

    @AfterEach
    public void afterEach() {

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
        testCustomerInfo = customerInfoResult.getData();
    }

    private String getTimeString() {
        String timeString;
        timeString = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        return timeString;
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

    private void addTestProduct() {
        String productName = "Product" + getTimeString();
        addProduct(productName, true);
        testProduct = checkProduct(productName);
    }

    private void addTestLicense() {
        addLicense(testProduct.getProductId(), true);
        testLicense = checkLicense(testProduct.getProductId());
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
        order.setProductId(testProduct.getProductId());
        order.setRemark("Subscribe");
        Result result = systemApi.subscribe(order);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testRenew() {
        Order order = new Order();
        order.setProductId(testProduct.getProductId());
        order.setRemark("Renew");
        Result result = systemApi.renew(order);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    private void addProduct(String productName, boolean expectedResult) {
        ProductAdd productAdd = new ProductAdd();
        productAdd.setProductName(productName);
        Result result = systemApi.addProduct(productAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Product checkProduct(String productName) {
        ProductPage productPage = new ProductPage();
        productPage.setPageSize(99999);
        Result<Page<Product>> productResult = systemApi.getProducts(productPage);
        assertNotNull(productResult);
        assertTrue(productResult.isSuccess());
        long size = productResult.getData().getRecords().size();
        boolean checkProduct = false;
        Product product = null;
        for (long i = size - 1; i >= 0; i--) {
            Product theProduct = productResult.getData().getRecords().get((int) i);
            if (theProduct.getProductName().equals(productName)) {
                checkProduct = true;
                product = theProduct;
            }
        }
        assertTrue(checkProduct);
        return product;
    }

    @Test
    public void testAddProduct() {
        log.info("Add product with new product name");
        String testProductName = "Test Add Product" + getTimeString();
        addProduct(testProductName, true);
        Product product = checkProduct(testProductName);
        assertNotNull(product);
        log.info("Add product with existing product name");
        addProduct(testProductName, false);
    }

    @Test
    public void testDeleteProduct() {
        log.info("Test delete product");
        String testProductName = "Test Delete Product" + getTimeString();
        addProduct(testProductName, true);
        Product product = checkProduct(testProductName);
        assertNotNull(product);
        ProductDelete productDelete = new ProductDelete();
        productDelete.setProductId(product.getProductId());
        Result deleteResult = systemApi.deleteProduct(productDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
    }

    @Test
    public void testUpdateProduct() {
        log.info("Test update product");
        String testProductName = "Test Update Product" + getTimeString();
        addProduct(testProductName, true);
        Product product = checkProduct(testProductName);
        assertNotNull(product);
        ProductUpdate productUpdate = new ProductUpdate();
        productUpdate.setProductId(product.getProductId());
        productUpdate.setProductName(testProductName + " is updated");
        Result updateResult = systemApi.updateProduct(productUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
    }

    private void addLicense(Long productId, boolean expectedResult) {
        LicenseAdd licenseAdd = new LicenseAdd();
        licenseAdd.setProductId(productId);
        licenseAdd.setRemark("Add License Test");
        Result result = systemApi.addLicense(licenseAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private License checkLicense(Long productId) {
        LicensePage licensePage = new LicensePage();
        licensePage.setPageSize(99999);
        Result<Page<License>> licenseResult = systemApi.getLicenses(licensePage);
        long size = licenseResult.getData().getRecords().size();
        boolean checkLicense = false;
        License license = null;
        for (long i = size - 1; i >= 0; i--) {
            License theLicense = licenseResult.getData().getRecords().get((int) i);
            if (theLicense.getProductId().equals(productId) && theLicense.getCustomerId().equals(testCustomerInfo.getCustomerId())) {
                checkLicense = true;
                license = theLicense;
                break;
            }
        }
        assertTrue(checkLicense);
        return license;
    }

    @Test
    public void testAddLicense() {
        log.info("Test add license");
        addLicense(testProduct.getProductId(), true);
        //log.info("Test add license with existing product id");
        //addLicense(testProduct.getProductId(), false);
    }

}
