package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DocumentTeamAccessUpdate implements Serializable {

    private static final long serialVersionUID = 4095463897909795920L;


    private Long documentId;

    private Long teamId;

    private Long accessMode;

}
