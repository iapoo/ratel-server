package org.ivipa.ratel.system.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.system.common.model.Operator;
import org.ivipa.ratel.system.common.model.OperatorAdd;
import org.ivipa.ratel.system.common.model.OperatorDelete;
import org.ivipa.ratel.system.common.model.OperatorDetail;
import org.ivipa.ratel.system.common.model.OperatorDetailPage;
import org.ivipa.ratel.system.common.model.OperatorPage;
import org.ivipa.ratel.system.common.model.OperatorQuery;
import org.ivipa.ratel.system.common.model.OperatorUpdate;
import org.ivipa.ratel.system.domain.service.OperatorService;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.controller.GenericController;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("operator")
public class OperatorController extends GenericController {

    @Autowired
    private OperatorService operatorService;


    @PostMapping("operators")
    @Audit
    public Result<Page<Operator>> getOperators(Auth auth, @RequestBody OperatorPage operatorPage) {
        Page<Operator> customers = operatorService.getOperators(auth, operatorPage);
        return Result.success(customers);
    }

    @PostMapping("operatorDetails")
    @Audit
    public Result<Page<OperatorDetail>> getOperatorDetails(Auth auth, @RequestBody OperatorDetailPage operatorDetailPage) {
        Page<OperatorDetail> customers = operatorService.getOperatorDetails(auth, operatorDetailPage);
        return Result.success(customers);
    }

    @PostMapping("operator")
    @Audit
    public Result<Operator> getOperator(Auth auth, @RequestBody OperatorQuery operatorQuery) {
        Operator operator = operatorService.getOperator(auth, operatorQuery);
        return Result.success(operator);
    }

    @PostMapping("add")
    @Audit
    public Result<Operator> addOperator(Auth auth, @RequestBody OperatorAdd operatorAdd) {
        Operator operator = operatorService.addOperator(auth, operatorAdd);
        return Result.success(operator);
    }

    @PostMapping("update")
    @Audit
    public Result updateOperator(Auth auth, @RequestBody OperatorUpdate operatorUpdate) {
        operatorService.updateOperator(auth, operatorUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteOperators(Auth auth, @RequestBody OperatorDelete operatorDelete) {
        operatorService.deleteOperator(auth, operatorDelete);
        return Result.success();
    }

}
