package org.ivipa.ratel.system.domain.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.common.model.Customer;
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerDelete;
import org.ivipa.ratel.system.common.model.CustomerInfo;
import org.ivipa.ratel.system.common.model.CustomerOperatorPage;
import org.ivipa.ratel.system.common.model.CustomerQuery;
import org.ivipa.ratel.system.common.model.CustomerSettings;
import org.ivipa.ratel.system.common.model.CustomerUpdate;
import org.ivipa.ratel.system.common.model.CustomerLicense;
import org.ivipa.ratel.system.common.model.CustomerPage;
import org.ivipa.ratel.system.common.model.CustomerPassword;
import org.ivipa.ratel.system.common.model.CustomerUpdateEx;
import org.ivipa.ratel.system.common.utils.SystemConstants;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.ivipa.ratel.system.common.utils.SystemUtils;
import org.ivipa.ratel.system.domain.mapper.CustomerMapper;
import org.ivipa.ratel.system.domain.entity.CustomerDo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Be noted: Methods with "Ex" will be called by operators and require permissions
 */
@Service
@Slf4j
public class CustomerService extends ServiceImpl<CustomerMapper, CustomerDo> {


    public Page<CustomerDo> getPage(int pageNum, int pageSize) {
        Page<CustomerDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        Page<CustomerDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }

    public Page<Customer> getCustomersEx(Auth auth, CustomerPage customerPage) {
        if(auth.getOperator() == null) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(auth.getOperator().getOperatorType() <= SystemConstants.OPERATOR_TYPE_CUSTOMER_OPERATION) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        Page<Customer> page = new Page<>(customerPage.getPageNum(), customerPage.getPageSize());
        List<Customer> result = baseMapper.getCustomers(page, customerPage.getCustomerName());
        return page.setRecords(result);
    }

    public Page<Customer> getOperatorCustomersEx(Auth auth, CustomerOperatorPage customerOperatorPage) {
        if(auth.getOperator() == null) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(auth.getOperator().getOperatorType() <= SystemConstants.OPERATOR_TYPE_CUSTOMER_OPERATION) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        Page<Customer> page = new Page<>(customerOperatorPage.getPageNum(), customerOperatorPage.getPageSize());
        List<Customer> result = baseMapper.getOperatorCustomers(page, customerOperatorPage.getLike(), customerOperatorPage.getExcludedOperatorId());
        return page.setRecords(result);
    }

