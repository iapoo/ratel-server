package org.ivipa.ratel.rockie.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamMemberDelete implements Serializable {

    private static final long serialVersionUID = 8893169054752526842L;

    /**
     * Team Id
     */
    private Long teamId;

    /**
     * Customer Id
     */
    private Long customerId;

}
