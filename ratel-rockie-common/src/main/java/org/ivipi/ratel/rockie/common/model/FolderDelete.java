package org.ivipi.ratel.rockie.common.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipi.ratel.common.model.BasePage;

@Data
@Accessors(chain = true)
public class FolderDelete extends BasePage {


    private static final long serialVersionUID = 1L;

    /**
     * id 自增主键
     */
    private Long folderId;


}
