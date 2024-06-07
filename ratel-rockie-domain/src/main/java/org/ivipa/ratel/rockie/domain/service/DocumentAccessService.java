package org.ivipa.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentAccess;
import org.ivipa.ratel.rockie.common.model.DocumentAccessAdd;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDelete;
import org.ivipa.ratel.rockie.common.model.DocumentAccessPage;
import org.ivipa.ratel.rockie.common.model.DocumentAccessQuery;
import org.ivipa.ratel.rockie.common.model.DocumentAccessUpdate;
import org.ivipa.ratel.rockie.common.model.DocumentQuery;
import org.ivipa.ratel.rockie.common.utils.RockieConsts;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.entity.DocumentAccessDo;
import org.ivipa.ratel.rockie.domain.mapper.DocumentAccessMapper;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class DocumentAccessService extends ServiceImpl<DocumentAccessMapper, DocumentAccessDo> {

    @Autowired
    private DocumentService documentService;

    public List<DocumentAccess> addDocumentAccesses(Auth auth,  DocumentAccessAdd documentAccessAdd) {
        if (documentAccessAdd.getDocumentId() == null || documentAccessAdd.getCustomerIds() == null || documentAccessAdd.getCustomerIds().length == 0) {
            throw RockieError.DOCUMENT_ACCESS_INVALID_DOCUMENT_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentAccessAdd.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_ACCESS_DOCUMENT_ACCESS_NOT_FOUND.newException();
        }
        if(documentAccessAdd.getAccessMode() == null || documentAccessAdd.getAccessMode() < RockieConsts.ACCESS_MODE_MIN || documentAccessAdd.getAccessMode() > RockieConsts.ACCESS_MODE_MAX) {
            throw RockieError.DOCUMENT_ACCESS_ACCESS_MODE_IS_INVALID.newException();
        }

        List<DocumentAccessDo> documentAccessDos = convertDocumentAccessAdd(documentAccessAdd);
        this.saveBatch(documentAccessDos);
        List<DocumentAccess> documentAccesses = convertDocumentAccessDos(documentAccessDos);
        return documentAccesses;
    }

    public boolean deleteDocumentAccesses(Auth auth, DocumentAccessDelete documentAccessDelete) {
        if (documentAccessDelete.getDocumentId() == null) {
            throw RockieError.DOCUMENT_ACCESS_INVALID_DOCUMENT_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentAccessDelete.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_ACCESS_DOCUMENT_ACCESS_NOT_FOUND.newException();
        }

        QueryWrapper<DocumentAccessDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentAccessDelete.getDocumentId());
        return this.remove(queryWrapper);
    }

    public Page<DocumentAccess> getDocumentAccesses(Auth auth, DocumentAccessPage documentAccessPageQuery) {
        if (documentAccessPageQuery.getDocumentId() == null) {
            throw RockieError.DOCUMENT_ACCESS_INVALID_DOCUMENT_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentAccessPageQuery.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_ACCESS_DOCUMENT_ACCESS_NOT_FOUND.newException();
        }

        Page<DocumentAccessDo> page = new Page<>(documentAccessPageQuery.getPageNum(), documentAccessPageQuery.getPageSize());
        QueryWrapper<DocumentAccessDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentAccessPageQuery.getDocumentId());
        Page<DocumentAccessDo> result = this.page(page, queryWrapper);
        Page<DocumentAccess> documentAccessPage = new Page<>(documentAccessPageQuery.getPageNum(), documentAccessPageQuery.getPageSize());
        documentAccessPage.setRecords(convertDocumentAccessDos(result.getRecords()));
        return documentAccessPage;
    }

    public List<DocumentAccess> updateDocumentAccesses(Auth auth, DocumentAccessUpdate documentAccessUpdate) {
        if (documentAccessUpdate.getDocumentId() == null || documentAccessUpdate.getCustomerIds() == null || documentAccessUpdate.getCustomerIds().length == 0) {
            throw RockieError.DOCUMENT_ACCESS_INVALID_DOCUMENT_ACCESS_REQUEST.newException();
        }
        if(documentAccessUpdate.getAccessMode() == null || documentAccessUpdate.getAccessMode() < RockieConsts.ACCESS_MODE_MIN || documentAccessUpdate.getAccessMode() > RockieConsts.ACCESS_MODE_MAX) {
            throw RockieError.DOCUMENT_ACCESS_ACCESS_MODE_IS_INVALID.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentAccessUpdate.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_ACCESS_DOCUMENT_ACCESS_NOT_FOUND.newException();
        }

        QueryWrapper<DocumentAccessDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentAccessUpdate.getDocumentId());
        this.remove(queryWrapper);

        List<DocumentAccessDo> documentAccessDos = convertDocumentAccessUpdate(documentAccessUpdate);
        this.saveBatch(documentAccessDos);
        List<DocumentAccess> documentAccesses = convertDocumentAccessDos(documentAccessDos);
        return documentAccesses;
    }

    private List<DocumentAccessDo> convertDocumentAccessUpdate(DocumentAccessUpdate documentAccessUpdate) {
        List<DocumentAccessDo> documentAccessDos = new ArrayList<>();
        for(int i = 0; i < documentAccessUpdate.getCustomerIds().length; i ++) {
            DocumentAccessDo documentAccessDo = new DocumentAccessDo();
            BeanUtils.copyProperties(documentAccessUpdate, documentAccessDo);
            documentAccessDo.setCustomerId(documentAccessUpdate.getCustomerIds()[i]);
            documentAccessDos.add(documentAccessDo);
        }
        return documentAccessDos;
    }

    private List<DocumentAccessDo> convertDocumentAccessAdd(DocumentAccessAdd documentAccessAdd) {
        List<DocumentAccessDo> documentAccessDos = new ArrayList<>();
        for(int i = 0; i < documentAccessAdd.getCustomerIds().length; i ++) {
            DocumentAccessDo documentAccessDo = new DocumentAccessDo();
            BeanUtils.copyProperties(documentAccessAdd, documentAccessDo);
            documentAccessDo.setCustomerId(documentAccessAdd.getCustomerIds()[i]);
            documentAccessDos.add(documentAccessDo);
        }
        return documentAccessDos;
    }

    private List<DocumentAccess> convertDocumentAccessDos(List<DocumentAccessDo> documentAccessDos) {
        List<DocumentAccess> documentAccesses = new ArrayList<>();
        for(int i = 0; i < documentAccessDos.size(); i ++) {
            DocumentAccess documentAccess = new DocumentAccess();
            BeanUtils.copyProperties(documentAccessDos.get(i), documentAccess);
            documentAccesses.add(documentAccess);
        }
        return documentAccesses;
    }
}
