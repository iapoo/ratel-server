package org.ivipi.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipi.ratel.rockie.common.model.Folder;
import org.ivipi.ratel.rockie.domain.entity.DocumentDo;
import org.ivipi.ratel.rockie.domain.entity.FolderDo;

import java.util.List;

public interface FolderMapper extends BaseMapper<FolderDo> {
    List<Folder> getFolders(IPage<Folder> page, @Param("customerId") Long customerId);
}
