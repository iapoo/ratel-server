package org.ivipa.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccess;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessAdd;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessDelete;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessPage;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessUpdate;
import org.ivipa.ratel.rockie.domain.service.DocumentTeamAccessService;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.controller.GenericController;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("document-team-access")
public class DocumentTeamAccessController extends GenericController {

    @Autowired
    private DocumentTeamAccessService documentTeamAccessService;


    @PostMapping("document-team-accesses")
    @Audit
    public Result<Page<DocumentTeamAccess>> getDocumentTeamAccesses(Auth auth, @RequestBody DocumentTeamAccessPage documentTeamAccessPage) {
        Page<DocumentTeamAccess> page = documentTeamAccessService.getDocumentTeamAccesses(auth, documentTeamAccessPage);
        return Result.success(page);
    }

    @PostMapping("add")
    @Audit
    public Result<List<DocumentTeamAccess>> addDocumentTeamAccess(Auth auth, @RequestBody DocumentTeamAccessAdd documentTeamAccessAdd) {
        List<DocumentTeamAccess> documentTeamAccessList = documentTeamAccessService.addDocumentTeamAccesses(auth, documentTeamAccessAdd);
        return Result.success(documentTeamAccessList);
    }

    @PostMapping("update")
    @Audit
    public Result<List<DocumentTeamAccess>> updateDocumentTeamAccesses(Auth auth, @RequestBody DocumentTeamAccessUpdate documentTeamAccessUpdate) {
        List<DocumentTeamAccess> documentTeamAccessList = documentTeamAccessService.updateDocumentTeamAccesses(auth, documentTeamAccessUpdate);
        return Result.success(documentTeamAccessList);
    }

    @PostMapping("delete")
    @Audit
    public Result<Boolean> deleteDocumentTeamAccesses(Auth auth, @RequestBody DocumentTeamAccessDelete documentTeamAccessDelete) {
        boolean result = documentTeamAccessService.deleteDocumentTeamAccesses(auth, documentTeamAccessDelete);
        return Result.success(result);
    }

}
