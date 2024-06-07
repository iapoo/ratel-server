package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ContentAdd implements Serializable {

    private static final long serialVersionUID = -4130576715994804378L;

    /**
     * 用户名
     */
    private String contentName;

    /**
     * 密码
     */
    private String content;

    /**
     * 备注说明
     */
    private String remark;

}
