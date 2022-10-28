package org.ivipi.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ivipi.ratel.system.domain.dto.CustomerPageDto;
import org.ivipi.ratel.system.domain.entity.CustomerDo;

import java.util.List;

public interface CustomerMapper extends BaseMapper<CustomerDo> {
    List<CustomerPageDto> getCustomerPage(IPage<CustomerPageDto> page);
}
