package org.ivipa.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipa.ratel.system.common.model.Operator;
import org.ivipa.ratel.system.domain.entity.OperatorDo;

import java.util.List;

public interface OperatorMapper extends BaseMapper<OperatorDo> {
    List<Operator> getOperators(IPage<Operator> page, @Param("customerId") Long customerId);
}
