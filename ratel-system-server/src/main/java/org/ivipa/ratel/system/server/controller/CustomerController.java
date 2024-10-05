package org.ivipa.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.common.model.Customer;
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerDelete;
import org.ivipa.ratel.system.common.model.CustomerPage;
import org.ivipa.ratel.system.common.model.CustomerQuery;
import org.ivipa.ratel.system.common.model.CustomerUpdate;
import org.ivipa.ratel.system.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("customers")
    @Audit
    public Result<Page<Customer>> getCustomers(@RequestBody CustomerPage customerPage) {
        Page<Customer> customers = customerService.getCustomers(customerPage);
        return Result.success(customers);
    }

    @PostMapping("customer")
    @Audit
    public Result<Customer> getCustomer(Auth auth, @RequestBody CustomerQuery customerQuery) {
        Customer customer = customerService.getCustomer(customerQuery);
        return Result.success(customer);
    }

    @PostMapping("add")
    @Audit
    public Result<Customer> addCustomer(Auth auth, @RequestBody CustomerAdd customerAdd) {
        Customer customer = customerService.addCustomer(customerAdd);
        return Result.success(customer);
    }

    @PostMapping("update")
    @Audit
    public Result updateCustomer(Auth auth, @RequestBody CustomerUpdate customerUpdate) {
        customerService.updateCustomer(customerUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteCustomers(Auth auth, @RequestBody CustomerDelete customerDelete) {
        customerService.deleteCustomer(customerDelete);
        return Result.success();
    }

}
