package org.ivipi.ratel.system.server.controller;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.annoation.Audit;
import org.ivipi.ratel.system.common.controller.GenericController;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerUpdate;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.model.Login;
import org.ivipi.ratel.system.common.model.OnlineCustomer;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class SystemController extends GenericController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("register")
    @Audit
    public Result register(@RequestBody CustomerAdd customerAdd) {
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
            onlineCustomer.setCustomerId(1L);
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
    public Result updateCustomer(Auth auth, @RequestBody CustomerUpdate customerUpdate) {
       customerService.updateCustomer(auth, customerUpdate);
        return Result.success();
    }

    @PostMapping("updatePassword")
    @Audit
    public Result updateCustomerPassword(Auth auth, @RequestBody CustomerPassword customerPassword) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        customerService.updatePassword(customerId, customerPassword);
        return Result.success();
    }
}
