package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TeamMemberAdd implements Serializable {

    private static final long serialVersionUID = -3125361938890050374L;

    /**
     * Team Id
     */
    private Long teamId;

    /**
     * Customer Id
     */
    private String customerName;

}
