package org.ivipi.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DocumentAccessDelete implements Serializable {

    private static final long serialVersionUID = -8144565338803107996L;

    private Long documentId;

}
