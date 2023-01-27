package org.ivipi.ratel.system.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Slf4j
public abstract class GenericController {

    protected final static String LOGIN_USER_NAME = "login-user-name";
    protected final static String LOGIN_USER_TOKEN = "login-user-token";
    protected final static String USER_TOKEN = "User-Token";


    @Value("${ratel.system.token.timeout}")
    private int tokenTimeout;

    @Autowired
    protected HttpServletRequest request;

    @Resource(name = "systemRedisTemplate")
    protected RedisTemplate systemRedisTemplate;

    protected String getSession(String attributeName) {
        return String.valueOf(request.getSession().getAttribute(attributeName));
    }

    protected void putSession(String attributeName, String attributeValue) {
        request.getSession().setAttribute(attributeName, attributeValue);
    }

    protected boolean IsLoggedIn() {
        return getLoginUserName() != null;
    }

    protected String getLoginUserName() {
        return getSession(LOGIN_USER_NAME);
    }

    protected String getUserToken() {
        return request.getHeader(USER_TOKEN);
    }

    protected String getUserName() {
        String userToken = getUserToken();
        String userName = getUserName(userToken);
        return userName;
    }

    protected void refreshUserToken(String userToken, String userName) {
        systemRedisTemplate.opsForValue().set(userToken, userName, Duration.ofSeconds(tokenTimeout));
    }

    protected String getUserName(String userToken) {
        Object userNameValue = systemRedisTemplate.opsForValue().get(userToken);
        if(userNameValue != null) {
            String userName = userNameValue.toString();
            refreshUserToken(userToken, userName);
            return userName;
        } else {
            return null;
        }
    }
}
