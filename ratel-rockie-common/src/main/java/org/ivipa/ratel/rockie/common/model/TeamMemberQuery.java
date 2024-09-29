package org.ivipa.ratel.rockie.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamMemberQuery implements Serializable {

    private static final long serialVersionUID = 5288757161061836226L;

    /**
     * Team Id
     */
    private Long teamId;

    /**
     * CustomerId
     */
    private Long CustomerId;
}
