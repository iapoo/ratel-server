package org.ivipa.ratel.system.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class CustomerOperatorPage extends BasePage {

    private static final long serialVersionUID = -3605197274671237560L;

    /**
     * excluded OperatorId Id.
     */
    private Long excludedOperatorId;

    /**
     * Like, for SQL like customer name or email
     */
    private String like;
}
