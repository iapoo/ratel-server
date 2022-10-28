package org.ivipi.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.rockie.domain.dto.CustomerPageDto;
import org.ivipi.ratel.rockie.domain.entity.Customer;
import org.ivipi.ratel.rockie.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class RockieController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("list")
    public Page<Customer> getCustomers() {
        Page<Customer> customers = customerService.getPage(1, 10);
        return customers;
    }

    @PostMapping("customers")
    public Page<CustomerPageDto> getCustomerPage() {
        Page<CustomerPageDto> customers = customerService.getCustomerPage(1, 10);
        return customers;
    }
}
