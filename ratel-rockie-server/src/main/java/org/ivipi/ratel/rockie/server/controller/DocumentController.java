package org.ivipi.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentPageQuery;
import org.ivipi.ratel.rockie.common.model.DocumentQuery;
import org.ivipi.ratel.rockie.domain.service.DocumentService;
import org.ivipi.ratel.system.common.controller.GenericController;
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


    @PostMapping("page")
    public Result<Page<Document>> getDocumentPage(@RequestBody DocumentPageQuery documentPageQuery) {
        Page<Document> customers = documentService.getDocumentPage(documentPageQuery);
        return Result.success(customers);
    }


    @PostMapping("page2")
    public Result<Page<Document>> getDocumentPage2(@RequestBody DocumentPageQuery documentPageQuery) {
        Page<Document> customers = documentService.getDocumentPage2(documentPageQuery);
        return Result.success(customers);
    }

    @PostMapping("get")
    public Result<Document> getDocument(@RequestBody DocumentQuery documentQuery) {
        Document document = documentService.getDocument(documentQuery.getDocumentId());
        return Result.success(document);
    }

    @PostMapping("add")
    public Result<Document> addDocument(@RequestBody Document document) {
        boolean isLoggedIn = isLoggedIn();
        Document newDocument = documentService.addDocument(document);
        return Result.success(newDocument);
    }

    @PostMapping("update")
    public Result<Document> updateDocument(@RequestBody Document document) {
        Document newDocument = documentService.updateDocument(document);
        return Result.success(newDocument);
    }

    @PostMapping("delete")
    public Page<Document> deleteDocuments() {
        Page<Document> customers = documentService.getDocuments(1, 10);
        return customers;
    }

}
