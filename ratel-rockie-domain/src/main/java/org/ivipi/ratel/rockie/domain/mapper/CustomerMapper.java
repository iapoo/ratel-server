package org.ivipi.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ivipi.ratel.rockie.domain.dto.CustomerPageDto;
import org.ivipi.ratel.rockie.domain.entity.Customer;

import java.util.List;

public interface CustomerMapper extends BaseMapper<Customer> {
    List<CustomerPageDto> getCustomerPage(IPage<CustomerPageDto> page);
}
