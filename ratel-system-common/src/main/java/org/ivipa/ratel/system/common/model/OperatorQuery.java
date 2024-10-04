package org.ivipa.ratel.system.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperatorQuery implements Serializable {

    private static final long serialVersionUID = -2840858295442828130L;

    /**
     * Operator Id
     */
    private Long operatorId;
}
