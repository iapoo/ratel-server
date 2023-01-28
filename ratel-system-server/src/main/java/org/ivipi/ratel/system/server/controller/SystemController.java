package org.ivipi.ratel.system.server.controller;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerEdit;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.model.Login;
import org.ivipi.ratel.system.common.model.LoginCustomer;
import org.ivipi.ratel.system.common.model.UserToken;
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
public class SystemController extends SystemGenericController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("register")
    public Result register(@RequestBody CustomerAdd customerAdd) {
        customerService.addCustomer(customerAdd);
        return Result.success();
    }


    @PostMapping("login")
    public Result login(@RequestBody Login login) {
        Customer customer = customerService.getCustomer(login.getName(), login.getPassword());
        if(customer != null) {
            String token = IdUtil.simpleUUID();
            LoginCustomer loginCustomer = new LoginCustomer();
            loginCustomer.setName(login.getName());
            UserToken userToken = new UserToken();
            userToken.setToken(token);
            refreshUserToken(token, loginCustomer);
            return Result.success(userToken);
        } else {
            return Result.error(SystemError.SYSTEM_LOGIN_FAILED.newException());
        }
    }

    @PostMapping("logout")
    public Result logout() {
        String userToken = getUserToken();
        if(userToken != null) {
            removeUserToken(userToken);
        } else {
            Result.error(SystemError.SYSTEM_TOKEN_NOT_FOUND.newException());
        }
        return Result.success();
    }

    @PostMapping("update")
    public Result updateCustomer(@RequestBody CustomerEdit customerEdit) {
        customerService.updateCustomer(customerEdit);
        return Result.success();
    }

    @PostMapping("updatePassword")
    public Result updateCustomerPassword(@RequestBody CustomerPassword customerPassword) {
        String userName = getUserName();
        customerService.updatePassword(userName, customerPassword);
        return Result.success();
    }
}
