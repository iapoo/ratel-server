package org.ivipa.ratel.rockie.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipa.ratel.common.config.StandardLocalDateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DocumentLinkDelete implements Serializable {


    private static final long serialVersionUID = -8900388718907986149L;

    private Long documentId;
}
