package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class DocumentTeamAccessDetailPage extends BasePage {

    private static final long serialVersionUID = -7997243353101710519L;

    private Long documentId;

    private String like;
}
