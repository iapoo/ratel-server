package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DocumentAccessAdd implements Serializable {

    private static final long serialVersionUID = -8144565338803107996L;

    private Long documentId;

    private String customerName;

    private Long accessMode;

}
