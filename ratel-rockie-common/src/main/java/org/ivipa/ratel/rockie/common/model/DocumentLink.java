package org.ivipa.ratel.rockie.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DocumentLink implements Serializable {

    private static final long serialVersionUID = 3813974717242736684L;

    private String linkCode;

    private String shareCode;
}
