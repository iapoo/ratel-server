package org.ivipi.ratel.system.common.utils;

import org.ivipi.ratel.common.utils.BaseException;

public enum SystemError {


    SYSTEM_UNKNOWN_EXCEPTION("System", "999001", "未知异常，请联系我们，我们会尽快处理"),
    SYSTEM_BAD_PARAMETERS_EXCEPTION("System", "999003", "参数不正确"),

    CUSTOMER_CUSTOMER_ID_IS_NOT_NULL("System", "001001", "非空客户ID"),
    CUSTOMER_CUSTOMER_ID_IS_NULL("System", "001001", "空的客户ID"),
    ;

    private String service;
    private String code;
    private String message;

    SystemError(String service, String code, String message) {
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
