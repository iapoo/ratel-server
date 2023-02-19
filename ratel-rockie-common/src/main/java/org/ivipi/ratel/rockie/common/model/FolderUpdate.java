package org.ivipi.ratel.rockie.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipi.ratel.common.config.StandardLocalDateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FolderUpdate implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * id 自增主键
     */
    private Long folderId;

    /**
     * 用户名
     */
    private String folderName;

    /**
     * 备注说明
     */
    private String remark;


}
