package org.ivipa.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class GoogleFont {
    private static final long serialVersionUID = -8264252101185666228L;

    private String family;

    private String[] variants;

    private String[] subsets;

    private String version;

    private String lastModified;

    private Map<String, String> files;

    private String category;

    private String kind;

    private String menu;

    private GoogleFontAxis[] axes;
}
