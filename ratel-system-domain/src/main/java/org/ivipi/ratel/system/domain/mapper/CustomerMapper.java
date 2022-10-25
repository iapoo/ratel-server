package org.ivipi.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipi.ratel.system.domain.dto.CustomerPageDto;
import org.ivipi.ratel.system.domain.model.Customer;

import java.util.List;

public interface CustomerMapper extends BaseMapper<Customer> {
    List<CustomerPageDto> getCustomerPage(IPage<CustomerPageDto> page);
}
