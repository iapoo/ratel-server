package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.system.domain.dto.LicensePageDto;
import org.ivipi.ratel.system.domain.entity.LicenseDo;
import org.ivipi.ratel.system.domain.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @PostMapping("list")
    public Page<LicenseDo> getLicenses() {
        Page<LicenseDo> licenses = licenseService.getPage(1, 10);
        return licenses;
    }

    @PostMapping("licenses")
    public Page<LicensePageDto> getLicensePage() {
        Page<LicensePageDto> licenses = licenseService.getLicensePage(1, 10);
        return licenses;
    }
}
