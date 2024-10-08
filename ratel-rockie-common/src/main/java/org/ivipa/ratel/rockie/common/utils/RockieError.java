package org.ivipa.ratel.rockie.common.utils;

import org.ivipa.ratel.common.utils.BaseException;

public enum RockieError {
    STORAGE_CREATE_DOCUMENT_EXCEPTION("Rockie", "1002001", "文件创建失败异常"),
    STORAGE_GET_DOCUMENT_EXCEPTION("Rockie", "1002001", "文件读取失败异常"),

    DOCUMENT_DOCUMENT_NOT_FOUND("System", "003002", "Document未找到"),
    DOCUMENT_DOCUMENT_ID_IS_NULL("System", "003002", "Document ID为空"),
    DOCUMENT_CUSTOMER_IS_INVALID("System", "003003", "用户无效"),
    DOCUMENT_CONTENT_IS_INVALID("System", "003004", "内容无效"),
    DOCUMENT_FOLDER_NOT_FOUND("System", "003001", "Document Folder未找到"),
    DOCUMENT_DOCUMENT_NAME_EXISTS("System", "003001", "Document已存在"),


    FOLDER_FOLDER_ID_IS_NULL("System", "003001", "Folder ID is empty"),
    FOLDER_FOLDER_NOT_FOUND("System", "003002", "Folder is not found"),
    FOLDER_PARENT_FOLDER_NOT_FOUND("System", "003003", "Parent folder is not found"),
    FOLDER_FOLDER_NAME_EXISTS("System", "003004", "Folder already exists"),

    DOCUMENT_ACCESS_INVALID_DOCUMENT_ACCESS_REQUEST("System", "003005", "Invalid document access request"),
    DOCUMENT_ACCESS_DOCUMENT_ACCESS_NOT_FOUND("System", "003006", "Document access not found"),
    DOCUMENT_ACCESS_ACCESS_MODE_IS_INVALID("System", "003007", "Document access mode is invalid"),;

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
