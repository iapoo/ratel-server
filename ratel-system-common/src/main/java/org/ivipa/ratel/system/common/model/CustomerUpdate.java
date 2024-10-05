package org.ivipa.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CustomerUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Customer Id
     */
    private Long customerId;

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
     * 备注说明
     */
    private String remark;

}
