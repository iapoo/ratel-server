package org.ivipi.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.domain.entity.LicenseDo;

import java.util.List;

public interface LicenseMapper extends BaseMapper<LicenseDo> {
    List<License> getLicenses(IPage<License> page);
}
