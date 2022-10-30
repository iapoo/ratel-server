package org.ivipi.ratel.common.utils;


import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 5693037128899703087L;

    private String service;
    private String code;
    private String message;

    public BaseException(String service, String code,String message){
        super();
        this.service = service;
        this.code = code;
        this.message = message;
    }

}
