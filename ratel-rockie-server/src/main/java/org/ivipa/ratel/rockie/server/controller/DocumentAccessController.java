package org.ivipa.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.DocumentAccess;
import org.ivipa.ratel.rockie.common.model.DocumentAccessAdd;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDelete;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDetail;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDetailPage;
import org.ivipa.ratel.rockie.common.model.DocumentAccessPage;
import org.ivipa.ratel.rockie.common.model.DocumentAccessUpdate;
import org.ivipa.ratel.rockie.domain.service.DocumentAccessService;
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
@RequestMapping("documentAccess")
public class DocumentAccessController extends GenericController {

    @Autowired
    private DocumentAccessService documentAccessService;


    @PostMapping("documentAccesses")
    @Audit
    public Result<Page<DocumentAccess>> getDocumentAccesses(Auth auth, @RequestBody DocumentAccessPage documentAccessPage) {
        Page<DocumentAccess> page = documentAccessService.getDocumentAccesses(auth, documentAccessPage);
        return Result.success(page);
    }

    @PostMapping("documentAccessDetails")
    @Audit
    public Result<Page<DocumentAccessDetail>> getDocumentAccessDetails(Auth auth, @RequestBody DocumentAccessDetailPage documentAccessDetailPage) {
        Page<DocumentAccessDetail> page = documentAccessService.getDocumentAccessDetails(auth, documentAccessDetailPage);
        return Result.success(page);
    }

    @PostMapping("add")
    @Audit
    public Result<DocumentAccess> addDocumentAccess(Auth auth, @RequestBody DocumentAccessAdd documentAccessAdd) {
        DocumentAccess documentAccess = documentAccessService.addDocumentAccess(auth, documentAccessAdd);
        return Result.success(documentAccess);
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
