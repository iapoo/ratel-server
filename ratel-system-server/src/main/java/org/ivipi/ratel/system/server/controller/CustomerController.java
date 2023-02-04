package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.annoation.Log;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerLicense;
import org.ivipi.ratel.system.common.model.CustomerPage;
import org.ivipi.ratel.system.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //@PostMapping("list")
    //public Result<Page<CustomerDo>> getCustomers() {
    //    Page<CustomerDo> customers = customerService.getPage(1, 10);
    //    return Result.success(customers);
    //}

    @PostMapping("customers")
    @Log
    public Result<Page<Customer>> getCustomers(@RequestBody CustomerPage customerPage) {
        Page<Customer> customers = customerService.getCustomers(customerPage);
        return Result.success(customers);
    }

    @PostMapping("customerLicenses")
    @Log
    public Result<List<CustomerLicense>> getCustomerLicenses() {
        List<CustomerLicense> customerLicenseList = customerService.getCustomerLicenseList();
        return Result.success(customerLicenseList);
    }

}
