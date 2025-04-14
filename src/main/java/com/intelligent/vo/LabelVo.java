package com.intelligent.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intelligent.entity.AgentUrlParameter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 标签VO
 *
 * @Date 2025/4/13
 * @Version 1.0
 * @author Mysql
 */
@Data
public class LabelVo {


    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String labelName;

    /**
     * 标签类型
     */
    private String type;

    /**
     * 调用URL/API
     */
    private String callUrl;

    /**
     * 请求方式 GET POST
     */
    private String requestType;

    /**
     * 请求参数参数类型 表单/JSON
     */
    private String parameterType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    /**
     * 入参集合
     */
    private List<AgentUrlParameter> requestList;

    /**
     * 反参集合
     */
    private List<AgentUrlParameter> responseList;
}
