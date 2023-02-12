package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.common.model.Content;
import org.ivipi.ratel.rockie.common.model.ContentAdd;
import org.ivipi.ratel.rockie.common.model.ContentPage;
import org.ivipi.ratel.rockie.common.model.ContentUpdate;
import org.ivipi.ratel.rockie.common.utils.RockieError;
import org.ivipi.ratel.rockie.domain.entity.ContentDo;
import org.ivipi.ratel.rockie.domain.mapper.ContentMapper;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContentService extends ServiceImpl<ContentMapper, ContentDo> {

    @Autowired
    private StorageService storageService;

    public Page<Content> getContentPage(ContentPage contentPageQuery) {
        Page<ContentDo> page = new Page<>(contentPageQuery.getPageNum(), contentPageQuery.getPageSize());
        QueryWrapper<ContentDo> queryWrapper = new QueryWrapper<>();
        Page<ContentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Content> contentPage = new Page<>(contentPageQuery.getPageNum(), contentPageQuery.getPageSize());
        contentPage.setRecords(convertContentDos(result.getRecords()));
        return contentPage;
    }

    public Content getContent(Long contentId) {
        ContentDo contentDo = getById(contentId);
        if (contentDo == null) {
            throw RockieError.DOCUMENT_DOCUMENT_NOT_FOUND.newException();
        }
        return convertContentDo(contentDo);

    }

    public Page<Content> getContents(int pageNum, int pageSize) {
        Page<ContentDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ContentDo> queryWrapper = new QueryWrapper<>();
        Page<ContentDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Content> contentPage = new Page<>(pageNum, pageSize);
        contentPage.setRecords(convertContentDos(result.getRecords()));
        return contentPage;
    }

    public Content addContent(Auth auth, ContentAdd contentAdd) {
        ContentDo contentDo = convertContentAdd(contentAdd);
        storageService.createObject(contentAdd.getContentName(), "document", auth.getOnlineCustomer().getCustomerCode(), contentAdd.getContent().getBytes());
        save(contentDo);
        return convertContentDo(contentDo);
    }

    public Content updateContent(Auth auth, Long contentId, ContentUpdate contentUpdate) {
        ContentDo oldContentDo =  getById(contentId);
        if(oldContentDo == null) {
            throw SystemError.CONTENT_CONTENT_NOT_FOUND.newException();
        }
        ContentDo contentDo = convertContentUpdate(contentUpdate, oldContentDo);
        storageService.createObject(contentUpdate.getContentName(), "document", auth.getOnlineCustomer().getCustomerCode(), contentUpdate.getContent().getBytes());
        updateById(contentDo);
        return convertContentDo(contentDo);
    }

    private List<Content> convertContentDos(List<ContentDo> contentDos) {
        List<Content> contents = new ArrayList<>();
        contentDos.forEach(
                contentDo -> {
                    Content content = new Content();
                    BeanUtils.copyProperties(contentDo, content);
                    contents.add(content);
                }
        );
        return contents;

    }

    private ContentDo convertContentUpdate(ContentUpdate contentUpdate, ContentDo contentDo) {
        BeanUtils.copyProperties(contentUpdate, contentDo);
        return contentDo;
    }

    private Content convertContentDo(ContentDo contentDo) {
        Content content = new Content();
        BeanUtils.copyProperties(contentDo, content);
        return content;
    }

    private ContentDo convertContentAdd(ContentAdd contentAdd) {
        ContentDo contentDo = new ContentDo();
        BeanUtils.copyProperties(contentAdd, contentDo);
        return contentDo;
    }

}
