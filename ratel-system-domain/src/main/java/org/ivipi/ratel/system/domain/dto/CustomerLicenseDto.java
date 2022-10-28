package org.ivipi.ratel.system.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.ivipi.ratel.common.config.StandardLocalDateTime;
import org.ivipi.ratel.common.dto.PageDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CustomerLicenseDto implements Serializable {

    private static final long serialVersionUID = -2826713151630340505L;

    /**
     * id 自增主键
     */
    private Long customerId;

    /**
     * 用户名
     */
    private String customerName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * id 自增主键
     */
    private Long licenseId;

    /**
     * 用户名
     */
    private String licenseName;

}
