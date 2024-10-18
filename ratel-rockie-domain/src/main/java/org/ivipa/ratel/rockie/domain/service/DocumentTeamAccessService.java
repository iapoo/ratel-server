package org.ivipa.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentQuery;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccess;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessAdd;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessDelete;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessPage;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessUpdate;
import org.ivipa.ratel.rockie.common.utils.RockieConsts;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.entity.DocumentTeamAccessDo;
import org.ivipa.ratel.rockie.domain.mapper.DocumentTeamAccessMapper;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class DocumentTeamAccessService extends ServiceImpl<DocumentTeamAccessMapper, DocumentTeamAccessDo> {

    @Autowired
    private DocumentService documentService;

    public List<DocumentTeamAccess> addDocumentTeamAccesses(Auth auth,  DocumentTeamAccessAdd documentTeamAccessAdd) {
        if (documentTeamAccessAdd.getDocumentId() == null || documentTeamAccessAdd.getTeamId() == null) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_INVALID_DOCUMENT_TEAM_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentTeamAccessAdd.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_DOCUMENT_TEAM_ACCESS_NOT_FOUND.newException();
        }
        if(documentTeamAccessAdd.getAccessMode() == null || documentTeamAccessAdd.getAccessMode() < RockieConsts.ACCESS_MODE_MIN || documentTeamAccessAdd.getAccessMode() > RockieConsts.ACCESS_MODE_MAX) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_ACCESS_MODE_IS_INVALID.newException();
        }

        List<DocumentTeamAccessDo> documentTeamAccessDos = convertDocumentTeamAccessAdd(documentTeamAccessAdd);
        this.saveBatch(documentTeamAccessDos);
        List<DocumentTeamAccess> documentTeamAccesses = convertDocumentTeamAccessDos(documentTeamAccessDos);
        return documentTeamAccesses;
    }

    public boolean deleteDocumentTeamAccesses(Auth auth, DocumentTeamAccessDelete documentTeamAccessDelete) {
        if (documentTeamAccessDelete.getDocumentId() == null) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_INVALID_DOCUMENT_TEAM_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentTeamAccessDelete.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_DOCUMENT_TEAM_ACCESS_NOT_FOUND.newException();
        }

        QueryWrapper<DocumentTeamAccessDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentTeamAccessDelete.getDocumentId());
        queryWrapper.eq("team_id", documentTeamAccessDelete.getTeamId());
        return this.remove(queryWrapper);
    }

    public Page<DocumentTeamAccess> getDocumentTeamAccesses(Auth auth, DocumentTeamAccessPage documentTeamAccessPage) {
        if (documentTeamAccessPage.getDocumentId() == null) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_INVALID_DOCUMENT_TEAM_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentTeamAccessPage.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_DOCUMENT_TEAM_ACCESS_NOT_FOUND.newException();
        }

        Page<DocumentTeamAccessDo> page = new Page<>(documentTeamAccessPage.getPageNum(), documentTeamAccessPage.getPageSize());
        QueryWrapper<DocumentTeamAccessDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentTeamAccessPage.getDocumentId());
        Page<DocumentTeamAccessDo> result = this.page(page, queryWrapper);
        Page<DocumentTeamAccess> documentTeamAccessPageResult = new Page<>(documentTeamAccessPage.getPageNum(), documentTeamAccessPage.getPageSize());
        documentTeamAccessPageResult.setRecords(convertDocumentTeamAccessDos(result.getRecords()));
        return documentTeamAccessPageResult;
    }

    public Page<DocumentTeamAccess> getDocumentTeamAccessDetails(Auth auth, DocumentTeamAccessPage documentTeamAccessPage) {
        if (documentTeamAccessPage.getDocumentId() == null) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_INVALID_DOCUMENT_TEAM_ACCESS_REQUEST.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentTeamAccessPage.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_DOCUMENT_TEAM_ACCESS_NOT_FOUND.newException();
        }

        Page<DocumentTeamAccessDo> page = new Page<>(documentTeamAccessPage.getPageNum(), documentTeamAccessPage.getPageSize());
        QueryWrapper<DocumentTeamAccessDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentTeamAccessPage.getDocumentId());
        Page<DocumentTeamAccessDo> result = this.page(page, queryWrapper);
        Page<DocumentTeamAccess> documentTeamAccessPageResult = new Page<>(documentTeamAccessPage.getPageNum(), documentTeamAccessPage.getPageSize());
        documentTeamAccessPageResult.setRecords(convertDocumentTeamAccessDos(result.getRecords()));
        return documentTeamAccessPageResult;
    }

    public List<DocumentTeamAccess> updateDocumentTeamAccesses(Auth auth, DocumentTeamAccessUpdate documentTeamAccessUpdate) {
        if (documentTeamAccessUpdate.getDocumentId() == null || documentTeamAccessUpdate.getTeamId() == null) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_INVALID_DOCUMENT_TEAM_ACCESS_REQUEST.newException();
        }
        if(documentTeamAccessUpdate.getAccessMode() == null || documentTeamAccessUpdate.getAccessMode() < RockieConsts.ACCESS_MODE_MIN || documentTeamAccessUpdate.getAccessMode() > RockieConsts.ACCESS_MODE_MAX) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_ACCESS_MODE_IS_INVALID.newException();
        }
        DocumentQuery documentQuery = new DocumentQuery();
        documentQuery.setDocumentId(documentTeamAccessUpdate.getDocumentId());
        Document document = documentService.getDocument(auth, documentQuery);
        if(document == null || document.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.DOCUMENT_TEAM_ACCESS_DOCUMENT_TEAM_ACCESS_NOT_FOUND.newException();
        }

        QueryWrapper<DocumentTeamAccessDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentTeamAccessUpdate.getDocumentId());
        queryWrapper.eq("team_id", documentTeamAccessUpdate.getTeamId());
        this.remove(queryWrapper);

        List<DocumentTeamAccessDo> documentTeamAccessDos = convertDocumentTeamAccessUpdate(documentTeamAccessUpdate);
        this.saveBatch(documentTeamAccessDos);
        List<DocumentTeamAccess> documentTeamAccesses = convertDocumentTeamAccessDos(documentTeamAccessDos);
        return documentTeamAccesses;
    }

    private List<DocumentTeamAccessDo> convertDocumentTeamAccessUpdate(DocumentTeamAccessUpdate documentTeamAccessUpdate) {
        List<DocumentTeamAccessDo> documentTeamAccessDos = new ArrayList<>();
        DocumentTeamAccessDo documentTeamAccessDo = new DocumentTeamAccessDo();
        BeanUtils.copyProperties(documentTeamAccessUpdate, documentTeamAccessDo);
        documentTeamAccessDo.setTeamId(documentTeamAccessUpdate.getTeamId());
        documentTeamAccessDos.add(documentTeamAccessDo);
        return documentTeamAccessDos;
    }

    private List<DocumentTeamAccessDo> convertDocumentTeamAccessAdd(DocumentTeamAccessAdd documentTeamAccessAdd) {
        List<DocumentTeamAccessDo> documentTeamAccessDos = new ArrayList<>();
        DocumentTeamAccessDo documentTeamAccessDo = new DocumentTeamAccessDo();
        BeanUtils.copyProperties(documentTeamAccessAdd, documentTeamAccessDo);
        documentTeamAccessDo.setTeamId(documentTeamAccessAdd.getTeamId());
        documentTeamAccessDos.add(documentTeamAccessDo);
        return documentTeamAccessDos;
    }

    private List<DocumentTeamAccess> convertDocumentTeamAccessDos(List<DocumentTeamAccessDo> documentTeamAccessDos) {
        List<DocumentTeamAccess> documentTeamAccesses = new ArrayList<>();
        for(int i = 0; i < documentTeamAccessDos.size(); i ++) {
            DocumentTeamAccess documentTeamAccess = new DocumentTeamAccess();
            BeanUtils.copyProperties(documentTeamAccessDos.get(i), documentTeamAccess);
            documentTeamAccesses.add(documentTeamAccess);
        }
        return documentTeamAccesses;
    }
}
