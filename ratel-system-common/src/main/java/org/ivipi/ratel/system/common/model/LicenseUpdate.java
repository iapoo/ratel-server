package org.ivipi.ratel.system.common.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipi.ratel.common.config.StandardLocalDateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LicenseUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id 自增主键
     */
    private Long licenseId;

    private String licenseName;

    private Long customerId;

    private Long productId;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 是否启用
     */
    private Integer isEnabled;
}
