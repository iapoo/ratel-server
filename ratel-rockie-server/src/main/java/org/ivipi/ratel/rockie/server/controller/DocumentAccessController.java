package org.ivipi.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentAccess;
import org.ivipi.ratel.rockie.common.model.DocumentAccessAdd;
import org.ivipi.ratel.rockie.common.model.DocumentAccessDelete;
import org.ivipi.ratel.rockie.common.model.DocumentAccessPage;
import org.ivipi.ratel.rockie.common.model.DocumentAccessUpdate;
import org.ivipi.ratel.rockie.common.model.DocumentAdd;
import org.ivipi.ratel.rockie.common.model.DocumentDelete;
import org.ivipi.ratel.rockie.common.model.DocumentPage;
import org.ivipi.ratel.rockie.common.model.DocumentQuery;
import org.ivipi.ratel.rockie.common.model.DocumentUpdate;
import org.ivipi.ratel.rockie.domain.service.DocumentAccessService;
import org.ivipi.ratel.rockie.domain.service.DocumentService;
import org.ivipi.ratel.system.common.annoation.Audit;
import org.ivipi.ratel.system.common.controller.GenericController;
import org.ivipi.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("document-access")
public class DocumentAccessController extends GenericController {

    @Autowired
    private DocumentAccessService documentAccessService;


    @PostMapping("document-accesses")
    @Audit
    public Result<Page<DocumentAccess>> getDocumentAccesses(Auth auth, @RequestBody DocumentAccessPage documentAccessPage) {
        Page<DocumentAccess> page = documentAccessService.getDocumentAccesses(auth, documentAccessPage);
        return Result.success(page);
    }

    @PostMapping("add")
    @Audit
    public Result<List<DocumentAccess>> addDocumentAccess(Auth auth, @RequestBody DocumentAccessAdd documentAccessAdd) {
        List<DocumentAccess> documentAccessList = documentAccessService.addDocumentAccesses(auth, documentAccessAdd);
        return Result.success(documentAccessList);
    }

    @PostMapping("update")
    @Audit
    public Result<List<DocumentAccess>> updateDocumentAccesses(Auth auth, @RequestBody DocumentAccessUpdate documentAccessUpdate) {
        List<DocumentAccess> documentAccessList = documentAccessService.updateDocumentAccesses(auth, documentAccessUpdate);
        return Result.success(documentAccessList);
    }

    @PostMapping("delete")
    @Audit
    public Result<Boolean> deleteDocumentAccesses(Auth auth, @RequestBody DocumentAccessDelete documentAccessDelete) {
        boolean result = documentAccessService.deleteDocumentAccesses(auth, documentAccessDelete);
        return Result.success(result);
    }

}
