package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.annoation.Audit;
import org.ivipi.ratel.system.common.controller.GenericController;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicenseAdd;
import org.ivipi.ratel.system.common.model.LicenseUpdate;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.domain.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("license")
public class LicenseController extends GenericController {

    @Autowired
    private LicenseService licenseService;

    @PostMapping("licenses")
    @Audit
    public Result<Page<License>> getLicenses(Auth auth, @RequestBody LicensePage licensePage) {
        Page<License> licenses = licenseService.getLicenses(auth, licensePage);
        return Result.success(licenses);
    }

    @PostMapping("add")
    @Audit
    public Result addLicense(Auth auth, @RequestBody LicenseAdd licenseAdd) {
        licenseService.addLicense(auth, licenseAdd);
        return Result.success();
    }

    @PostMapping("update")
    @Audit
    public Result updateLicense(Auth auth, @RequestBody LicenseUpdate licenseUpdate) {
        licenseService.updateLicense(auth, licenseUpdate);
        return Result.success();
    }
}
