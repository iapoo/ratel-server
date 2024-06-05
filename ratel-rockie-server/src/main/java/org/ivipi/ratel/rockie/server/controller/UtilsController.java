package org.ivipi.ratel.rockie.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.common.model.GoogleFonts;
import org.ivipi.ratel.rockie.server.api.GoogleFontApi;
import org.ivipi.ratel.system.common.annoation.Audit;
import org.ivipi.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("utils")
public class UtilsController {

    @Value("${ratel.rockie.google-font-api.key}")
    private String googleFontApiKey;

//    @Autowired
//    private GoogleFontApi googleFontApi;

    @PostMapping("google-fonts")
    @Audit
    public Result<String> getFontUrl(Auth auth) {
//        String googleFonts =  googleFontApi.getFontInfo(googleFontApiKey);
        String googleFonts = "";
        return Result.success(googleFonts);
    }
}
