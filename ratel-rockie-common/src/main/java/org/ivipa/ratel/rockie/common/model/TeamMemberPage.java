package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class TeamMemberPage extends BasePage {

    private static final long serialVersionUID = -3078843051715189764L;

    /**
     * Team Id
     */
    private Long teamId;
}
