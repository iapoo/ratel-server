package org.ivipa.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.Operator;
import org.ivipa.ratel.rockie.common.model.OperatorAdd;
import org.ivipa.ratel.rockie.common.model.OperatorDelete;
import org.ivipa.ratel.rockie.common.model.OperatorPage;
import org.ivipa.ratel.rockie.common.model.OperatorQuery;
import org.ivipa.ratel.rockie.common.model.OperatorUpdate;
import org.ivipa.ratel.rockie.common.utils.RockieConsts;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.service.OperatorService;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.controller.GenericController;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;

@RestController
@RequestMapping("/")
public class RockieController extends GenericController {


    @Value("${ratel.system.token.timeout}")
    private int tokenTimeout;

    @Autowired
    private OperatorService operatorService;


    @Resource(name = "rockieRedisTemplate")
    protected RedisTemplate rockieRedisTemplate;

    @PostMapping("admin")
    @Audit
    public Result admin(Auth auth ) {
        Long customerId = auth.getOnlineCustomer().getCustomerId();
        Operator operator = operatorService.getOperator(auth, customerId);
        refreshLoginOperation(customerId, operator);
        if(operator != null) {
            refreshLoginOperation(customerId, operator);
            return Result.success();
        } else {
            return Result.error(RockieError.OPERATOR_OPERATOR_NOT_FOUND.newException());
        }
    }

    protected void refreshLoginOperation(Long customerId, Operator operator) {
        String key = RockieConsts.OPERATOR_PREFIX + operator.getCustomerId();
        rockieRedisTemplate.opsForValue().set(key, operator, Duration.ofSeconds(tokenTimeout) );
    }

}
