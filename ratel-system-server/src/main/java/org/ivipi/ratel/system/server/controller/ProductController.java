package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.model.Product;
import org.ivipi.ratel.system.common.model.ProductAdd;
import org.ivipi.ratel.system.common.model.ProductEdit;
import org.ivipi.ratel.system.common.model.ProductPage;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.ivipi.ratel.system.domain.entity.ProductDo;
import org.ivipi.ratel.system.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController extends SystemGenericController {

    @Autowired
    private ProductService productService;

    @PostMapping("list")
    public Page<ProductDo> getProducts() {
        Page<ProductDo> products = productService.getPage(1, 10);
        return products;
    }

    @PostMapping("products")
    public Page<ProductPage> getProductPage() {
        Page<ProductPage> products = productService.getProductPage(1, 10);
        return products;
    }

    @PostMapping("add")
    public Result addProduct(@RequestBody ProductAdd productAdd) {
        boolean isLoggedIn = isLoggedIn();
        if(isLoggedIn) {
            productService.addProduct(productAdd);
            return Result.success();
        }
        return Result.error(SystemError.SYSTEM_TOKEN_NOT_FOUND.newException());
    }

    @PostMapping("update")
    public Result updateProduct(@RequestBody ProductEdit productEdit) {
        boolean isLoggedIn = isLoggedIn();
        if(isLoggedIn) {
            productService.updateProduct(productEdit);
            return Result.success();
        }
        return Result.error(SystemError.SYSTEM_TOKEN_NOT_FOUND.newException());
    }
}
