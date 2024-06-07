package org.ivipa.ratel.rockie.server.controller;

import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
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
