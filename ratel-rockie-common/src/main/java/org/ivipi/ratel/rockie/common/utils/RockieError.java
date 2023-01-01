package org.ivipi.ratel.rockie.common.utils;

import org.ivipi.ratel.common.utils.BaseException;

public enum RockieError {

    DOCUMENT_DOCUMENT_NOT_FOUND("Rockie", "101001", "文档未找到"),
    ;

    private String service;
    private String code;
    private String message;

    RockieError(String service, String code, String message) {
        this.service = service;
        this.code = code;
        this.message = message;
    }

    public String getService() {
        return this.service;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public BaseException newException() {
        return new BaseException(this.service, this.code, this.message);
    }
}
