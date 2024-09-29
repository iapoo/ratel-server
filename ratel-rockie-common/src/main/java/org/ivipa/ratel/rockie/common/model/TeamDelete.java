package org.ivipa.ratel.rockie.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamDelete implements Serializable {

    private static final long serialVersionUID = -505482897458498396L;

    private Long teamId;
}
