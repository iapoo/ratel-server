package org.ivipa.ratel.system.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;
import org.ivipa.ratel.common.config.StandardLocalDateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OperatorDetail implements Serializable {


    private static final long serialVersionUID = -1873231240615507652L;

    private Long operatorId;

    private Long customerId;

    private Long operatorType;

    private String customerName;

    private String email;

}
