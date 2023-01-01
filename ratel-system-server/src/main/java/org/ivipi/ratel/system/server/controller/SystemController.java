package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerLicense;
import org.ivipi.ratel.system.common.model.CustomerPage;
import org.ivipi.ratel.system.domain.entity.CustomerDo;
import org.ivipi.ratel.system.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class SystemController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("register")
    public Result register(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return Result.success();
    }


    @PostMapping("login")
    public Result login(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return Result.success();
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
}
