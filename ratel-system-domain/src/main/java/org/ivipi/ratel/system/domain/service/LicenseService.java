package org.ivipi.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicenseAdd;
import org.ivipi.ratel.system.common.model.LicenseUpdate;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.domain.entity.LicenseDo;
import org.ivipi.ratel.system.domain.mapper.LicenseMapper;
import org.springframework.beans.BeanUtils;
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


    public Page<License> getLicenses(Auth auth, LicensePage licensePage) {
        Page<License> page = new Page<>(licensePage.getPageNum(), licensePage.getPageSize());
        List<License> result = baseMapper.getLicenses(page);
        return page.setRecords(result);
    }

    public void addLicense(Auth auth, LicenseAdd licenseAdd) {
        LicenseDo licenseDo = convertLicenseAdd(licenseAdd);
        licenseDo.setLicenseId(null);
        save(licenseDo);
    }

    public void updateLicense(Auth auth, LicenseUpdate licenseUpdate) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        if (!customerId.equals(licenseUpdate.getCustomerId())) {
            throw SystemError.SYSTEM_ID_IS_INVALID.newException();
        }
        if (licenseUpdate.getLicenseId() == null) {
            throw SystemError.LICENSE_LICENSE_ID_IS_NULL.newException();
        }
        if(licenseUpdate.getProductId() == null) {
            throw SystemError.LICENSE_PRODUCT_ID_IS_NULL.newException();
        }
        LicenseDo oldLicenseDo = getById(licenseUpdate.getLicenseId());
        if(oldLicenseDo == null) {
            throw  SystemError.LICENSE_LICENSE_NOT_FOUND.newException();
        }
        LicenseDo licenseDo = convertLicenseEdit(licenseUpdate, oldLicenseDo);
        updateById(licenseDo);
    }

    private License convertLicenseDo(LicenseDo licenseDo) {
        License license = new License();
        BeanUtils.copyProperties(licenseDo, license);
        return license;
    }

    private LicenseDo convertLicenseAdd(LicenseAdd license) {
        LicenseDo licenseDo = new LicenseDo();
        BeanUtils.copyProperties(license, licenseDo);
        return licenseDo;
    }

    private LicenseDo convertLicenseEdit(LicenseUpdate licenseUpdate, LicenseDo oldLicenseDo) {
        BeanUtils.copyProperties(licenseUpdate, oldLicenseDo);
        return oldLicenseDo;
    }
}
