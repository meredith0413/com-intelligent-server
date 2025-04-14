package com.intelligent.vo;

import lombok.Data;

/**
 * @Date 2025/4/13
 * @Version 1.0
 */
@Data
public class ReplaceTemplateVo {

    //需要替换的字符串
    private String replaceStr;

    //目标字符串  被替换目标
    private String targetStr;

    //替换类型 1 文本 2 表格
    private Integer type;

}
