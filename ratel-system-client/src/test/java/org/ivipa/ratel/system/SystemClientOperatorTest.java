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
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerInfo;
import org.ivipa.ratel.system.common.model.Login;
import org.ivipa.ratel.system.common.model.Operator;
import org.ivipa.ratel.system.common.model.OperatorAdd;
import org.ivipa.ratel.system.common.model.OperatorDelete;
import org.ivipa.ratel.system.common.model.OperatorPage;
import org.ivipa.ratel.system.common.model.OperatorQuery;
import org.ivipa.ratel.system.common.model.OperatorUpdate;
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
public class SystemClientOperatorTest {

    @Autowired
    private SystemApi systemApi;

    @Autowired
    private TokenSignService tokenSignService;

    private CustomerInfo testCustomerInfo;
    private boolean enableLogout;
    private String adminName = "admin";
    private String adminPassword = "97b6a7c35da84e9073ea511a4eb2bc72a5edf5c733a010b406baa02fa3386149d04fccfcf61a4885e4e09d0c1001b1eb78ee86ffea97e57f8e61dae4f97b7170";

    @BeforeAll
    public void beforeAll(){
    }

    @BeforeEach
    public  void beforeEach(){
        log.info("Start Operator Test now ... {}");
        String name = generateName();
        String password = generatePassword();

        enableLogout = false;
        register(name, password);
        enableLogout = true;
        login(name, password, false);
        name = adminName;
        password = adminPassword;
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
        log.info("End Operation Test now ...");
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
        if(!isAdmin) {
            testCustomerInfo = customerInfoResult.getData();
        } else {
            Result adminResult = systemApi.admin();
            assertNotNull(adminResult);
            assertTrue(adminResult.isSuccess());
        }
    }

    private String generateName() {
        String namePrefix = "Customer";
        String nameSuffix = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmssSSS");
        String name = namePrefix + nameSuffix;
        return name;
    }

    private void register(String name, String password) {
        log.info("Register is started");
        CustomerAdd customerAdd = new CustomerAdd();
        customerAdd.setCustomerName(name);
        customerAdd.setPassword(password);
        customerAdd.setEmail(name + "@test.com");
        Result result = systemApi.register(customerAdd);
//        String token = result.getData().toString();
//        log.info("Token is setup with value={}", token);
//        tokenSignService.setToken(token);
        assertNotNull(result);
        log.info("===={}", result.getMessage());
        assertTrue(result.isSuccess());
    }

    private String generatePassword() {
        Digester sha512 = new Digester(DigestAlgorithm.SHA512);
        return sha512.digestHex("Password1");
    }

    private void addOperator(Long customerId, boolean expectedResult) {
        OperatorAdd operatorAdd = new OperatorAdd();
        operatorAdd.setCustomerId(customerId);
        operatorAdd.setOperatorType(0L);
        Result<Operator> result = systemApi.addOperator(operatorAdd);
        assertNotNull(result);
        if(expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Operator checkOperator(Long customerId) {
        OperatorPage operatorPage = new OperatorPage();
        operatorPage.setPageSize(99999);
        Result<Page<Operator>> operatorResult = systemApi.getOperators(operatorPage);
        assertNotNull(operatorResult);
        assertTrue(operatorResult.isSuccess());
        long size = operatorResult.getData().getRecords().size();
        boolean checkOperator = false;
        Operator operator = null;
        for(long i = 0; i < size; i++) {
            Operator theOperator = operatorResult.getData().getRecords().get((int)i);
            if(theOperator.getCustomerId().equals(customerId)) {
                checkOperator = true;
                operator = theOperator;
            }
        }
        assertTrue(checkOperator);
        return operator;
    }

    @Test
    public void testAddOperator() {
        log.info("Test Add Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
    }

    @Test
    public void testGetOperator() {
        log.info("Test Get Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorQuery operatorQuery = new OperatorQuery();
        operatorQuery.setOperatorId(operatorId);
        Result<Operator> result = systemApi.getOperator(operatorQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        operator = result.getData();
        assertNotNull(operator);
        assertTrue(operatorId.equals(operator.getOperatorId()));
    }

    @Test
    public void testGetOperators() {
        log.info("Test Get Operators");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorPage operatorPage = new OperatorPage();
        Result<Page<Operator>> result = systemApi.getOperators(operatorPage);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        Page<Operator> page = result.getData();
        assertNotNull(page);
        assertTrue(page.getSize() > 0);
    }

    @Test
    public void testUpdateOperator() {
        log.info("Test Update Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorUpdate operatorUpdate = new OperatorUpdate();
        operatorUpdate.setOperatorId(operatorId);
        operatorUpdate.setCustomerId(customerId);
        operatorUpdate.setOperatorType(2L);
        Result updateResult = systemApi.updateOperator(operatorUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
        OperatorQuery operatorQuery = new OperatorQuery();
        operatorQuery.setOperatorId(operatorId);
        Result<Operator> result = systemApi.getOperator(operatorQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        operator = result.getData();
        assertNotNull(operator);
        assertTrue(operatorId.equals(operator.getOperatorId()));
        assertTrue(operator.getOperatorType() == 2);

    }

    @Test
    public void testDeleteOperator() {
        log.info("Test Delete Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorDelete operatorDelete = new OperatorDelete();
        operatorDelete.setOperatorId(operatorId);
        Result deleteResult = systemApi.deleteOperators(operatorDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
        OperatorQuery operatorQuery = new OperatorQuery();
        operatorQuery.setOperatorId(operatorId);
        Result<Operator> result = systemApi.getOperator(operatorQuery);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        operator = result.getData();
        assertTrue(operator == null);
    }


}
