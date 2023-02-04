package org.ivipi.ratel.rockie.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.common.model.LoginCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Slf4j
public abstract class RockieGenericController {

    protected final static String USER_TOKEN = "Token";


    @Value("${ratel.system.token.timeout}")
    private int tokenTimeout;

    @Autowired
    protected HttpServletRequest request;

    @Resource(name = "systemRedisTemplate")
    protected RedisTemplate systemRedisTemplate;

    protected boolean isLoggedIn() {
        return getCustomerName() != null;
    }

    protected String getToken() {
        return request.getHeader(USER_TOKEN);
    }

    protected String getCustomerName() {
        String token = getToken();
        String customerName = getCustomerName(token);
        return customerName;
    }

    protected void refreshLoginCustomer(String token, LoginCustomer loginCustomer) {
        //HashOperations hashOperations = systemRedisTemplate.opsForHash();
        systemRedisTemplate.opsForValue().set(token, loginCustomer, Duration.ofSeconds(tokenTimeout));
    }

    protected boolean hasLoginCustomer(String token) {
        LoginCustomer loginCustomer = (LoginCustomer) systemRedisTemplate.opsForValue().get(token);
        return loginCustomer != null;
    }

    protected  void removeLoginCustomer(String token) {
        systemRedisTemplate.opsForValue().getAndDelete(token);
    }

    protected LoginCustomer getLoginCustomer(String token) {
        Object loginCustomerValue = systemRedisTemplate.opsForValue().get(token);
        if(loginCustomerValue != null) {
            LoginCustomer loginCustomer = (LoginCustomer)loginCustomerValue;
            refreshLoginCustomer(token, loginCustomer);
            return loginCustomer;
        } else {
            return null;
        }
    }

    protected String getCustomerName(String token) {
        if(token != null) {
            LoginCustomer loginCustomer = getLoginCustomer(token);
            if(loginCustomer != null) {
                return loginCustomer.getCustomerName();
            }
        }
        return null;
    }
}
