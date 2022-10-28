package org.ivipi.ratel.system.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.ivipi.ratel.common.config.StandardLocalDateTime;
import org.ivipi.ratel.common.dto.PageDto;

import java.time.LocalDateTime;

@Data
public class LicensePageDto extends PageDto {

    /**
     * id 自增主键
     */
    private Long licenseId;

    /**
     * 用户名
     */
    private String licenseName;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    @JsonDeserialize(using = StandardLocalDateTime.StandardLocalDateTimeDeserializer.class)
    @JsonSerialize(using =  StandardLocalDateTime.StandardLocalDateTimeSerializer.class)
    private LocalDateTime effectiveDate;

    @JsonDeserialize(using = StandardLocalDateTime.StandardLocalDateTimeDeserializer.class)
    @JsonSerialize(using =  StandardLocalDateTime.StandardLocalDateTimeSerializer.class)
    private LocalDateTime expireDate;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = StandardLocalDateTime.StandardLocalDateTimeDeserializer.class)
    @JsonSerialize(using =  StandardLocalDateTime.StandardLocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

    /**
     * 修改者
     */
    private Long updatedBy;

    /**
     * 修改时间
     */
    @JsonDeserialize(using = StandardLocalDateTime.StandardLocalDateTimeDeserializer.class)
    @JsonSerialize(using =  StandardLocalDateTime.StandardLocalDateTimeSerializer.class)
    private LocalDateTime updatedDate;


}
