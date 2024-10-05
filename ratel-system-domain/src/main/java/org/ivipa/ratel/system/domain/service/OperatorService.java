package org.ivipa.ratel.system.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.system.common.model.Operator;
import org.ivipa.ratel.system.common.model.OperatorAdd;
import org.ivipa.ratel.system.common.model.OperatorDelete;
import org.ivipa.ratel.system.common.model.OperatorDetail;
import org.ivipa.ratel.system.common.model.OperatorPage;
import org.ivipa.ratel.system.common.model.OperatorQuery;
import org.ivipa.ratel.system.common.model.OperatorUpdate;
import org.ivipa.ratel.system.common.model.Auth;
import org.ivipa.ratel.system.common.utils.SystemConstants;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.ivipa.ratel.system.domain.entity.OperatorDo;
import org.ivipa.ratel.system.domain.mapper.OperatorMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class OperatorService extends ServiceImpl<OperatorMapper, OperatorDo> {

    public Operator addOperator(Auth auth,  OperatorAdd operatorAdd) {
        if (operatorAdd.getCustomerId() == null) {
            throw SystemError.OPERATOR_INVALID_OPERATOR_REQUEST.newException();
        }
        if(operatorAdd.getOperatorType() == null || operatorAdd.getOperatorType() < SystemConstants.OPERATOR_TYPE_MIN || operatorAdd.getOperatorType() > SystemConstants.OPERATOR_TYPE_MAX) {
            throw SystemError.OPERATOR_OPERATOR_TYPE_IS_INVALID.newException();
        }
        Operator oldOperator = getOperator(auth, operatorAdd.getCustomerId());
        if(oldOperator != null) {
            throw SystemError.OPERATOR_CUSTOMER_EXISTS.newException();
        }
        OperatorDo operatorDo = convertOperatorAdd(operatorAdd);
        this.save(operatorDo);
        Operator operator = convertOperatorDo(operatorDo);
        return operator;
    }


    public Operator getOperator(Auth auth, OperatorQuery operatorQuery) {
        OperatorDo operatorDo = getById(operatorQuery.getOperatorId());
        if (operatorDo == null || operatorDo.getDeleted()) {
            throw SystemError.OPERATOR_OPERATOR_NOT_FOUND.newException();
        }
        Operator operator = convertOperatorDo(operatorDo);
        return operator;
    }

    public Operator getOperator(Auth auth,  Long customerId) {
        QueryWrapper<OperatorDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        queryWrapper.eq("deleted", false);
        OperatorDo operatorDo = this.getOne(queryWrapper);
        if (operatorDo != null) {
            Operator operator = convertOperatorDo(operatorDo);
            return operator;
        } else {
            return null;
        }
    }
    
    public void deleteOperator(Auth auth, OperatorDelete operatorDelete) {
        if (operatorDelete.getOperatorId() == null) {
            throw SystemError.OPERATOR_INVALID_OPERATOR_REQUEST.newException();
        }
        OperatorDo oldOperatorDo = getById(operatorDelete.getOperatorId());
        if (oldOperatorDo == null || oldOperatorDo.getDeleted()) {
            throw SystemError.OPERATOR_OPERATOR_NOT_FOUND.newException();
        }

        OperatorDo operatorDo = oldOperatorDo;
        operatorDo.setDeleted(true);
        updateById(operatorDo);
    }

    public Page<Operator> getOperators(Auth auth, OperatorPage operatorPageQuery) {
        Page<OperatorDo> page = new Page<>(operatorPageQuery.getPageNum(), operatorPageQuery.getPageSize());
        QueryWrapper<OperatorDo> queryWrapper = new QueryWrapper<>();
        Page<OperatorDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<Operator> operatorPage = new Page<>(operatorPageQuery.getPageNum(), operatorPageQuery.getPageSize());
        operatorPage.setRecords(convertOperatorDos(result.getRecords()));
        return operatorPage;
    }

    public Page<OperatorDetail> getOperatorDetails(Auth auth, OperatorPage operatorPageQuery) {
        Page<OperatorDetail> page = new Page<>(operatorPageQuery.getPageNum(), operatorPageQuery.getPageSize());
        QueryWrapper<OperatorDo> queryWrapper = new QueryWrapper<>();
        List<OperatorDetail> result = baseMapper.getOperatorDetails(page, operatorPageQuery.getCustomerName(), operatorPageQuery.getEmail());
        Page<OperatorDetail> operatorPage = new Page<>(operatorPageQuery.getPageNum(), operatorPageQuery.getPageSize());
        operatorPage.setRecords(result);
        return operatorPage;
    }

    public Operator updateOperator(Auth auth, OperatorUpdate operatorUpdate) {
        if (operatorUpdate.getCustomerId() == null) {
            throw SystemError.OPERATOR_INVALID_OPERATOR_REQUEST.newException();
        }
        if(operatorUpdate.getOperatorType() == null || operatorUpdate.getOperatorType() < SystemConstants.OPERATOR_TYPE_MIN || operatorUpdate.getOperatorType() > SystemConstants.OPERATOR_TYPE_MAX) {
            throw SystemError.OPERATOR_OPERATOR_TYPE_IS_INVALID.newException();
        }

        OperatorDo oldOperatorDo = getById(operatorUpdate.getOperatorId());
        if (oldOperatorDo == null || oldOperatorDo.getDeleted()) {
            throw SystemError.OPERATOR_OPERATOR_NOT_FOUND.newException();
        }
        Operator oldCustomerOperator = getOperator(auth, operatorUpdate.getCustomerId());
        if(oldCustomerOperator != null && oldCustomerOperator.getOperatorId() != operatorUpdate.getOperatorId()) {
            throw SystemError.OPERATOR_CUSTOMER_EXISTS.newException();
        }
        OperatorDo operatorDo = convertOperatorUpdate(oldOperatorDo, operatorUpdate);
        updateById(operatorDo);
        Operator operator = convertOperatorDo(operatorDo);
        return operator;
    }

    private List<Operator> convertOperatorDos(List<OperatorDo> operatorDos) {
        List<Operator> operators = new ArrayList<>();
        operatorDos.forEach(
                operatorDo -> {
                    Operator operator = new Operator();
                    BeanUtils.copyProperties(operatorDo, operator);
                    operators.add(operator);
                }
        );
        return operators;
    }

    private OperatorDo convertOperatorUpdate(OperatorDo operatorDo, OperatorUpdate operatorUpdate) {
        BeanUtils.copyProperties(operatorUpdate, operatorDo);
        return operatorDo;
    }

    private OperatorDo convertOperatorAdd(OperatorAdd operatorAdd) {
        OperatorDo operatorDo = new OperatorDo();
        BeanUtils.copyProperties(operatorAdd, operatorDo);
        return operatorDo;
    }

    private Operator convertOperatorDo(OperatorDo operatorDo) {
        Operator operator = new Operator();
        BeanUtils.copyProperties(operatorDo, operator);
        return operator;
    }
}
