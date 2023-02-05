package org.ivipi.ratel.system.server.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ivipi.ratel.system.common.annoation.Audit;
import org.ivipi.ratel.system.common.model.LoginCustomer;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.common.utils.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.DefaultParameters;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: rdf-platform-v2
 * @description: 操作日志增强类
 * @author: LiJiufan
 * @create: 2018-11-27 19:24
 **/

@Component
@Aspect
@Slf4j
@Order(2)
public class ControllerAdvice {

    private final static String[] AUTH_IGNORE_LIST = {"/login", "/register"};
    private final static String[] AUDIT_IGNORE_LIST = {};

    @Value("${ratel.system.token.timeout}")
    private int tokenTimeout;

    @Resource(name = "systemRedisTemplate")
    protected RedisTemplate systemRedisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Pointcut(value = "execution(public * org.ivipi.ratel.system.server.controller..*.*(..))")
    public void executePackage() {
    }


    @Around("executePackage()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        Object result = null;
        Audit auditFlag = method.getAnnotation(Audit.class);
        boolean isLog = auditFlag != null;
        Auth auth = null;
        Long customerId = null;
        String customerName = null;
        String token = null;
        String resource = request.getServletPath();
        String ipAddress = getIpAddress(request);
        boolean ignored = false;
        token = request.getHeader(SystemConstants.TOKEN);
        for (String ignoreResource : AUTH_IGNORE_LIST) {
            if (resource.contains(ignoreResource)) {
                ignored = true;
                break;
            }
        }
        try {
            if(!ignored) {
                if (StrUtil.isEmpty(token)) {
                    throw SystemError.SYSTEM_TOKEN_NOT_FOUND.newException();
                }
                LoginCustomer loginCustomer = (LoginCustomer)systemRedisTemplate.opsForValue().get(token);
                if (loginCustomer == null) {
                    throw SystemError.SYSTEM_TOKEN_NOT_FOUND.newException();
                }
                customerId = loginCustomer.getCustomerId();
                customerName = loginCustomer.getCustomerName();
                DefaultParameters parameters = new DefaultParameters(method);
                if (parameters.getNumberOfParameters() > 0) {
                    if (Auth.class == parameters.getParameter(0).getType()) {
                        auth = new Auth();
                        auth.setToken(token);
                        auth.setLoginCustomer(loginCustomer);
                        joinPoint.getArgs()[0] = auth;
                    }
                }
            }
            log.info("User access : [customerId={}, customerName={}, token={}, resource={}, ipAddress={}], isLog={}", customerId, customerName, token, resource, ipAddress, isLog);
            result = joinPoint.proceed(joinPoint.getArgs());
            if (isLog) {
                log(customerId, customerName, token, resource,ipAddress ,  true);
            }
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            if (isLog) {
                log(customerId, customerName,token, resource, ipAddress,  false);
            }
            throw throwable;
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    private void log(Long customerId, String customerName, String token, String resource, String ipAddress, boolean success) {

    }

}
