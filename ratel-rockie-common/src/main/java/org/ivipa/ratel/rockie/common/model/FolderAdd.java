package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class FolderAdd implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String folderName;

    /**
     * 密码
     */
    private Long parentId;

    /**
     * 备注说明
     */
    private String remark;


}
