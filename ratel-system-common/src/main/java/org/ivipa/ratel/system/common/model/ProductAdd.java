package org.ivipa.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ProductAdd implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productName;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;


}
