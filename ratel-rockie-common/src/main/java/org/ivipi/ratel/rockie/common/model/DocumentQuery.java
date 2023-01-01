package org.ivipi.ratel.rockie.common.model;

import lombok.Data;
import org.ivipi.ratel.common.model.BasePage;

import java.io.Serializable;

@Data
public class DocumentQuery implements Serializable {

    private static final long serialVersionUID = 3823544864225943337L;

    private Long documentId;
}
