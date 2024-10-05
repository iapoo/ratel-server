package org.ivipa.ratel.system.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerQuery implements Serializable {

    private static final long serialVersionUID = -4558333233124468669L;

    /**
     * Customer Id
     */
    private Long customerId;
}
