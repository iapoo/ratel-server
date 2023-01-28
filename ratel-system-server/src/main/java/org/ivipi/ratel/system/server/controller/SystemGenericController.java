package org.ivipi.ratel.system.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.common.model.LoginCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Slf4j
public abstract class SystemGenericController {

    protected final static String USER_TOKEN = "User-Token";


    @Value("${ratel.system.token.timeout}")
    private int tokenTimeout;

    @Autowired
    protected HttpServletRequest request;

    @Resource(name = "systemRedisTemplate")
    protected RedisTemplate systemRedisTemplate;

    protected boolean isLoggedIn() {
        return getUserName() != null;
    }


    protected String getUserToken() {
        return request.getHeader(USER_TOKEN);
    }

    protected String getUserName() {
        String userToken = getUserToken();
        String userName = getUserName(userToken);
        return userName;
    }

    protected void refreshUserToken(String userToken, LoginCustomer loginCustomer) {
        //HashOperations hashOperations = systemRedisTemplate.opsForHash();
        systemRedisTemplate.opsForValue().set(userToken, loginCustomer, Duration.ofSeconds(tokenTimeout));
    }

    protected  void removeUserToken(String userToken) {
    systemRedisTemplate.opsForValue().getAndDelete(userToken);
    }

    protected LoginCustomer getLoginCustomer(String userToken) {
        Object loginCustomerValue = systemRedisTemplate.opsForValue().get(userToken);
        if(loginCustomerValue != null) {
            LoginCustomer loginCustomer = (LoginCustomer)loginCustomerValue;
            refreshUserToken(userToken, loginCustomer);
            return loginCustomer;
        } else {
            return null;
        }
    }

    protected String getUserName(String userToken) {
        if(userToken != null) {
            LoginCustomer loginCustomer = getLoginCustomer(userToken);
            if(loginCustomer != null) {
                return loginCustomer.getName();
            }
        }
        return null;
    }
}
