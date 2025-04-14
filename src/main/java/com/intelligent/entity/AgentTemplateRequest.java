package com.intelligent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 模板与标签的关联关系
 *
 * @Date 2025/4/14
 * @Version 1.0
 * @author Mysql
 */
@Data
public class AgentTemplateRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模板ID
     */
    private Long generalTemplateId;

    /**
     * 标签ID
     */
    private Long labelId;

    /**
     * 标签所对应的参数名
     */
    private String parameterName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    /**
     * 逻辑删除 0 存在 1删除
     */
    private Integer isDelete;
}
