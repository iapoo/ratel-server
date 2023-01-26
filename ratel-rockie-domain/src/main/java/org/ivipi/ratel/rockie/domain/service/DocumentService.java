package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.common.model.Content;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentPageQuery;
import org.ivipi.ratel.rockie.common.utils.RockieError;
import org.ivipi.ratel.rockie.domain.entity.DocumentDo;
import org.ivipi.ratel.rockie.domain.mapper.DocumentMapper;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DocumentService extends ServiceImpl<DocumentMapper, DocumentDo> {

    @Autowired
    private ContentService contentService;

    public Page<Document> getDocumentPage(DocumentPageQuery documentPageQuery) {
        Page<DocumentDo> page = new Page<>(documentPageQuery.getPageNum(), documentPageQuery.getPageSize());
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(documentPageQuery.getPageNum(), documentPageQuery.getPageSize());
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
    }


    public Page<Document> getDocumentPage2(DocumentPageQuery documentPageQuery) {
        Page<Document> page = new Page<>(documentPageQuery.getPageNum(), documentPageQuery.getPageSize());
        List<Document> result = baseMapper.getDocumentPage(page);
        return page.setRecords(result);
    }


    public Document getDocument(Long documentId) {
        DocumentDo documentDo = getById(documentId);
        if(documentDo == null) {
            throw RockieError.DOCUMENT_DOCUMENT_NOT_FOUND.newException();
        }
        return convertDocumentDo(documentDo);

    }

    public Page<Document> getDocuments(int pageNum, int pageSize) {
        Page<DocumentDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(pageNum, pageSize);
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
    }

    public Document addDocument(Document document) {
        DocumentDo documentDo = convertDocument(document);
        Content newContent = contentService.addContent(document.getContent());
        documentDo.setContentId(newContent.getContentId());
        documentDo.setDocumentId(null);
        saveOrUpdate(documentDo);
        return convertDocumentDo(documentDo);
    }

    public Document updateDocument(Document document) {
        DocumentDo documentDo = convertDocument(document);
        Content newContent = contentService.updateContent(document.getContent());
        documentDo.setContentId(newContent.getContentId());
        saveOrUpdate(documentDo);
        return convertDocumentDo(documentDo);
    }

    private List<Document> convertDocumentDos(List<DocumentDo> documentDos) {
        List<Document> documents = new ArrayList<>();
        documentDos.forEach(
                documentDo -> {
                    Document document = new Document();
                    BeanUtils.copyProperties(documentDo, document);
                    documents.add(document);
                }
        );
        return documents;

    }


    private Document convertDocumentDo(DocumentDo documentDo) {
        Document document = new Document();
        BeanUtils.copyProperties(documentDo, document);
        return document;
    }

    private DocumentDo convertDocument(Document document) {
        DocumentDo documentDo = new DocumentDo();
        BeanUtils.copyProperties(document, documentDo);
        return documentDo;
    }
}
