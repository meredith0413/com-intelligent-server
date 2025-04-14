package com.intelligent.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 自动插件模板表
 *
 * @Date 2025/4/14
 * @Version 1.0
 * @author Mysql
 */
@Data
public class AgentGeneralTemplate {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 部门类型
     */
    private String departmentType;

    /**
     * 原始模板ID
     */
    private String originalFile;

    /**
     * 插入标签的模板ID
     */
    private String updateOriginalFile;

    /**
     * 描述
     */
    private String describe;

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
