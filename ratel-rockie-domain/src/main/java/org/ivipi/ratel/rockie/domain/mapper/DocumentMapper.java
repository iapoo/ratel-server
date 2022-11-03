package org.ivipi.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.domain.entity.DocumentDo;

import java.util.List;

public interface DocumentMapper extends BaseMapper<DocumentDo> {
    List<Document> getDocumentPage(IPage<Document> page);
}
