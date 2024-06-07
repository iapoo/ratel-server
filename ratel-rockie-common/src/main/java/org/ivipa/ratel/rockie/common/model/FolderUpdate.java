package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class FolderUpdate implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * id 自增主键
     */
    private Long folderId;

    /**
     * 用户名
     */
    private String folderName;

    /**
     * 备注说明
     */
    private String remark;


}
