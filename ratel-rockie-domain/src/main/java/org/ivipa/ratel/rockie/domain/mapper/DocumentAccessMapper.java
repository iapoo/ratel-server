package org.ivipa.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDetail;
import org.ivipa.ratel.rockie.domain.entity.DocumentAccessDo;

import java.util.List;

public interface DocumentAccessMapper extends BaseMapper<DocumentAccessDo> {
    List<DocumentAccessDetail> getDocumentAccessDetails(IPage<DocumentAccessDetail> page, @Param("documentId") Long documentId, @Param("like")String like);
}
