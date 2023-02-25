package org.ivipi.ratel.system.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.system.common.annoation.Audit;
import org.ivipi.ratel.system.common.controller.GenericController;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.system.common.model.Product;
import org.ivipi.ratel.system.common.model.ProductAdd;
import org.ivipi.ratel.system.common.model.ProductDelete;
import org.ivipi.ratel.system.common.model.ProductQuery;
import org.ivipi.ratel.system.common.model.ProductUpdate;
import org.ivipi.ratel.system.common.model.ProductPage;
import org.ivipi.ratel.system.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController extends GenericController {

    @Autowired
    private ProductService productService;

    @PostMapping("products")
    @Audit
    public Result<Page<Product>> getProducts(Auth auth, @RequestBody ProductPage productPage) {
        Page<Product> products = productService.getProducts(auth, productPage);
        return Result.success(products);
    }

    @PostMapping("add")
    @Audit
    public Result addProduct(Auth auth, @RequestBody ProductAdd productAdd) {
        productService.addProduct(auth, productAdd);
        return Result.success();
    }

    @PostMapping("update")
    @Audit
    public Result updateProduct(Auth auth, @RequestBody ProductUpdate productUpdate) {
        productService.updateProduct(auth, productUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteProduct(Auth auth, @RequestBody ProductDelete productDelete) {
        productService.deleteProduct(auth, productDelete);
        return Result.success();
    }

    @PostMapping("product")
    @Audit
    public Result getProduct(Auth auth, @RequestBody ProductQuery productQuery) {
        Product product = productService.getProduct(auth, productQuery);
        return Result.success();
    }
}
