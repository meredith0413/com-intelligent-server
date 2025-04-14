package com.intelligent.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 字典表
 *
 * @Date 2025/4/14
 * @Version 1.0
 * @author Mysql
 */
@Data
@TableName("agent_dict")
public class AgentDict {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 字典类型 1标签类型 2参数类型
     */
    private String type;

    /**
     * 字典key值
     */
    private String key;

    /**
     * 字典VALUE值
     */
    private String value;

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
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;
}
