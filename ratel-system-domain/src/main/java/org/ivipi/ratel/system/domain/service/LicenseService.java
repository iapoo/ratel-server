package org.ivipi.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.domain.entity.LicenseDo;
import org.ivipi.ratel.system.domain.entity.LicenseDo;
import org.ivipi.ratel.system.domain.mapper.LicenseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LicenseService extends ServiceImpl<LicenseMapper, LicenseDo> {

    public Page<LicenseDo> getPage(int pageNum, int pageSize) {
        Page<LicenseDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LicenseDo> queryWrapper = new QueryWrapper<>();
        Page<LicenseDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }


    public Page<LicensePage> getLicensePage(int pageNum, int pageSize) {
        Page<LicensePage> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LicensePage> queryWrapper = new QueryWrapper<>();
        List<LicensePage> result = baseMapper.getLicensePage(page);
        return page.setRecords(result);
    }

    public void addLicense(License license) {
        LicenseDo licenseDo = convertLicense(license);
        licenseDo.setLicenseId(null);
        save(licenseDo);
    }

    public void updateLicense(License license) {
        LicenseDo licenseDo = convertLicense(license);
        LicenseDo oldLicenseDo = getById(license.getLicenseId());
        updateById(licenseDo);
    }

    private License convertLicenseDo(LicenseDo licenseDo) {
        License license = new License();
        BeanUtils.copyProperties(licenseDo, license);
        return license;
    }

    private LicenseDo convertLicense(License license) {
        LicenseDo licenseDo = new LicenseDo();
        BeanUtils.copyProperties(license, licenseDo);
        return licenseDo;
    }
}
