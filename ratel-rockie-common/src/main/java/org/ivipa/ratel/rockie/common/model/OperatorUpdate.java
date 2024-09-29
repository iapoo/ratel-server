package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class OperatorUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Operator Id
     */
    private Long operatorId;

    /**
     * Customer Id
     */
    private Long customerId;

    /**
     * Operator Type
     */
    private Long operatorType;

}
