package org.ivipa.ratel.system.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class OperatorPage extends BasePage {

    private static final long serialVersionUID = 7916006131446538559L;

    private String customerName;

    private String email;
}
