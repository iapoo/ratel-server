package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class OperatorAdd implements Serializable {

    private static final long serialVersionUID = -7862563769108181194L;

    /**
     * Customer Id
     */
    private Long customerId;

    /**
     * Operator Type
     */
    private Long operatorType;


}
