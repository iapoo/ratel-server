package org.ivipi.ratel.rockie.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipi.ratel.common.config.StandardLocalDateTime;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class Customer {

    private static final long serialVersionUID = 1L;

    /**
     * id 自增主键
     */
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;

    /**
     * 用户名
     */
    private String customerName;

    /**
     * 密码
     */
    private String password;

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
