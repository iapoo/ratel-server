package org.ivipi.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.rockie.common.model.Content;
import org.ivipi.ratel.rockie.domain.entity.ContentDo;
import org.ivipi.ratel.rockie.domain.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("content")
public class ContentController {

    @Autowired
    private ContentService contentServicee;

    @PostMapping("get")
    public Page<Content> getDocuments() {
        Page<Content> customers = contentServicee.getContents(1, 10);
        return customers;
    }

    @PostMapping("add")
    public Page<Content> addDocument() {
        Page<Content> customers = contentServicee.getContents(1, 10);
        return customers;
    }

    @PostMapping("update")
    public Page<Content> updateDocument() {
        Page<Content> customers = contentServicee.getContents(1, 10);
        return customers;
    }

    @PostMapping("delete")
    public Page<Content> deleteDocuments() {
        Page<Content> customers = contentServicee.getContents(1, 10);
        return customers;
    }

}
