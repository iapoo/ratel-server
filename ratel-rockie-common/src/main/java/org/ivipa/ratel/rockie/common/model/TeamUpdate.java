package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TeamUpdate implements Serializable {

    private static final long serialVersionUID = -8972208975660941395L;

    /**
     * Team Id
     */
    private Long teamId;

    /**
     * Team Name
     */
    private String teamName;

    /**
     * Remark
     */
    private String remark;

}
