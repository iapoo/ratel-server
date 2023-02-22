package org.ivipi.ratel;


import cn.hutool.core.date.DateUtil;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerUpdate;
import org.ivipi.ratel.system.common.model.Login;
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
public class SystemServerTest {

    @Autowired
    private SystemApi systemApi;

    @Autowired
    private TokenSignService tokenSignService;


    @BeforeAll
    public void beforeAll(){
        log.info("Start test now ...");
        String name = generateName();
        String password = generatePassword();

        register(name, password);
        login(name, password);
    }

    @BeforeEach
    public  void beforeEach(){

    }

    @AfterAll
    public void afterAll(){
        log.info("End test now ...");
        Result result = systemApi.logout();
        assertNotNull(result);
        assertNotNull(result.isSuccess());
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
        assertNotNull(result.isSuccess());
    }

    private String generatePassword() {
        return "Password1";
    }

    @Test
    public void testUpdate() {
        CustomerUpdate customerUpdate = new CustomerUpdate();
        customerUpdate.setNickName("Nick name is updated");
        Result result = systemApi.update(customerUpdate);
        assertNotNull(result);
        assertNotNull(result.isSuccess());
    }

    @Test
    public void testUpdatePassword() {

    }

    @Test
    public void testSubscribe() {

    }

    @Test
    public void testRenew() {

    }
}
