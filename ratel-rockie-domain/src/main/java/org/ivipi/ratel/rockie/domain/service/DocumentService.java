package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentPageQuery;
import org.ivipi.ratel.rockie.domain.entity.DocumentDo;
import org.ivipi.ratel.rockie.domain.mapper.DocumentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DocumentService extends ServiceImpl<DocumentMapper, DocumentDo> {


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


    public Page<Document> getDocuments(int pageNum, int pageSize) {
        Page<DocumentDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<DocumentDo> queryWrapper = new QueryWrapper<>();
        Page<DocumentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Document> documentPage = new Page<>(pageNum, pageSize);
        documentPage.setRecords(convertDocumentDos(result.getRecords()));
        return documentPage;
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

}
