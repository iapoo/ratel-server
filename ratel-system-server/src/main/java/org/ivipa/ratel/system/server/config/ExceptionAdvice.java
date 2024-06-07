package org.ivipa.ratel.system.server.config;

import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.common.utils.BaseException;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = BaseException.class)
    public Result baseExceptionHandler(BaseException baseException){
        log.error("业务异常:cause:{},message:{},stackTrace:{}",baseException.getCause(),baseException.getMessage(),baseException.getStackTrace());
        Result resultDO = Result.error(baseException);
        return resultDO;
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException httpMessageNotReadableException){
        log.error("参数信息错误:cause:{},message:{},stackTrace:{}",httpMessageNotReadableException.getCause(),httpMessageNotReadableException.getMessage(),httpMessageNotReadableException.getStackTrace());
        Result resultDO = Result.error(SystemError.SYSTEM_BAD_PARAMETERS_EXCEPTION.newException());
        return resultDO;
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e){
        log.error("未知异常:cause:{},message:{},stackTrace:{}",e.getCause(),e.getMessage(),e.getStackTrace());
        Result resultDO = Result.error(SystemError.SYSTEM_UNKNOWN_EXCEPTION.newException());
        return resultDO;
    }
}
