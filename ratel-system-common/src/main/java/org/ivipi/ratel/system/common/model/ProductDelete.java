package org.ivipi.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ProductDelete implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
}
