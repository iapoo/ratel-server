package org.ivipa.ratel.rockie.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DocumentDelete implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long documentId;
}
