package org.ivipa.ratel.system.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.ivipa.ratel.common.config.StandardLocalDateTime;
import org.ivipa.ratel.common.model.BasePage;

import java.time.LocalDateTime;

@Data
public class CustomerPage extends BasePage {
    /**
     * 用户名
     */
    private String customerName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 电子邮箱
     */
    private String email;



}
