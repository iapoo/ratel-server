package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DocumentAdd implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户名
     */
    private String documentName;

    /**
     * 密码
     */
    private Long folderId;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 文档内容
     */
    private ContentAdd content;


}
