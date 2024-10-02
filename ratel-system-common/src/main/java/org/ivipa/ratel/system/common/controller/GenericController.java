package org.ivipa.ratel.system.common.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.system.common.model.OnlineCustomer;
import org.ivipa.ratel.system.common.utils.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Slf4j
public abstract class GenericController {


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
        return request.getHeader(SystemConstants.TOKEN);
    }

    protected String getCustomerName() {
        String token = getToken();
        String customerName = getCustomerName(token);
        return customerName;
    }

    protected void refreshLoginCustomer(String token, OnlineCustomer onlineCustomer) {
        //HashOperations hashOperations = systemRedisTemplate.opsForHash();
        String tokenKey = SystemConstants.TOKEN_PREFIX + token;
        systemRedisTemplate.opsForValue().set(tokenKey, onlineCustomer, Duration.ofSeconds(tokenTimeout));
    }

    protected boolean hasLoginCustomer(String token) {
        String tokenKey = SystemConstants.TOKEN_PREFIX + token;
        OnlineCustomer onlineCustomer = (OnlineCustomer) systemRedisTemplate.opsForValue().get(tokenKey);
        return onlineCustomer != null;
    }

    protected  void removeLoginCustomer(String token) {
        String tokenKey = SystemConstants.TOKEN_PREFIX + token;
        systemRedisTemplate.opsForValue().getAndDelete(tokenKey);
    }

    protected OnlineCustomer getLoginCustomer(String token) {
        String tokenKey = SystemConstants.TOKEN_PREFIX + token;
        Object loginCustomerValue = systemRedisTemplate.opsForValue().get(tokenKey);
        if(loginCustomerValue != null) {
            OnlineCustomer onlineCustomer = (OnlineCustomer)loginCustomerValue;
            refreshLoginCustomer(token, onlineCustomer);
            return onlineCustomer;
        } else {
            return null;
        }
    }

    protected String getCustomerName(String token) {
        if(token != null) {
            OnlineCustomer onlineCustomer = getLoginCustomer(token);
            if(onlineCustomer != null) {
                return onlineCustomer.getCustomerName();
            }
        }
        return null;
    }
}
