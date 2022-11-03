package org.ivipi.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentPageQuery;
import org.ivipi.ratel.rockie.domain.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;


    @PostMapping("page")
    public Page<Document> getDocumentPage(@RequestBody DocumentPageQuery documentPageQuery) {
        Page<Document> customers = documentService.getDocumentPage(documentPageQuery);
        return customers;
    }


    @PostMapping("page2")
    public Page<Document> getDocumentPage2(@RequestBody DocumentPageQuery documentPageQuery) {
        Page<Document> customers = documentService.getDocumentPage2(documentPageQuery);
        return customers;
    }

    @PostMapping("get")
    public Page<Document> getDocument() {
        Page<Document> customers = documentService.getDocuments(1, 10);
        return customers;
    }

    @PostMapping("add")
    public Page<Document> addDocument() {
        Page<Document> customers = documentService.getDocuments(1, 10);
        return customers;
    }

    @PostMapping("update")
    public Page<Document> updateDocument() {
        Page<Document> customers = documentService.getDocuments(1, 10);
        return customers;
    }

    @PostMapping("delete")
    public Page<Document> deleteDocuments() {
        Page<Document> customers = documentService.getDocuments(1, 10);
        return customers;
    }

}
