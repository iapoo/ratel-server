package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TeamAdd implements Serializable {

    private static final long serialVersionUID = 1155129496650194986L;


    /**
     * Team Name
     */
    private String teamName;

    /**
     * Remark
     */
    private String remark;


}
