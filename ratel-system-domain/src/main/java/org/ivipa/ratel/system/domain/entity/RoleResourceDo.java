package org.ivipa.ratel.system.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipa.ratel.common.config.StandardLocalDateTime;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Accessors(chain = true)
public class RoleResourceDo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 资源ID
     */
    private Long resourceId;


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
