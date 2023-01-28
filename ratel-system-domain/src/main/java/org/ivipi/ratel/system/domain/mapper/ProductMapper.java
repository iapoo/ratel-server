package org.ivipi.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.ivipi.ratel.system.common.model.ProductPage;
import org.ivipi.ratel.system.domain.entity.ProductDo;

import java.util.List;

public interface ProductMapper extends BaseMapper<ProductDo> {
    List<ProductPage> getProductPage(IPage<ProductPage> page);
}