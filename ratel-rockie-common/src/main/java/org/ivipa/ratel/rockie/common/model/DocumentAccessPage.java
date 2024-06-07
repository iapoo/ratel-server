package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class DocumentAccessPage extends BasePage {

    private static final long serialVersionUID = 3905614198292431469L;

    private Long documentId;
}
