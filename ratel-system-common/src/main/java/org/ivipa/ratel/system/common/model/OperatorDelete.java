package org.ivipa.ratel.system.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperatorDelete implements Serializable {

    private static final long serialVersionUID = -8976020173965325003L;

    /**
     * OperatorId
     */
    private Long operatorId;
}
