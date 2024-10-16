package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class TeamMemberDetailPage extends BasePage {

    private static final long serialVersionUID = 2955489275145316044L;

    /**
     * Team Id
     */
    private Long teamId;

    private String like;
}