    public Customer getCustomerEx(Auth auth, CustomerQuery customerQuery) {
        if(auth.getOperator() == null) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(auth.getOperator().getOperatorType() <= SystemConstants.OPERATOR_TYPE_CUSTOMER_OPERATION) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        CustomerDo customerDo = getById(customerQuery.getCustomerId());
        if (customerDo == null || customerDo.getDeleted()) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }
        Customer customer = convertCustomerDo(customerDo);
        return customer;
    }

    public Customer getCustomer(Long customerId) {
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        queryWrapper.eq("deleted", false);
        CustomerDo customerDo = this.getOne(queryWrapper);
        if(customerDo != null) {
            Customer customer = convertCustomerDo(customerDo);
            return customer;
        } else {
            return null;
        }
    }

    public Customer getCustomerByCustomerName(String customerName) {
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
        queryWrapper.eq("deleted", 0);
        CustomerDo customerDo = this.getOne(queryWrapper);
        return customerDo;
    }

    public CustomerDo getCustomerDoByEmail(String email) {
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.eq("deleted", 0);
        CustomerDo customerDo = this.getOne(queryWrapper);
        return customerDo;
    }

    public CustomerDo getCustomerDo(String customerName, Long excludeCustomerId) {
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_name", customerName);
        queryWrapper.ne("customer_id", excludeCustomerId);
        CustomerDo customerDo = this.getOne(queryWrapper);
        return customerDo;
    }

    public CustomerDo getCustomerDoByEmail(String email, Long excludeCustomerId) {
        QueryWrapper<CustomerDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.ne("customer_id", excludeCustomerId);
        CustomerDo customerDo = this.getOne(queryWrapper);
        return customerDo;
    }

    public CustomerInfo getCustomerInfo(Auth auth) {
        CustomerDo customerDo = getById(auth.getOnlineCustomer().getCustomerId());
        CustomerInfo customerInfo = convertCustomerDoToCustomerInfo(customerDo);
        return customerInfo;
    }


    public CustomerSettings getCustomerSettings(Auth auth) {
        CustomerDo customerDo = getById(auth.getOnlineCustomer().getCustomerId());
        CustomerSettings customerSettings = convertCustomerDoToCustomerSettings(customerDo);
        return customerSettings;
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

    public Customer addCustomer(CustomerAdd customerAdd) {
        CustomerDo customerDo;

        String password = customerAdd.getPassword();
        if(customerAdd.getCustomerName() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NAME_IS_NULL.newException();
        }
        if(!SystemUtils.IsValidPassword(password)) {
            throw SystemError.CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID.newException();
        }
        CustomerDo oldCustomerDo = getCustomerDo(customerAdd.getCustomerName());
        if(oldCustomerDo != null) {
            throw SystemError.CUSTOMER_CUSTOMER_NAME_EXISTS.newException();
        }
        oldCustomerDo = getCustomerDoByEmail(customerAdd.getEmail());
        if(oldCustomerDo != null) {
            throw SystemError.CUSTOMER_EMAIL_EXISTS.newException();
        }
        customerDo = convertCustomerAdd(customerAdd);
        customerDo.setCustomerCode(IdUtil.simpleUUID());
        customerDo.setCreatedDate(LocalDateTime.now());
        customerDo.setUpdatedDate(LocalDateTime.now());
        save(customerDo);
        Customer customer = convertCustomerDo(customerDo);
        return customer;
    }

    /**
     * Add customer by operator
     * @param customerAdd
     * @return
     */
    public Customer addCustomerEx(Auth auth, CustomerAdd customerAdd) {
        CustomerDo customerDo;

        String password = customerAdd.getPassword();
        if(auth.getOperator() == null) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(auth.getOperator().getOperatorType() <= SystemConstants.OPERATOR_TYPE_CUSTOMER_OPERATION) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(customerAdd.getCustomerName() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NAME_IS_NULL.newException();
        }
        if(!SystemUtils.IsValidPassword(password)) {
            throw SystemError.CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID.newException();
        }
        CustomerDo oldCustomerDo = getCustomerDo(customerAdd.getCustomerName());
        if(oldCustomerDo != null) {
            throw SystemError.CUSTOMER_CUSTOMER_NAME_EXISTS.newException();
        }
        oldCustomerDo = getCustomerDoByEmail(customerAdd.getEmail());
        if(oldCustomerDo != null) {
            throw SystemError.CUSTOMER_EMAIL_EXISTS.newException();
        }
        customerDo = convertCustomerAdd(customerAdd);
        customerDo.setCustomerCode(IdUtil.simpleUUID());
        customerDo.setCreatedDate(LocalDateTime.now());
        customerDo.setUpdatedDate(LocalDateTime.now());
        save(customerDo);
        Customer customer = convertCustomerDo(customerDo);
        return customer;
    }

    /**
     * Operator update customer
     * @param customerUpdateEx
     */
    public void updateCustomerEx(Auth auth, CustomerUpdateEx customerUpdateEx) {
        if(auth.getOperator() == null) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(auth.getOperator().getOperatorType() <= SystemConstants.OPERATOR_TYPE_CUSTOMER_OPERATION) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(customerUpdateEx.getCustomerId() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }
        if(customerUpdateEx.getCustomerName() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NAME_IS_NULL.newException();
        }
        if(customerUpdateEx.getEmail() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_EMAIL_IS_NULL.newException();
        }
        CustomerDo oldCustomerDo = getById(customerUpdateEx.getCustomerId());
        if(oldCustomerDo == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }
        String password = customerUpdateEx.getPassword();
        if(!SystemUtils.IsValidPassword(password)) {
            throw SystemError.CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID.newException();
        }
        CustomerDo oldCustomerDoEx = getCustomerDo(customerUpdateEx.getCustomerName(), customerUpdateEx.getCustomerId());
        if(oldCustomerDoEx != null) {
            throw SystemError.CUSTOMER_CUSTOMER_NAME_EXISTS.newException();
        }
        oldCustomerDoEx = getCustomerDoByEmail(customerUpdateEx.getEmail(), customerUpdateEx.getCustomerId());
        if(oldCustomerDoEx != null) {
            throw SystemError.CUSTOMER_EMAIL_EXISTS.newException();
        }
        CustomerDo customerDo = convertCustomerUpdateEx(customerUpdateEx, oldCustomerDo);
        customerDo.setUpdatedDate(LocalDateTime.now());
        updateById(customerDo);
    }
    /**
     * Operator update customer
     * @param customerUpdate
     */
    public void updateCustomer(CustomerUpdate customerUpdate) {
        CustomerDo oldCustomerDo = getById(customerUpdate.getCustomerId());
        if(oldCustomerDo == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }
        CustomerDo customerDo = convertCustomerUpdate(customerUpdate, oldCustomerDo);
        customerDo.setUpdatedDate(LocalDateTime.now());
        updateById(customerDo);
    }

    /**
     * Customer update this
     * @param auth
     * @param customerUpdate
     */
    public void updateCustomer(Auth auth, CustomerUpdate customerUpdate) {
        CustomerDo oldCustomerDo = getById(auth.getOnlineCustomer().getCustomerId());
        if(oldCustomerDo == null) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }
        CustomerDo customerDo = convertCustomerUpdate(customerUpdate, oldCustomerDo);
        customerDo.setUpdatedDate(LocalDateTime.now());
        updateById(customerDo);
    }

    public void deleteCustomer(CustomerDelete customerDelete) {
        if (customerDelete.getCustomerId() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_IS_INVALID.newException();
        }
        CustomerDo oldCustomerDo = getById(customerDelete.getCustomerId());
        if (oldCustomerDo == null || oldCustomerDo.getDeleted()) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }

        CustomerDo customerDo = oldCustomerDo;
        customerDo.setDeleted(true);
        updateById(customerDo);
    }

    /**
     * Delete customer by operator.
     * @param customerDelete
     */
    public void deleteCustomerEx(Auth auth, CustomerDelete customerDelete) {
        if(auth.getOperator() == null) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(auth.getOperator().getOperatorType() <= SystemConstants.OPERATOR_TYPE_CUSTOMER_OPERATION) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if (customerDelete.getCustomerId() == null) {
            throw SystemError.CUSTOMER_CUSTOMER_IS_INVALID.newException();
        }
        CustomerDo oldCustomerDo = getById(customerDelete.getCustomerId());
        if (oldCustomerDo == null || oldCustomerDo.getDeleted()) {
            throw SystemError.CUSTOMER_CUSTOMER_NOT_FOUND.newException();
        }

        CustomerDo customerDo = oldCustomerDo;
        customerDo.setDeleted(true);
        updateById(customerDo);
    }

    public void updatePassword(Long customerId, CustomerPassword customerPassword) {
        CustomerDo oldCustomerDo = getById(customerId);
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
        oldCustomerDo.setUpdatedDate(LocalDateTime.now());
        updateById(oldCustomerDo);
    }

    public void updateSettings(Long customerId, CustomerSettings customerSettings) {
        CustomerDo oldCustomerDo = getById(customerId);
        if(oldCustomerDo == null) {
            throw SystemError.CUSTOMER_CUSTOMER_IS_INVALID.newException();
        }
        oldCustomerDo.setSettings(customerSettings.getSettings());
        oldCustomerDo.setUpdatedDate(LocalDateTime.now());
        updateById(oldCustomerDo);
    }

    private Customer convertCustomerDo(CustomerDo customerDo) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDo, customer);
        return customer;
    }


    private CustomerInfo convertCustomerDoToCustomerInfo(CustomerDo customerDo) {
        CustomerInfo customerInfo = new CustomerInfo();
        BeanUtils.copyProperties(customerDo, customerInfo);
        return customerInfo;
    }


    private CustomerSettings convertCustomerDoToCustomerSettings(CustomerDo customerDo) {
        CustomerSettings customerSettings = new CustomerSettings();
        BeanUtils.copyProperties(customerDo, customerSettings);
        return customerSettings;
    }

    private CustomerDo convertCustomerAdd(CustomerAdd customerAdd) {
        CustomerDo customerDo = new CustomerDo();
        BeanUtils.copyProperties(customerAdd, customerDo);
        return customerDo;
    }

    private CustomerDo convertCustomerUpdate(CustomerUpdate customerUpdate, CustomerDo customerDo) {
        BeanUtils.copyProperties(customerUpdate, customerDo);
        return customerDo;
    }

    private CustomerDo convertCustomerUpdateEx(CustomerUpdateEx customerUpdateEx, CustomerDo customerDo) {
        BeanUtils.copyProperties(customerUpdateEx, customerDo);
        return customerDo;
    }
}
