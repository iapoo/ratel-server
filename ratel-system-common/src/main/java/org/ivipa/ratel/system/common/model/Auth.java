package org.ivipa.ratel.system.common.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Auth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Token
     */
    private String token;


    private OnlineCustomer onlineCustomer;



}
