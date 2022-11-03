package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.domain.entity.FolderDo;
import org.ivipi.ratel.rockie.domain.mapper.FolderMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FolderService extends ServiceImpl<FolderMapper, FolderDo> {


    public Page<FolderDo> getPage(int pageNum, int pageSize) {
        Page<FolderDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<FolderDo> queryWrapper = new QueryWrapper<>();
        Page<FolderDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }


}
