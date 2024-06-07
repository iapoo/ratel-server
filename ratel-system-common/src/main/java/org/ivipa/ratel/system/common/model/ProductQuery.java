package org.ivipa.ratel.system.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ProductQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
}
