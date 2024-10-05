package org.ivipa.ratel.system;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.system.client.api.SystemApi;
import org.ivipa.ratel.system.client.api.TokenSignService;
import org.ivipa.ratel.system.common.model.Customer;
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerDelete;
import org.ivipa.ratel.system.common.model.CustomerInfo;
import org.ivipa.ratel.system.common.model.CustomerPage;
import org.ivipa.ratel.system.common.model.CustomerQuery;
import org.ivipa.ratel.system.common.model.CustomerUpdate;
import org.ivipa.ratel.system.common.model.Login;
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
public class SystemClientCustomerTest {

    @Autowired
    private SystemApi systemApi;

    @Autowired
    private TokenSignService tokenSignService;

    private boolean enableLogout;
    private String adminName = "admin";
    private String adminPassword = "97b6a7c35da84e9073ea511a4eb2bc72a5edf5c733a010b406baa02fa3386149d04fccfcf61a4885e4e09d0c1001b1eb78ee86ffea97e57f8e61dae4f97b7170";

    @BeforeAll
    public void beforeAll(){
    }

    @BeforeEach
    public  void beforeEach(){
        log.info("Start Customer Test now ... {}");
        enableLogout = true;
        String name = adminName;
        String password = adminPassword;
        login(name, password, true);
        //Sleep so different customer name can be generated
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            //Nothing needed here
        }
    }

    @AfterAll
    public void afterAll(){
        log.info("End Customer Test now ...");
        if(enableLogout) {
            Result result = systemApi.logout();
            assertNotNull(result);
            assertTrue(result.isSuccess());
        }

    }

    @AfterEach
    public void afterEach(){

    }

    private void login(String name, String password, boolean isAdmin) {
        log.info("Login is started");
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
        Result adminResult = systemApi.admin();
        assertNotNull(adminResult);
        assertTrue(adminResult.isSuccess());
    }

    private String generateName() {
        String namePrefix = "Customer";
        String nameSuffix = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmssSSS");
        String name = namePrefix + nameSuffix;
        return name;
    }

    private String generatePassword() {
        Digester sha512 = new Digester(DigestAlgorithm.SHA512);
        return sha512.digestHex("Password1");
    }

    private void addCustomer(String customerName, String customerPassword, boolean expectedResult) {
        CustomerAdd customerAdd = new CustomerAdd();
        customerAdd.setCustomerName(customerName);
        customerAdd.setPassword(customerPassword);
        customerAdd.setEmail(customerName + "@test.com");
        Result<Customer> result = systemApi.addCustomer(customerAdd);
        assertNotNull(result);
        if(expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Customer checkCustomer(String customerName) {
        CustomerPage customerPage = new CustomerPage();
        customerPage.setCustomerName(customerName);
        customerPage.setPageSize(99999);
        Result<Page<Customer>> customerResult = systemApi.getCustomers(customerPage);
        assertNotNull(customerResult);
        assertTrue(customerResult.isSuccess());
        long size = customerResult.getData().getRecords().size();
        boolean checkCustomer = false;
        Customer customer = null;
        for(long i = 0; i < size; i++) {
            Customer theCustomer = customerResult.getData().getRecords().get((int)i);
            if(theCustomer.getCustomerName().equals(customerName)) {
                checkCustomer = true;
                customer = theCustomer;
            }
        }
        assertTrue(checkCustomer);
        return customer;
    }

    @Test
    public void testAddCustomer() {
        log.info("Test Add Customer");
        String customerName = generateName();
        String customerPassword = generatePassword();
        addCustomer(customerName, customerPassword, true);
        Customer customer = checkCustomer(customerName);
        assertNotNull(customer);
    }

    @Test
    public void testGetCustomer() {
        log.info("Test Get Customer");
        String customerName = generateName();
        String customerPassword = generatePassword();
        addCustomer(customerName, customerPassword, true);
        Customer customer = checkCustomer(customerName);
        assertNotNull(customer);
        Long customerId = customer.getCustomerId();
        CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.setCustomerId(customerId);
        Result<Customer> result = systemApi.getCustomer(customerQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        customer = result.getData();
        assertNotNull(customer);
        assertTrue(customerId.equals(customer.getCustomerId()));
    }

    @Test
    public void testGetCustomers() {
        log.info("Test Get Customers");
        String customerName = generateName();
        String customerPassword = generatePassword();
        addCustomer(customerName, customerPassword, true);
        Customer customer = checkCustomer(customerName);
        assertNotNull(customer);
        CustomerPage customerPage = new CustomerPage();
        Result<Page<Customer>> result = systemApi.getCustomers(customerPage);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        Page<Customer> page = result.getData();
        assertNotNull(page);
        assertTrue(page.getSize() > 0);
    }

    @Test
    public void testUpdateCustomer() {
        log.info("Test Update Customer");
        String customerName = generateName();
        String customerPassword = generatePassword();
        addCustomer(customerName, customerPassword, true);
        Customer customer = checkCustomer(customerName);
        assertNotNull(customer);
        Long customerId = customer.getCustomerId();
        CustomerUpdate customerUpdate = new CustomerUpdate();
        customerUpdate.setCustomerId(customerId);
        customerUpdate.setNickName(customerName);
        Result updateResult = systemApi.updateCustomer(customerUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
        CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.setCustomerId(customerId);
        Result<Customer> result = systemApi.getCustomer(customerQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        customer = result.getData();
        assertNotNull(customer);
        assertTrue(customerId.equals(customer.getCustomerId()));
        assertTrue(customerName.equals(customer.getNickName()));

    }

    @Test
    public void testDeleteCustomer() {
        log.info("Test Delete Customer");
        String customerName = generateName();
        String customerPassword = generatePassword();
        addCustomer(customerName, customerPassword, true);
        Customer customer = checkCustomer(customerName);
        assertNotNull(customer);
        Long customerId = customer.getCustomerId();
        CustomerDelete customerDelete = new CustomerDelete();
        customerDelete.setCustomerId(customerId);
        Result deleteResult = systemApi.deleteCustomers(customerDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
        CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.setCustomerId(customerId);
        Result<Customer> result = systemApi.getCustomer(customerQuery);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        customer = result.getData();
        assertTrue(customer == null);
    }


}
