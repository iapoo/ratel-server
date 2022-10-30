package org.ivipi.ratel.common.model;

import lombok.Data;
import lombok.ToString;
import org.ivipi.ratel.common.utils.BaseException;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 8445138131498776039L;

    private static final String CODE_SUCCESS = "000000";

    private String code = CODE_SUCCESS;

    private boolean success = true;
    private String message;
    private T data;

    public static Result<String> success(){
        Result<String> result = new Result<>();
        return result;
    }

    public static <T> Result<T> success(T t){
        Result<T> result = new Result<>();
        result.setData(t);
        return result;
    }
    public static <T> Result<T> error(BaseException exception){
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(exception.getCode());
        result.setMessage(exception.getMessage());
        return result;
    }

}