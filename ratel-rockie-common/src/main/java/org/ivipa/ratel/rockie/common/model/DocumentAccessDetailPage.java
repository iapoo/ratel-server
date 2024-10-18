package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import org.ivipa.ratel.common.model.BasePage;

@Data
public class DocumentAccessDetailPage extends BasePage {

    private static final long serialVersionUID = -4282708463569950761L;

    private Long documentId;

    private String like;
}
