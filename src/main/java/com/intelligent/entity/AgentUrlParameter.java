package com.intelligent.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 标签入参出参声明类
 *
 * @Date 2025/4/14
 * @Version 1.0
 * @author Mysql
 */
@Data
@TableName("agent_url_parameter")
public class AgentUrlParameter {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标签类
     */
    private Long labelId;

    /**
     * 参数类型 1入参 2反参
     */
    private String type;

    /**
     * 参数名
     */
    private String parameterName;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 参数来源
     */
    private String parameterSource;

    /**
     * 参数是否必填 1 是 2 否
     */
    private Integer isMust;

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
