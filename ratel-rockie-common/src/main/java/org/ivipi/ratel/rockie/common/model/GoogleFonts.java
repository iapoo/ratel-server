package org.ivipi.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GoogleFonts {
    private static final long serialVersionUID = -879257147893265373L;

    private String kind;

    private GoogleFont[] items;
}
