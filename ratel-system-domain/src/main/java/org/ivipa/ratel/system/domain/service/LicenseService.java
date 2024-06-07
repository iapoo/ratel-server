package org.ivipa.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.common.model.Customer;
import org.ivipa.ratel.system.common.model.License;
import org.ivipa.ratel.system.common.model.LicenseAdd;
import org.ivipa.ratel.system.common.model.Order;
import org.ivipa.ratel.system.common.model.LicensePage;
import org.ivipa.ratel.system.common.model.LicenseUpdate;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.ivipa.ratel.system.domain.entity.CustomerDo;
import org.ivipa.ratel.system.domain.entity.LicenseDo;
import org.ivipa.ratel.system.domain.entity.ProductDo;
import org.ivipa.ratel.system.domain.mapper.LicenseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LicenseService extends ServiceImpl<LicenseMapper, LicenseDo> {

    @Autowired
    private ProductService productService;

    public Page<LicenseDo> getPage(int pageNum, int pageSize) {
        Page<LicenseDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<LicenseDo> queryWrapper = new QueryWrapper<>();
        Page<LicenseDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }


    public Page<License> getLicenses(Auth auth, LicensePage licensePage) {
        Page<License> page = new Page<>(licensePage.getPageNum(), licensePage.getPageSize());
        List<License> result = baseMapper.getLicenses(page, auth.getOnlineCustomer().getCustomerId());
        return page.setRecords(result);
    }

    public void addLicense(Auth auth, LicenseAdd licenseAdd) {
        LicenseDo licenseDo = convertLicenseAdd(licenseAdd);
        licenseDo.setLicenseId(null);
        licenseDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
        licenseDo.setCreatedDate(LocalDateTime.now());
        licenseDo.setUpdatedDate(LocalDateTime.now());
        save(licenseDo);
    }

    public void subscribe(Auth auth, Order order) {
        ProductDo product = productService.getById(order.getProductId());
        if(product == null) {
            throw SystemError.PRODUCT_PRODUCT_NOT_FOUND.newException();
        }
        LicenseDo licenseDo = convertLicenseBuy(order);
        licenseDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
        save(licenseDo);
    }

    public void renewLicense(Auth auth, Order order) {
        LicenseDo licenseDo = convertLicenseBuy(order);
        licenseDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
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
        licenseDo.setUpdatedDate(LocalDateTime.now());
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

    private LicenseDo convertLicenseBuy(Order order) {
        LicenseDo licenseDo = new LicenseDo();
        BeanUtils.copyProperties(order, licenseDo);
        return licenseDo;
    }

    private LicenseDo convertLicenseEdit(LicenseUpdate licenseUpdate, LicenseDo oldLicenseDo) {
        BeanUtils.copyProperties(licenseUpdate, oldLicenseDo);
        return oldLicenseDo;
    }
}
