package org.ivipa.ratel.system.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.ivipa.ratel.common.config.StandardLocalDateTime;
import org.ivipa.ratel.common.model.BasePage;

import java.time.LocalDateTime;

@Data
public class CustomerPage extends BasePage {

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
     * 身份证号
     */
    private String idCard;

    /**
     * 固定电话
     */
    private String telephone;

    /**
     * 移动电话电话
     */
    private String mobile;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 备注说明
     */
    private String remark;


    /**
     * Settings
     */
    private String settings;

    /**
     * 是否启用
     */
    private Boolean enabled;

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
