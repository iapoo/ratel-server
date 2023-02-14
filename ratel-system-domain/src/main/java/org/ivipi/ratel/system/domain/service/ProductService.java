package org.ivipi.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.model.Customer;
import org.ivipi.ratel.system.common.model.Product;
import org.ivipi.ratel.system.common.model.ProductAdd;
import org.ivipi.ratel.system.common.model.ProductUpdate;
import org.ivipi.ratel.system.common.model.ProductPage;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.domain.entity.CustomerDo;
import org.ivipi.ratel.system.domain.entity.ProductDo;
import org.ivipi.ratel.system.domain.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService extends ServiceImpl<ProductMapper, ProductDo> {

    public Page<ProductDo> getPage(int pageNum, int pageSize) {
        Page<ProductDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<ProductDo> queryWrapper = new QueryWrapper<>();
        Page<ProductDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }


    public Page<Product> getProducts(Auth auth, ProductPage productPage) {
        Page<Product> page = new Page<>(productPage.getPageNum(), productPage.getPageSize());
        List<Product> result = baseMapper.getProducts(page);
        return page.setRecords(result);
    }

    public Product getProduct(Long productId) {
        ProductDo productDo = getById(productId);
        Product product = convertProductDo(productDo);
        return product;
    }

    public Product getProduct(String productName) {
        QueryWrapper<ProductDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_name", productName);
        ProductDo productDo = this.getOne(queryWrapper);
        if(productDo != null) {
            Product product = convertProductDo(productDo);
            return product;
        } else {
            return null;
        }
    }

    public Product getAvailableProduct(Long productId) {
        Product product = getProduct(productId);
        if(product.getEnabled() && !product.getDeleted()) {
            return product;
        }
        return null;
    }

    public void addProduct(Auth auth, ProductAdd productAdd) {
        Product oldProduct = getProduct(productAdd.getProductName());
        if(oldProduct != null) {
            throw SystemError.PRODUCT_PRODUCT_NAME_EXISTS.newException();
        }
        ProductDo productDo = convertProductAdd(productAdd);
        productDo.setProductId(null);
        save(productDo);
    }

    public void updateProduct(Auth auth, ProductUpdate productUpdate) {
        if(productUpdate.getProductId() == null) {
            throw SystemError.PRODUCT_PRODUCT_ID_IS_NULL.newException();
        }
        ProductDo oldProductDo = getById(productUpdate.getProductId());
        if(oldProductDo == null) {
            throw SystemError.PRODUCT_PRODUCT_NOT_FOUND.newException();
        }
        ProductDo productDo = convertProductUpdate(productUpdate, oldProductDo);
        updateById(productDo);
    }

    private Product convertProductDo(ProductDo productDo) {
        Product product = new Product();
        BeanUtils.copyProperties(productDo, product);
        return product;
    }

    private ProductDo convertProductAdd(ProductAdd productAdd) {
        ProductDo productDo = new ProductDo();
        BeanUtils.copyProperties(productAdd, productDo);
        return productDo;
    }

    private ProductDo convertProductUpdate(ProductUpdate productUpdate, ProductDo productDo) {
        BeanUtils.copyProperties(productUpdate, productDo);
        return productDo;
    }
}
