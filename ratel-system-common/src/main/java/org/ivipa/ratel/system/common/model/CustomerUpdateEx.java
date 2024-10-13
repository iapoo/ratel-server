package org.ivipa.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CustomerUpdateEx implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Customer Id
     */
    private Long customerId;

    private String customerName;

    /**
     * NickName
     */
    private String nickName;

    private String email;

    private String password;

}
