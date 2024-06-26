package org.ivipa.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentAdd;
import org.ivipa.ratel.rockie.common.model.DocumentDelete;
import org.ivipa.ratel.rockie.common.model.DocumentPage;
import org.ivipa.ratel.rockie.common.model.DocumentQuery;
import org.ivipa.ratel.rockie.common.model.DocumentUpdate;
import org.ivipa.ratel.rockie.domain.service.DocumentService;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.controller.GenericController;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("document")
public class DocumentController extends GenericController {

    @Autowired
    private DocumentService documentService;


    @PostMapping("documents")
    @Audit
    public Result<Page<Document>> getDocuments(Auth auth, @RequestBody DocumentPage documentPage) {
        Page<Document> customers = documentService.getDocuments(auth, documentPage);
        return Result.success(customers);
    }

    @PostMapping("document")
    @Audit
    public Result<Document> getDocument(Auth auth, @RequestBody DocumentQuery documentQuery) {
        Document document = documentService.getDocument(auth, documentQuery);
        return Result.success(document);
    }

    @PostMapping("add")
    @Audit
    public Result<Document> addDocument(Auth auth, @RequestBody DocumentAdd documentAdd) {
        Document document = documentService.addDocument(auth, documentAdd);
        return Result.success(document);
    }

    @PostMapping("update")
    @Audit
    public Result updateDocument(Auth auth, @RequestBody DocumentUpdate documentUpdate) {
        documentService.updateDocument(auth, documentUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteDocuments(Auth auth, @RequestBody DocumentDelete documentDelete) {
        documentService.deleteDocument(auth, documentDelete);
        return Result.success();
    }

}
