package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DocumentTeamAccessAdd implements Serializable {

    private static final long serialVersionUID = 2862215551712950323L;

    private Long documentId;

    private Long teamId;

    private Long accessMode;

}
