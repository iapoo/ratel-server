package org.ivipa.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.rockie.common.model.Content;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentAdd;
import org.ivipa.ratel.rockie.common.model.DocumentDelete;
import org.ivipa.ratel.rockie.common.model.DocumentPage;
import org.ivipa.ratel.rockie.common.model.DocumentQuery;
import org.ivipa.ratel.rockie.common.model.DocumentUpdate;
import org.ivipa.ratel.rockie.common.model.OperatorDocument;
import org.ivipa.ratel.rockie.common.model.OperatorDocumentPage;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.entity.DocumentDo;
import org.ivipa.ratel.rockie.domain.mapper.DocumentMapper;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.common.utils.SystemConstants;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DocumentService extends ServiceImpl<DocumentMapper, DocumentDo> {

    private final static int MAX_PAGE_SIZE = 99999999;
    @Autowired
    private ContentService contentService;

    public Page<Document> getDocuments(Auth auth, DocumentPage documentPage) {
        Page<Document> page = new Page<>(documentPage.getPageNum(), documentPage.getPageSize());
        List<Document> result = baseMapper.getDocuments(page, auth.getOnlineCustomer().getCustomerId(), documentPage.getFolderId());
        return page.setRecords(result);
    }

    public Page<OperatorDocument> getOperatorDocuments(Auth auth, OperatorDocumentPage operatorDocumentPage) {
        if(auth.getOperator() == null) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        if(auth.getOperator().getOperatorType() <= SystemConstants.OPERATOR_TYPE_CUSTOMER_OPERATION) {
            throw SystemError.AUTH_INSUFFICIENT_PERMISSION.newException();
        }
        Page<OperatorDocument> page = new Page<>(operatorDocumentPage.getPageNum(), operatorDocumentPage.getPageSize());
        List<OperatorDocument> result = baseMapper.getOperatorDocuments(page, operatorDocumentPage.getLike());
        return page.setRecords(result);
    }

    public Document getDocument(Auth auth, DocumentQuery documentQuery) {
        DocumentDo documentDo = getById(documentQuery.getDocumentId());
        if (documentDo == null || documentDo.getDeleted()) {
            throw RockieError.DOCUMENT_DOCUMENT_NOT_FOUND.newException();
        }
        Document document = convertDocumentDo(documentDo);
        Content content = contentService.getContent(auth, document.getFolderId(), document.getContentId());
        document.setContent(content);
        return document;
    }

    public Page<Document> getDocuments(Long customerId, Long folderId) {
        Page<DocumentDo> page = new Page<>(1, MAX_PAGE_SIZE);
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        queryWrapper.eq("folder_id", folderId);
        queryWrapper.eq("deleted", 0);
        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(1, MAX_PAGE_SIZE);
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
    }

    public Page<Document> getDocumentsInRootFolder(Long customerId) {
        Page<DocumentDo> page = new Page<>(1, MAX_PAGE_SIZE);
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        queryWrapper.isNull("folder_id");
        queryWrapper.eq("deleted", 0);

        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(1, MAX_PAGE_SIZE);
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
    }

    public Document addDocument(Auth auth, DocumentAdd documentAdd) {
        Page<Document> documents;
        if (documentAdd.getFolderId() == null) {
            //throw SystemError.DOCUMENT_FOLDER_NOT_FOUND.newException();
            //Check root folder of customer
            documents = getDocumentsInRootFolder(auth.getOnlineCustomer().getCustomerId());
        } else {
            documents = getDocuments(auth.getOnlineCustomer().getCustomerId(), documentAdd.getFolderId());
        }
        documents.getRecords().forEach(document -> {
            if (document.getDocumentName().equalsIgnoreCase(documentAdd.getDocumentName()) && !document.getDeleted()) {
                throw RockieError.DOCUMENT_DOCUMENT_NAME_EXISTS.newException();
            }
        });
        DocumentDo documentDo = convertDocumentAdd(documentAdd);
        Content newContent = contentService.addContent(auth, documentAdd.getFolderId(), documentAdd.getContent());
        documentDo.setContentId(newContent.getContentId());
        documentDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
        documentDo.setDocumentId(null);
        documentDo.setCreatedDate(LocalDateTime.now());
        documentDo.setUpdatedDate(LocalDateTime.now());
        save(documentDo);
        Document document = convertDocumentDo(documentDo);
        return document;
    }

    public void updateDocument(Auth auth, DocumentUpdate documentUpdate) {
        if (documentUpdate.getDocumentId() == null) {
            throw RockieError.DOCUMENT_DOCUMENT_ID_IS_NULL.newException();
        }
        DocumentDo oldDocumentDo = getById(documentUpdate.getDocumentId());
        if (oldDocumentDo == null || oldDocumentDo.getDeleted()) {
            throw RockieError.DOCUMENT_DOCUMENT_NOT_FOUND.newException();
        }
        if (!auth.getOnlineCustomer().getCustomerId().equals(oldDocumentDo.getCustomerId())) {
            throw RockieError.DOCUMENT_CUSTOMER_IS_INVALID.newException();
        }
        Page<Document> documents;
        if(documentUpdate.getFolderId() != null) {
            documents = getDocuments(auth.getOnlineCustomer().getCustomerId(), documentUpdate.getFolderId());
        } else {
            documents = getDocumentsInRootFolder(auth.getOnlineCustomer().getCustomerId());
        }
        documents.getRecords().forEach(document -> {
            if (!document.getDocumentId().equals(documentUpdate.getDocumentId()) && document.getDocumentName().equalsIgnoreCase(documentUpdate.getDocumentName()) && !document.getDeleted()) {
                throw RockieError.DOCUMENT_DOCUMENT_NAME_EXISTS.newException();
            }
        });
        DocumentDo documentDo = convertDocumentUpdate(documentUpdate, oldDocumentDo);
        Content newContent = contentService.updateContent(auth, documentDo.getFolderId(), oldDocumentDo.getContentId(), documentUpdate.getContent());
        documentDo.setContentId(newContent.getContentId());
        documentDo.setUpdatedDate(LocalDateTime.now());
        updateById(documentDo);
    }


    public void deleteDocument(Auth auth, DocumentDelete documentUpdate) {
        if (documentUpdate.getDocumentId() == null) {
            throw RockieError.DOCUMENT_DOCUMENT_ID_IS_NULL.newException();
        }
        DocumentDo oldDocumentDo = getById(documentUpdate.getDocumentId());
        if (oldDocumentDo == null || oldDocumentDo.getDeleted()) {
            throw RockieError.DOCUMENT_DOCUMENT_NOT_FOUND.newException();
        }
        if (!auth.getOnlineCustomer().getCustomerId().equals(oldDocumentDo.getCustomerId())) {
            throw RockieError.DOCUMENT_CUSTOMER_IS_INVALID.newException();
        }
        DocumentDo documentDo = oldDocumentDo;
        documentDo.setDeleted(true);
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
