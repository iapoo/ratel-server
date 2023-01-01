package org.ivipi.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerLicense;
import org.ivipi.ratel.system.common.model.CustomerPage;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.domain.mapper.CustomerMapper;
import org.ivipi.ratel.system.domain.entity.CustomerDo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerService extends ServiceImpl<CustomerMapper, CustomerDo> {


    public Page<CustomerDo> getPage(int pageNum, int pageSize) {
        Page<CustomerDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        Page<CustomerDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }


    public Page<CustomerPage> getCustomerPage(int pageNum, int pageSize) {
        Page<CustomerPage> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CustomerPage> queryWrapper = new QueryWrapper<>();
        List<CustomerPage> result = baseMapper.getCustomerPage(page);
        return page.setRecords(result);
    }


    public Customer getCustomer(String customerName) {
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_name", customerName);
        CustomerDo customerDo = this.getOne(queryWrapper);
        if(customerDo != null) {
            Customer customer = convertCustomerDo(customerDo);
            return customer;
        } else {
            return null;
        }
    }

    public List<CustomerLicense> getCustomerLicenseList() {
        List<CustomerLicense> result;

        result = baseMapper.getCustomerLicenseList();

        return result;
    }

    public void addCustomer(Customer customer) {
        CustomerDo customerDo;

        if(customer.getCustomerId() != null) {
            throw SystemError.CUSTOMER_CUSTOMER_ID_IS_NOT_NULL.newException();
        }
        customerDo = new CustomerDo();
        customerDo.setCustomerName(customer.getCustomerName());
        customerDo.setPassword(customer.getPassword());
        customerDo.setIdCard(customer.getIdCard());

        save(customerDo);
    }

    public void updateCustomer(Customer customer) {
        CustomerDo customerDo;

        if(customer.getCustomerId() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_ID_IS_NULL.newException();
        }
        customerDo = new CustomerDo();
        customerDo.setCustomerId(customer.getCustomerId());
        customerDo.setPassword(customer.getPassword());
        customerDo.setCustomerName(customer.getCustomerName());
        customerDo.setIdCard(customer.getIdCard());

        updateById(customerDo);
    }

    private Customer convertCustomerDo(CustomerDo customerDo) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDo, customer);
        return customer;
    }
}
