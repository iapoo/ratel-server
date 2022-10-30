package org.ivipi.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.rockie.common.model.CustomerPage;
import org.ivipi.ratel.rockie.domain.entity.CustomerDo;
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
    public Page<CustomerDo> getCustomers() {
        Page<CustomerDo> customers = customerService.getPage(1, 10);
        return customers;
    }

    @PostMapping("customers")
    public Page<CustomerPage> getCustomerPage() {
        Page<CustomerPage> customers = customerService.getCustomerPage(1, 10);
        return customers;
    }
}
