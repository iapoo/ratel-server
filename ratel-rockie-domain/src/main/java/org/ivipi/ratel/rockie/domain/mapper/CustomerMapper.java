package org.ivipi.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ivipi.ratel.rockie.common.model.CustomerPage;
import org.ivipi.ratel.rockie.domain.entity.CustomerDo;

import java.util.List;

public interface CustomerMapper extends BaseMapper<CustomerDo> {
    List<CustomerPage> getCustomerPage(IPage<CustomerPage> page);
}
