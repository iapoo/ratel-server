package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.common.model.Content;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentAdd;
import org.ivipi.ratel.rockie.common.model.DocumentPage;
import org.ivipi.ratel.rockie.common.model.DocumentQuery;
import org.ivipi.ratel.rockie.common.model.DocumentUpdate;
import org.ivipi.ratel.rockie.common.utils.RockieError;
import org.ivipi.ratel.rockie.domain.entity.DocumentDo;
import org.ivipi.ratel.rockie.domain.mapper.DocumentMapper;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DocumentService extends ServiceImpl<DocumentMapper, DocumentDo> {

    private final static int MAX_PAGE_SIZE = 99999999;
    @Autowired
    private ContentService contentService;

    public Page<Document> getDocumentPage(DocumentPage documentPageQuery) {
        Page<DocumentDo> page = new Page<>(documentPageQuery.getPageNum(), documentPageQuery.getPageSize());
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(documentPageQuery.getPageNum(), documentPageQuery.getPageSize());
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
    }


    public Page<Document> getDocuments(Auth auth, DocumentPage documentPage) {
        Page<Document> page = new Page<>(documentPage.getPageNum(), documentPage.getPageSize());
        List<Document> result = baseMapper.getDocuments(page, auth.getOnlineCustomer().getCustomerId(), documentPage.getFolderId());
        return page.setRecords(result);
    }


    public Document getDocument(Auth auth, DocumentQuery documentQuery) {
        DocumentDo documentDo = getById(documentQuery.getDocumentId());
        if (documentDo == null) {
            throw RockieError.DOCUMENT_DOCUMENT_NOT_FOUND.newException();
        }
        Document document = convertDocumentDo(documentDo);
        Content content = contentService.getContent(document.getContentId());
        document.setContent(content);
        return document;
    }

    public Page<Document> getDocuments(Long customerId, Long folderId) {
        Page<DocumentDo> page = new Page<>(1, MAX_PAGE_SIZE);
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        queryWrapper.eq("folder_id", folderId);
        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(1, MAX_PAGE_SIZE);
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
    }

    public Page<Document> getDocuments(int pageNum, int pageSize) {
        Page<DocumentDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(pageNum, pageSize);
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
    }

    public void addDocument(Auth auth, DocumentAdd documentAdd) {
        if (documentAdd.getFolderId() == null) {
            throw SystemError.DOCUMENT_FOLDER_NOT_FOUND.newException();
        }
        Page<Document> documents = getDocuments(auth.getOnlineCustomer().getCustomerId(), documentAdd.getFolderId());
        documents.getRecords().forEach(document -> {
            if(document.getDocumentName().equalsIgnoreCase(documentAdd.getDocumentName())) {
                throw  SystemError.DOCUMENT_DOCUMENT_NAME_EXISTS.newException();
            }
        });
        DocumentDo documentDo = convertDocumentAdd(documentAdd);
        Content newContent = contentService.addContent(auth,documentAdd.getFolderId(), documentAdd.getContent());
        documentDo.setContentId(newContent.getContentId());
        documentDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
        documentDo.setDocumentId(null);
        save(documentDo);
    }

    public void updateDocument(Auth auth, DocumentUpdate documentUpdate) {
        if (documentUpdate.getDocumentId() == null) {
            throw SystemError.DOCUMENT_DOCUMENT_ID_IS_NULL.newException();

        }
        DocumentDo oldDocumentDo = getById(documentUpdate.getDocumentId());
        if (oldDocumentDo == null) {
            throw SystemError.DOCUMENT_DOCUMENT_NOT_FOUND.newException();
        }
        if (!auth.getOnlineCustomer().getCustomerId().equals(oldDocumentDo.getCustomerId())) {
            throw SystemError.DOCUMENT_CUSTOMER_IS_INVALID.newException();
        }
        DocumentDo documentDo = convertDocumentUpdate(documentUpdate, oldDocumentDo);
        Content newContent = contentService.updateContent(auth, oldDocumentDo.getContentId(), documentUpdate.getContent());
        documentDo.setContentId(newContent.getContentId());
        updateById(documentDo);
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

    private DocumentDo convertDocumentUpdate(DocumentUpdate documentUpdate, DocumentDo oldDocumentDo) {
        BeanUtils.copyProperties(documentUpdate, oldDocumentDo);
        return oldDocumentDo;
    }

    private DocumentDo convertDocumentAdd(DocumentAdd documentAdd) {
        DocumentDo documentDo = new DocumentDo();
        BeanUtils.copyProperties(documentAdd, documentDo);
        return documentDo;
    }
}
