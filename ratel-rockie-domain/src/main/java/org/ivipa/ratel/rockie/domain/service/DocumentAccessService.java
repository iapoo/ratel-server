package org.ivipa.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentAccess;
import org.ivipa.ratel.rockie.common.model.DocumentAccessAdd;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDelete;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDetail;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDetailPage;
import org.ivipa.ratel.rockie.common.model.DocumentAccessPage;
import org.ivipa.ratel.rockie.common.model.DocumentAccessQuery;
import org.ivipa.ratel.rockie.common.model.DocumentAccessUpdate;
import org.ivipa.ratel.rockie.common.model.DocumentQuery;
import org.ivipa.ratel.rockie.common.utils.RockieConsts;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.entity.DocumentAccessDo;
import org.ivipa.ratel.rockie.domain.mapper.DocumentAccessMapper;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.domain.entity.CustomerDo;
import org.ivipa.ratel.system.domain.service.CustomerService;
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

    @Autowired
    private CustomerService customerService;

    public DocumentAccess addDocumentAccess(Auth auth,  DocumentAccessAdd documentAccessAdd) {
        if (documentAccessAdd.getDocumentId() == null || documentAccessAdd.getCustomerName() == null || documentAccessAdd.getCustomerName().length() == 0) {
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
        CustomerDo customerDO = customerService.getCustomerDo(documentAccessAdd.getCustomerName());
        if(customerDO == null) {
            throw RockieError.DOCUMENT_ACCESS_CUSTOMER_NOT_FOUND.newException();
        }
        DocumentAccessDo documentAccessDo = convertDocumentAccessAdd(documentAccessAdd);
        documentAccessDo.setCustomerId(customerDO.getCustomerId());
        this.save(documentAccessDo);
        DocumentAccess documentAccess= convertDocumentAccessDo(documentAccessDo);
        return documentAccess;
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
        queryWrapper.eq("customer_id", documentAccessDelete.getCustomerId());
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


    public Page<DocumentAccessDetail> getDocumentAccessDetails(Auth auth, DocumentAccessDetailPage documentAccessDetailPage) {
        if (documentAccessDetailPage.getDocumentId() == null) {
            throw RockieError.DOCUMENT_ACCESS_INVALID_DOCUMENT_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentAccessDetailPage.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_ACCESS_DOCUMENT_ACCESS_NOT_FOUND.newException();
        }

        Page<DocumentAccessDetail> page = new Page<>(documentAccessDetailPage.getPageNum(), documentAccessDetailPage.getPageSize());
        List<DocumentAccessDetail> result = baseMapper.getDocumentAccessDetails(page,documentAccessDetailPage.getDocumentId(), documentAccessDetailPage.getLike());
        return page.setRecords(result);
    }

    public List<DocumentAccess> updateDocumentAccesses(Auth auth, DocumentAccessUpdate documentAccessUpdate) {
        if (documentAccessUpdate.getDocumentId() == null || documentAccessUpdate.getCustomerId() == null) {
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
        queryWrapper.eq("customer_id", documentAccessUpdate.getCustomerId());
        this.remove(queryWrapper);

        List<DocumentAccessDo> documentAccessDos = convertDocumentAccessUpdate(documentAccessUpdate);
        this.saveBatch(documentAccessDos);
        List<DocumentAccess> documentAccesses = convertDocumentAccessDos(documentAccessDos);
        return documentAccesses;
    }

    private List<DocumentAccessDo> convertDocumentAccessUpdate(DocumentAccessUpdate documentAccessUpdate) {
        List<DocumentAccessDo> documentAccessDos = new ArrayList<>();
        DocumentAccessDo documentAccessDo = new DocumentAccessDo();
        BeanUtils.copyProperties(documentAccessUpdate, documentAccessDo);
        documentAccessDo.setCustomerId(documentAccessUpdate.getCustomerId());
        documentAccessDos.add(documentAccessDo);
        return documentAccessDos;
    }

    private DocumentAccessDo convertDocumentAccessAdd(DocumentAccessAdd documentAccessAdd) {
        DocumentAccessDo documentAccessDo = new DocumentAccessDo();
        BeanUtils.copyProperties(documentAccessAdd, documentAccessDo);
        return documentAccessDo;
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

    private DocumentAccess convertDocumentAccessDo(DocumentAccessDo documentAccessDo) {
        DocumentAccess documentAccess = new DocumentAccess();
        BeanUtils.copyProperties(documentAccessDo, documentAccess);
        return documentAccess;
    }
}
