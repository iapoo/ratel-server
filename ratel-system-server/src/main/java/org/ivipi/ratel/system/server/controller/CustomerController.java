package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.system.domain.dto.CustomerPageDto;
import org.ivipi.ratel.system.domain.entity.CustomerDo;
import org.ivipi.ratel.system.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("list")
    public Page<CustomerDo> getCustomers() {
        Page<CustomerDo> customers = customerService.getPage(1, 10);
        return customers;
    }

    @PostMapping("customers")
    public Page<CustomerPageDto> getCustomerPage() {
        Page<CustomerPageDto> customers = customerService.getCustomerPage(1, 10);
        return customers;
    }
}
