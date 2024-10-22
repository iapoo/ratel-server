package org.ivipa.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipa.ratel.rockie.common.model.DocumentTeamAccessDetail;
import org.ivipa.ratel.rockie.domain.entity.DocumentTeamAccessDo;

import java.util.List;

public interface DocumentTeamAccessMapper extends BaseMapper<DocumentTeamAccessDo> {
    List<DocumentTeamAccessDetail> getDocumentTeamAccessDetails(IPage<DocumentTeamAccessDetail> page, @Param("documentId") Long documentId, @Param("like")String like);
}
