package com.intelligent.vo;

import lombok.Data;

import java.util.Map;

/**
 * @Date 2025/4/13
 * @Version 1.0
 */
@Data
public class RequestVo {

    /**
     * 模板ID
     */
    private String id;


    /**
     * 请求参数
     */
    private Map<String,Object> map;

}
