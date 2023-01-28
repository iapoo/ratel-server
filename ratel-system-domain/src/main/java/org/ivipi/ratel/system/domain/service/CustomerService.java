package org.ivipi.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerEdit;
import org.ivipi.ratel.system.common.model.CustomerLicense;
import org.ivipi.ratel.system.common.model.CustomerPage;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.common.utils.SystemUtils;
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

    public CustomerDo getCustomerDo(String customerName) {
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_name", customerName);
        CustomerDo customerDo = this.getOne(queryWrapper);
        return customerDo;
    }

    public Customer getCustomer(String customerName, String customerPassword) {
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_name", customerName);
        queryWrapper.eq("password", customerPassword);
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

    public void addCustomer(CustomerAdd customerAdd) {
        CustomerDo customerDo;

        String password = customerAdd.getPassword();
        if(customerAdd.getCustomerName() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NAME_IS_NULL.newException();
        }
        if(!SystemUtils.IsValidPassword(password)) {
            throw SystemError.CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID.newException();
        }
        customerDo = convertCustomerAdd(customerAdd);

        save(customerDo);
    }

    public void updateCustomer(CustomerEdit customerEdit) {
        if(customerEdit.getCustomerId() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_ID_IS_NULL.newException();
        }
        CustomerDo oldCustomerDo = getById(customerEdit.getCustomerId());
        if(oldCustomerDo == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }
        CustomerDo customerDo = convertCustomerEdit(customerEdit, oldCustomerDo);
        updateById(customerDo);
    }

    public void updatePassword(String userName, CustomerPassword customerPassword) {
        CustomerDo oldCustomerDo = getCustomerDo(userName);
        if(oldCustomerDo == null) {
            throw SystemError.CUSTOMER_CUSTOMER_IS_INVALID.newException();
        }
        if(!oldCustomerDo.getPassword().equals(customerPassword.getOldPassword())) {
            throw SystemError.CUSTOMER_CUSTOMER_PASSWORD_IS_INCORRECT.newException();
        }
        if(!SystemUtils.IsValidPassword(customerPassword.getNewPassword())) {
            throw SystemError.CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID.newException();
        }
        oldCustomerDo.setPassword(customerPassword.getNewPassword());
        updateById(oldCustomerDo);
    }

    private Customer convertCustomerDo(CustomerDo customerDo) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDo, customer);
        return customer;
    }

    private CustomerDo convertCustomerAdd(CustomerAdd customerAdd) {
        CustomerDo customerDo = new CustomerDo();
        BeanUtils.copyProperties(customerAdd, customerDo);
        return customerDo;
    }

    private CustomerDo convertCustomerEdit(CustomerEdit customerEdit, CustomerDo customerDo) {
        BeanUtils.copyProperties(customerEdit, customerDo);
        return customerDo;
    }
}
