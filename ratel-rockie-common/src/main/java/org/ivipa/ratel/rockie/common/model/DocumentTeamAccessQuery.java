package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DocumentTeamAccessQuery implements Serializable {

    private static final long serialVersionUID = 2314584080734341885L;

    private Long documentId;

    private Long teamId;
}
