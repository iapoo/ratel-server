package org.ivipa.ratel.system.server.config;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.common.model.OnlineCustomer;
import org.ivipa.ratel.system.common.model.Operator;
import org.ivipa.ratel.system.common.utils.SystemConstants;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.DefaultParameters;
import org.springframework.data.repository.query.ParametersSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

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

    private final static String[] AUTH_IGNORE_LIST = {"/login", "/register", "/sendMail", "/sendVerificationCode", "/verifyCode"};
    private final static String[] AUDIT_IGNORE_LIST = {};
    private final static String[] OPERATION_LIST = {"/operator/", "/customer/"};

    @Value("${ratel.system.token.timeout}")
    private int tokenTimeout;

    @Resource(name = "systemRedisTemplate")
    protected RedisTemplate systemRedisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Pointcut(value = "execution(public * org.ivipa.ratel.system.server.controller..*.*(..))")
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
        String tokenKey = SystemConstants.TOKEN_PREFIX + token;
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
                OnlineCustomer onlineCustomer = (OnlineCustomer)systemRedisTemplate.opsForValue().get(tokenKey);
                if (onlineCustomer == null) {
                    throw SystemError.SYSTEM_TOKEN_NOT_FOUND.newException();
                }
                customerId = onlineCustomer.getCustomerId();
                customerName = onlineCustomer.getCustomerName();
                DefaultParameters parameters = new DefaultParameters(ParametersSource.of(method));
                if (parameters.getNumberOfParameters() > 0) {
                    if (Auth.class == parameters.getParameter(0).getType()) {
                        auth = new Auth();
                        auth.setToken(token);
                        auth.setOnlineCustomer(onlineCustomer);
                        joinPoint.getArgs()[0] = auth;
                    }
                }
                refreshLoginCustomer(token, onlineCustomer);

                boolean isOperation = false;
                String operationKey = SystemConstants.OPERATOR_PREFIX + customerId;
                for(String operationResource: OPERATION_LIST) {
                    if (resource.contains(operationResource)) {
                        isOperation = true;
                        break;
                    }
                }
                if (isOperation) {
                    Operator operator = (Operator)systemRedisTemplate.opsForValue().get(operationKey);
                    if (operator == null) {
                        throw SystemError.OPERATOR_OPERATOR_NOT_FOUND.newException();
                    }
                    refreshLoginOperation(customerId, operator);
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


    protected void refreshLoginCustomer(String token, OnlineCustomer onlineCustomer) {
        //HashOperations hashOperations = systemRedisTemplate.opsForHash();
        String tokenKey = SystemConstants.TOKEN_PREFIX + token;
        systemRedisTemplate.opsForValue().set(tokenKey, onlineCustomer, Duration.ofSeconds(tokenTimeout));
    }

    private void refreshLoginOperation(Long customerId, Operator operator) {
        String key = SystemConstants.OPERATOR_PREFIX + operator.getCustomerId();
        systemRedisTemplate.opsForValue().set(key, operator, Duration.ofSeconds(tokenTimeout) );
    }

    private void log(Long customerId, String customerName, String token, String resource, String ipAddress, boolean success) {

    }

}
