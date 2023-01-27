package org.ivipi.ratel.system.server.controller;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.model.Login;
import org.ivipi.ratel.system.common.model.UserToken;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    public Result register(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return Result.success();
    }


    @PostMapping("login")
    public Result login(@RequestBody Login login) {
        Customer customer = customerService.getCustomer(login.getName(), login.getPassword());
        if(customer != null) {
            String token = IdUtil.simpleUUID();

            UserToken userToken = new UserToken();
            userToken.setToken(token);
            putSession(LOGIN_USER_NAME, login.getName());
            putSession(LOGIN_USER_TOKEN, token);
            refreshUserToken(token, login.getName());
            return Result.success(userToken);
        } else {
            return Result.error(SystemError.SYSTEM_LOGIN_FAILED.newException());
        }

    }

    @PostMapping("logout")
    public Result logout(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return Result.success();
    }

    @PostMapping("update")
    public Result updateCustomer(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
        return Result.success();
    }

    @PostMapping("updatePassword")
    public Result updateCustomerPassword(@RequestBody CustomerPassword customerPassword) {
        String userName = getUserName();
        customerService.updatePassword(userName, customerPassword);
        return Result.success();
    }
}
