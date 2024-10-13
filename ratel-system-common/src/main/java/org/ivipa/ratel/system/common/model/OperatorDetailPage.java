package org.ivipa.ratel.system.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class OperatorDetailPage extends BasePage {

    private static final long serialVersionUID = -3662358942004754796L;

    /**
     * Like for Customer Name or Customer Email
     */
    private String like;
}
