package org.ivipi.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CustomerAdd implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * Settings
     */
    private String settings;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
