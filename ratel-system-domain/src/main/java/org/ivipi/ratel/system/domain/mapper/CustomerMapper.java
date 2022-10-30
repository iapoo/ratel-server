package org.ivipi.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ivipi.ratel.system.common.model.CustomerLicense;
import org.ivipi.ratel.system.common.model.CustomerPage;
import org.ivipi.ratel.system.domain.entity.CustomerDo;

import java.util.List;

public interface CustomerMapper extends BaseMapper<CustomerDo> {
    List<CustomerPage> getCustomerPage(IPage<CustomerPage> page);
    List<CustomerLicense> getCustomerLicenseList();
}
