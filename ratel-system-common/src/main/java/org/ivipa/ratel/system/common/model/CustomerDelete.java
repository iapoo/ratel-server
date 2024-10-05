package org.ivipa.ratel.system.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDelete implements Serializable {

    private static final long serialVersionUID = 6855835893619473369L;

    /**
     * CustomerId
     */
    private Long customerId;
}
