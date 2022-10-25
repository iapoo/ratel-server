package org.ivipi.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.domain.dto.CustomerPageDto;
import org.ivipi.ratel.system.domain.mapper.CustomerMapper;
import org.ivipi.ratel.system.domain.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

    @Autowired
    private CustomerMapper customerMapper;

    public Page<Customer> getPage(int pageNum, int pageSize) {
        Page<Customer> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        Page<Customer> result = customerMapper.selectPage(page, queryWrapper);
        return result;
    }


    public Page<CustomerPageDto> getCustomerPage(int pageNum, int pageSize) {
        Page<CustomerPageDto> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CustomerPageDto> queryWrapper = new QueryWrapper<>();
        List<CustomerPageDto> result = customerMapper.getCustomerPage(page);
        return page.setRecords(result);
    }
}
