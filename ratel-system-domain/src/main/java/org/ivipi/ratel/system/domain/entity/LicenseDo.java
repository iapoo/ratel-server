package org.ivipi.ratel.system.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipi.ratel.common.config.StandardLocalDateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("license")
public class LicenseDo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id 自增主键
     */
    @TableId(value = "license_id", type = IdType.AUTO)
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

    /**
     * 是否删除
     */
    private Boolean deleted;

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
