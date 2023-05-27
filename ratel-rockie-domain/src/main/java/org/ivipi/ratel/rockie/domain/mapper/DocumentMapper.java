package org.ivipi.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.domain.entity.DocumentDo;

import java.util.List;

public interface DocumentMapper extends BaseMapper<DocumentDo> {
    List<Document> getDocuments(IPage<Document> page, @Param("customerId") Long customerId, @Param("folderId")Long folderId);
}
