package org.ivipi.ratel.system.common.utils;

import org.ivipi.ratel.common.utils.BaseException;

public enum SystemError {


    SYSTEM_UNKNOWN_EXCEPTION("System", "999001", "未知异常，请联系我们，我们会尽快处理"),
    SYSTEM_BAD_PARAMETERS_EXCEPTION("System", "999002", "参数不正确"),
    SYSTEM_ID_NOT_FOUND("System", "999003", "ID未找到"),
    SYSTEM_LOGIN_FAILED("System", "999004", "用户名或密码错误"),
    SYSTEM_TOKEN_NOT_FOUND("System", "999003", "Token未找到"),
    SYSTEM_ID_IS_INVALID("System", "999003", "ID无效"),

    CUSTOMER_CUSTOMER_ID_IS_NOT_NULL("System", "001001", "非空ID"),
    CUSTOMER_CUSTOMER_ID_IS_NULL("System", "001002", "空的ID"),
    CUSTOMER_CUSTOMER_NAME_IS_NULL("System", "001003", "名称不可为空"),
    CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID("System", "001004", "密码无效, 必须同时包含大写字母、小写字母、数字或特殊符号（_、#、$)四类中至少3类"),
    CUSTOMER_CUSTOMER_IS_INVALID("System", "001005", "用户无效"),
    CUSTOMER_CUSTOMER_PASSWORD_IS_INCORRECT("System", "001006", "密码无效"),
    CUSTOMER_CUSTOMER_NOT_FOUND("System", "001007", "用户未找到"),
    CUSTOMER_CUSTOMER_NAME_EXISTS("System", "001003", "名称已存在"),
    CUSTOMER_CUSTOMER_NAME_CHANGE_NOT_ALLOWED("System", "001003", "名称不可修改"),

    PRODUCT_PRODUCT_NOT_FOUND("System", "003001", "Product未找到"),
    PRODUCT_PRODUCT_ID_IS_NULL("System", "003002", "Product ID为空"),
    PRODUCT_PRODUCT_NAME_EXISTS("System", "003003", "名称已存在"),

    LICENSE_LICENSE_ID_IS_NULL("System", "003002", "License ID为空"),
    LICENSE_PRODUCT_ID_IS_NULL("System", "003002", "Product ID为空"),
    LICENSE_LICENSE_NOT_FOUND("System", "003002", "License未找到"),

    DOCUMENT_DOCUMENT_NOT_FOUND("System", "003001", "Document未找到"),
    DOCUMENT_DOCUMENT_ID_IS_NULL("System", "003002", "Document ID为空"),
    DOCUMENT_CUSTOMER_IS_INVALID("System", "003003", "用户无效"),
    DOCUMENT_CONTENT_IS_INVALID("System", "003004", "内容无效"),
    DOCUMENT_FOLDER_NOT_FOUND("System", "003001", "Document Folder未找到"),
    DOCUMENT_DOCUMENT_NAME_EXISTS("System", "003001", "Document已存在"),

    CONTENT_CONTENT_NOT_FOUND("System", "003001", "Document内容未找到"),
    CONTENT_CONTENT_ID_IS_NULL("System", "003002", "Document内容ID为空"),

    FOLDER_FOLDER_ID_IS_NULL("System", "003001", "Folder ID为空"),
    FOLDER_FOLDER_NOT_FOUND("System", "003001", "Folder未找到"),
    FOLDER_PARENT_FOLDER_NOT_FOUND("System", "003001", "Parent Folder未找到"),
    FOLDER_FOLDER_NAME_EXISTS("System", "003001", "Folder已存在"),
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
