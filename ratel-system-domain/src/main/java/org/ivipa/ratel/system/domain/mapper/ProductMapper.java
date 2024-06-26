package org.ivipa.ratel.system.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipa.ratel.system.common.model.Product;
import org.ivipa.ratel.system.domain.entity.ProductDo;

import java.util.List;

public interface ProductMapper extends BaseMapper<ProductDo> {
    List<Product> getProducts(IPage<Product> page);
}
