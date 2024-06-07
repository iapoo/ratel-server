package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GoogleFontAxis {
    private static final long serialVersionUID = 330028891749330058L;

    private String tag;

    private String start;

    private String end;
}
