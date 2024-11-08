package org.ivipa.ratel.system.server.controller;

import cn.hutool.core.util.IdUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.controller.GenericController;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.common.model.Customer;
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerInfo;
import org.ivipa.ratel.system.common.model.CustomerLicense;
import org.ivipa.ratel.system.common.model.CustomerPassword;
import org.ivipa.ratel.system.common.model.CustomerSettings;
import org.ivipa.ratel.system.common.model.CustomerUpdate;
import org.ivipa.ratel.system.common.model.Login;
import org.ivipa.ratel.system.common.model.OnlineCustomer;
import org.ivipa.ratel.system.common.model.Operator;
import org.ivipa.ratel.system.common.model.Order;
import org.ivipa.ratel.system.common.model.VerificationMail;
import org.ivipa.ratel.system.common.utils.SystemConstants;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.ivipa.ratel.system.domain.service.CustomerService;
import org.ivipa.ratel.system.domain.service.LicenseService;
import org.ivipa.ratel.system.domain.service.OperatorService;
import org.ivipa.ratel.system.domain.service.ProductService;
import org.ivipa.ratel.system.server.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/")
@Slf4j
public class SystemController extends GenericController {


    @Value("${ratel.system.token.timeout}")
    private int tokenTimeout;

    @Value("${ratel.system.enable-mail-validation}")
    private boolean enableMailValidation;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MailService mailService;


    @Resource(name = "systemRedisTemplate")
    protected RedisTemplate systemRedisTemplate;

    @PostMapping("register")
    @Audit
    public Result register(@RequestBody CustomerAdd customerAdd) {
        if(enableMailValidation) {
            mailService.verifyMail(customerAdd.getEmail(), customerAdd.getCode());
        }
        customerService.addCustomer(customerAdd);
        return Result.success();
    }

    @PostMapping("login")
    @Audit
    public Result login(@RequestBody Login login) {
        Customer customer = customerService.getCustomer(login.getName(), login.getPassword());
        if(customer != null) {
            String token = IdUtil.simpleUUID();
            OnlineCustomer onlineCustomer = new OnlineCustomer();
            onlineCustomer.setCustomerName(login.getName());
            onlineCustomer.setCustomerId(customer.getCustomerId());
            onlineCustomer.setCustomerCode(customer.getCustomerCode());
            refreshLoginCustomer(token, onlineCustomer);
            return Result.success(token);
        } else {
            return Result.error(SystemError.SYSTEM_LOGIN_FAILED.newException());
        }
    }

    @PostMapping("logout")
    @Audit
    public Result logout() {
        String token = getToken();
        if(token != null) {
            boolean hasToken = hasLoginCustomer(token);
            if(hasToken) {
                removeLoginCustomer(token);
            } else {
                throw SystemError.SYSTEM_TOKEN_NOT_FOUND.newException();
            }
        } else {
            throw SystemError.SYSTEM_TOKEN_NOT_FOUND.newException();
        }
        return Result.success();
    }

    @PostMapping("update")
    @Audit
    public Result update(Auth auth, @RequestBody CustomerUpdate customerUpdate) {
       customerService.updateCustomer(auth, customerUpdate);
        return Result.success();
    }

    @PostMapping("updatePassword")
    @Audit
    public Result updatePassword(Auth auth, @RequestBody CustomerPassword customerPassword) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        customerService.updatePassword(customerId, customerPassword);
        return Result.success();
    }

    @PostMapping("updateSettings")
    @Audit
    public Result updateSettings(Auth auth, @RequestBody CustomerSettings customerSettings) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        customerService.updateSettings(customerId, customerSettings);
        return Result.success();
    }

    @PostMapping("settings")
    @Audit
    public Result<CustomerSettings> settings(Auth auth) {
        CustomerSettings customerSettings = customerService.getCustomerSettings(auth);
        return Result.success(customerSettings);
    }

    @PostMapping("subscribe")
    @Audit
    public Result subscribe(Auth auth, @RequestBody Order order) {
        licenseService.subscribe(auth, order);
        return Result.success();
    }

    @PostMapping("renew")
    @Audit
    public Result renew(Auth auth, @RequestBody Order order) {
        licenseService.renewLicense(auth, order);
        return Result.success();
    }


    @PostMapping("info")
    @Audit
    public Result<CustomerInfo> info(Auth auth) {
        CustomerInfo customerInfo = customerService.getCustomerInfo(auth);
        if(customerInfo != null) {
            refreshLoginCustomer(auth.getToken(), auth.getOnlineCustomer());
        }
        return Result.success(customerInfo);
    }

    @PostMapping("getLicenses")
    @Audit
    public Result getLicenses(Auth auth, @RequestBody CustomerPassword customerPassword) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        customerService.updatePassword(customerId, customerPassword);
        return Result.success();
    }

    @PostMapping("getLicense")
    @Audit
    public Result getLicense(Auth auth, @RequestBody CustomerPassword customerPassword) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        customerService.updatePassword(customerId, customerPassword);
        return Result.success();
    }

    @PostMapping("getProducts")
    @Audit
    public Result getProducts(Auth auth, @RequestBody CustomerPassword customerPassword) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        customerService.updatePassword(customerId, customerPassword);
        return Result.success();
    }

    @PostMapping("getProduct")
    @Audit
    public Result getProduct(Auth auth, @RequestBody CustomerPassword customerPassword) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        customerService.updatePassword(customerId, customerPassword);
        return Result.success();
    }

    @PostMapping("sendVerificationCode")
    @Audit
    public Result sendMail(Auth auth, @RequestBody VerificationMail verificationMail) throws MessagingException {
        mailService.sendVerificationCode(verificationMail.getTo());
        return Result.success();
    }

    @PostMapping("customerLicenses")
    @Audit
    public Result<List<CustomerLicense>> getCustomerLicenses() {
        List<CustomerLicense> customerLicenseList = customerService.getCustomerLicenseList();
        return Result.success(customerLicenseList);
    }


    @PostMapping("admin")
    @Audit
    public Result admin(Auth auth ) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        Operator operator = operatorService.getOperator(auth, customerId);
        refreshLoginOperation(customerId, operator);
        if(operator != null) {
            refreshLoginOperation(customerId, operator);
            return Result.success();
        } else {
            return Result.error(SystemError.OPERATOR_OPERATOR_NOT_FOUND.newException());
        }
    }


    @PostMapping("properties")
    @Audit
    public Result<Properties> properties(Auth auth ) {
        Properties properties = new Properties();
        properties.setProperty("enable-mail-validation", String.valueOf(enableMailValidation));
        return Result.success(properties);
    }

    protected void refreshLoginOperation(Long customerId, Operator operator) {
        String key = SystemConstants.OPERATOR_PREFIX + operator.getCustomerId();
        systemRedisTemplate.opsForValue().set(key, operator, Duration.ofSeconds(tokenTimeout) );
    }
}
