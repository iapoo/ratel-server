package org.ivipa.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentAdd;
import org.ivipa.ratel.rockie.common.model.DocumentDelete;
import org.ivipa.ratel.rockie.common.model.DocumentLink;
import org.ivipa.ratel.rockie.common.model.DocumentLinkDelete;
import org.ivipa.ratel.rockie.common.model.DocumentLinkUpdate;
import org.ivipa.ratel.rockie.common.model.DocumentPage;
import org.ivipa.ratel.rockie.common.model.DocumentQuery;
import org.ivipa.ratel.rockie.common.model.DocumentShareUpdate;
import org.ivipa.ratel.rockie.common.model.DocumentUpdate;
import org.ivipa.ratel.rockie.common.model.OperatorDocument;
import org.ivipa.ratel.rockie.common.model.OperatorDocumentPage;
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
        Page<Document> documents = documentService.getDocuments(auth, documentPage);
        return Result.success(documents);
    }

    @PostMapping("operatorDocuments")
    @Audit
    public Result<Page<OperatorDocument>> getOperatorDocuments(Auth auth, @RequestBody OperatorDocumentPage operatorDocumentPage) {
        Page<OperatorDocument> operatorDocuments = documentService.getOperatorDocuments(auth, operatorDocumentPage);
        return Result.success(operatorDocuments);
    }

    @PostMapping("document")
    @Audit
    public Result<Document> getDocument(Auth auth, @RequestBody DocumentQuery documentQuery) {
        Document document = documentService.getDocument(auth, documentQuery);
        return Result.success(document);
    }

    @PostMapping("link")
    @Audit
    public Result<Document> getDocumentByLink(Auth auth, @RequestBody DocumentLink documentLink) {
        Document document = documentService.getDocumentByLink(auth, documentLink);
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

    @PostMapping("share")
    @Audit
    public Result shareDocument(Auth auth, @RequestBody DocumentShareUpdate documentShareUpdate) {
        documentService.updateDocumentShare(auth, documentShareUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteDocument(Auth auth, @RequestBody DocumentDelete documentDelete) {
        documentService.deleteDocument(auth, documentDelete);
        return Result.success();
    }

    @PostMapping("updateLink")
    @Audit
    public Result updateDocumentLink(Auth auth, @RequestBody DocumentLinkUpdate documentUpdate) {
        documentService.updateDocumentLink(auth, documentUpdate);
        return Result.success();
    }

    @PostMapping("deleteLink")
    @Audit
    public Result deleteDocumentLink(Auth auth, @RequestBody DocumentLinkDelete documentDelete) {
        documentService.deleteDocumentLink(auth, documentDelete);
        return Result.success();
    }

}
