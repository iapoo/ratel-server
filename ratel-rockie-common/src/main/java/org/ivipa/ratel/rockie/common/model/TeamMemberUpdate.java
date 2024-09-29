package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TeamMemberUpdate implements Serializable {

    private static final long serialVersionUID = -762124829410331131L;

    /**
     * Team Id
     */
    private Long teamId;

    /**
     * Customer Id
     */
    private Long customerId;

    /**
     * Member Type
     */
    private Long memberType;

}
