package org.ivipi.ratel.system.common.model;

import lombok.Data;
import org.ivipi.ratel.common.model.BasePage;

@Data
public class LicensePage extends BasePage {

    /**
     * id 自增主键
     */
    private Long licenseId;

    private Long customerId;

    private Long productId;


}
