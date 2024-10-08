package org.ivipa.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class VerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private String to;

    private String code;

}
