package org.ivipa.ratel.system.common.utils;

import org.ivipa.ratel.common.utils.BaseException;

public enum SystemError {


    SYSTEM_UNKNOWN_EXCEPTION("System", "999001", "Unknown error detected, please contact system administrator."),
    SYSTEM_BAD_PARAMETERS_EXCEPTION("System", "999002", "Bad parameters detected."),
    SYSTEM_ID_NOT_FOUND("System", "999003", "ID not found."),
    SYSTEM_LOGIN_FAILED("System", "999004", "User login failed."),
    SYSTEM_TOKEN_NOT_FOUND("System", "999003", "Token not found"),
    SYSTEM_ID_IS_INVALID("System", "999003", "ID is invalid"),

    CUSTOMER_CUSTOMER_ID_IS_NOT_NULL("System", "001001", "ID is not null"),
    CUSTOMER_CUSTOMER_ID_IS_NULL("System", "001002", "ID is null"),
    CUSTOMER_CUSTOMER_NAME_IS_NULL("System", "001003", "Customer name is null"),
    CUSTOMER_CUSTOMER_PASSWORD_IS_INVALID("System", "001004", "Invalid password."),
    CUSTOMER_CUSTOMER_IS_INVALID("System", "001005", "Customer is invalid"),
    CUSTOMER_CUSTOMER_PASSWORD_IS_INCORRECT("System", "001006", "Password is incorrect."),
    CUSTOMER_CUSTOMER_NOT_FOUND("System", "001007", "Customer not found."),
    CUSTOMER_CUSTOMER_NAME_EXISTS("System", "001003", "Customer name already exists."),
    CUSTOMER_EMAIL_EXISTS("System", "001003", "Email already exists."),
    CUSTOMER_CUSTOMER_NAME_CHANGE_NOT_ALLOWED("System", "001003", "Customer name change not allowed."),

    PRODUCT_PRODUCT_NOT_FOUND("System", "003001", "Product not found."),
    PRODUCT_PRODUCT_ID_IS_NULL("System", "003002", "Product ID  is null"),
    PRODUCT_PRODUCT_NAME_EXISTS("System", "003003", "Product name already exists."),
    PRODUCT_PRODUCT_ENABLED_NOT_FOUND("System", "003003", "Enabled product not setup"),

    LICENSE_LICENSE_ID_IS_NULL("System", "003002", "License ID is null"),
    LICENSE_PRODUCT_ID_IS_NULL("System", "003002", "Product ID is null"),
    LICENSE_LICENSE_NOT_FOUND("System", "003002", "License not found."),
    CONTENT_CONTENT_NOT_FOUND("System", "003001", "Document content not found."),
    CONTENT_CONTENT_ID_IS_NULL("System", "003002", "Document content ID is null"),

    VERIFICATION_CODE_IS_INVALID("System", "003002", "Verification code is invalid."),
    VERIFICATION_CODE_NOT_FOUND("System", "003002", "Verification code not found."),


    OPERATOR_INVALID_OPERATOR_REQUEST("System", "003005", "Invalid operator request"),
    OPERATOR_OPERATOR_TYPE_IS_INVALID("System", "003007", "Operator type is invalid"),
    OPERATOR_CUSTOMER_EXISTS("System", "003001", "Customer already exists"),
    OPERATOR_OPERATOR_NOT_FOUND("System", "003002", "Operator not found"),
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
