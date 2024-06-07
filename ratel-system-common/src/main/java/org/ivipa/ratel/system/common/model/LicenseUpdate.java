package org.ivipa.ratel.system.common.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LicenseUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id 自增主键
     */
    private Long licenseId;

    private Long customerId;

    private Long productId;

    /**
     * 备注说明
     */
    private String remark;
    /**
     * 是否启用
     */
    private Boolean enabled;

}
