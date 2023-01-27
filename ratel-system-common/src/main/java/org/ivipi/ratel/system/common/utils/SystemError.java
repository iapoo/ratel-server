package org.ivipi.ratel.system.common.utils;

import org.ivipi.ratel.common.utils.BaseException;

public enum SystemError {


    SYSTEM_UNKNOWN_EXCEPTION("System", "999001", "未知异常，请联系我们，我们会尽快处理"),
    SYSTEM_BAD_PARAMETERS_EXCEPTION("System", "999002", "参数不正确"),
    SYSTEM_ID_NOT_FOUND("System", "999003", "ID未找到"),
    SYSTEM_LOGIN_FAILED("System", "999004", "用户名或密码错误"),
    SYSTEM_TOKEN_NOT_FOUND("System", "999003", "Token未找到"),

    CUSTOMER_CUSTOMER_ID_IS_NOT_NULL("System", "001001", "非空ID"),
    CUSTOMER_CUSTOMER_ID_IS_NULL("System", "001002", "空的ID"),
    CUSTOMER_CUSTOMER_NAME_IS_NULL("System", "001003", "名称不可为空"),
    CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID("System", "001004", "密码无效, 必须同时包含大写字母、小写字母、数字或特殊符号（_、#、$)四类中至少3类"),
    CUSTOMER_CUSTOMER_IS_INVALID("System", "001005", "用户无效"),
    CUSTOMER_CUSTOMER_PASSWORD_IS_INCORRECT("System", "001004", "密码无效"),
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
