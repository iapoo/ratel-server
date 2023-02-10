package org.ivipi.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.CustomerLicense;
import org.ivipi.ratel.system.domain.entity.CustomerDo;

import java.util.List;

public interface CustomerMapper extends BaseMapper<CustomerDo> {
    List<Customer> getCustomers(IPage<Customer> page, @Param("customerName")String customerName);
    List<CustomerLicense> getCustomerLicenseList();
}
