package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.domain.entity.LicenseDo;
import org.ivipi.ratel.system.domain.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("license")
public class LicenseController extends SystemGenericController {

    @Autowired
    private LicenseService licenseService;

    @PostMapping("list")
    public Page<LicenseDo> getLicenses() {
        Page<LicenseDo> licenses = licenseService.getPage(1, 10);
        return licenses;
    }

    @PostMapping("licenses")
    public Page<LicensePage> getLicensePage() {
        Page<LicensePage> licenses = licenseService.getLicensePage(1, 10);
        return licenses;
    }

    @PostMapping("add")
    public Result addLicense(@RequestBody License license) {
        boolean isLoggedIn = isLoggedIn();
        if(isLoggedIn) {
            licenseService.addLicense(license);
            return Result.success();
        }
        return Result.error(SystemError.SYSTEM_TOKEN_NOT_FOUND.newException());
    }

    @PostMapping("update")
    public Result updateLicense(@RequestBody License license) {
        boolean isLoggedIn = isLoggedIn();
        if(isLoggedIn) {
            licenseService.updateLicense(license);
            return Result.success();
        }
        return Result.error(SystemError.SYSTEM_TOKEN_NOT_FOUND.newException());
    }
}
