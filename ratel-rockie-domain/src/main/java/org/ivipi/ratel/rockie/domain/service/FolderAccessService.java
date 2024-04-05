package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.domain.entity.FolderAccessDo;
import org.ivipi.ratel.rockie.domain.mapper.FolderAccessMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FolderAccessService extends ServiceImpl<FolderAccessMapper, FolderAccessDo> {
}
