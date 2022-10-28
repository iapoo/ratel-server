package org.ivipi.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.domain.dto.LicensePageDto;
import org.ivipi.ratel.system.domain.entity.LicenseDo;
import org.ivipi.ratel.system.domain.mapper.LicenseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LicenseService extends ServiceImpl<LicenseMapper, LicenseDo> {

    @Autowired
    private LicenseMapper licenseMapper;

    public Page<LicenseDo> getPage(int pageNum, int pageSize) {
        Page<LicenseDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LicenseDo> queryWrapper = new QueryWrapper<>();
        Page<LicenseDo> result = licenseMapper.selectPage(page, queryWrapper);
        return result;
    }


    public Page<LicensePageDto> getLicensePage(int pageNum, int pageSize) {
        Page<LicensePageDto> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LicensePageDto> queryWrapper = new QueryWrapper<>();
        List<LicensePageDto> result = licenseMapper.getLicensePage(page);
        return page.setRecords(result);
    }
}
