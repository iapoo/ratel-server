package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.domain.entity.ContentDo;
import org.ivipi.ratel.rockie.domain.mapper.ContentMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContentService extends ServiceImpl<ContentMapper, ContentDo> {


    public Page<ContentDo> getPage(int pageNum, int pageSize) {
        Page<ContentDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ContentDo> queryWrapper = new QueryWrapper<>();
        Page<ContentDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }

}
