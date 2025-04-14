package com.intelligent.vo;

import lombok.Data;

/**
 * 修改模板请求类
 *
 * @Date 2025/4/13
 * @Version 1.0
 * @author mksql
 */
@Data
public class TemplateChangeVo {

    /**
     * 原始文件路径
     */
    private String path;

    /**
     *别的参数待定
     */
    private String str;
}
