package org.ivipi.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LoginCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String name;


}
