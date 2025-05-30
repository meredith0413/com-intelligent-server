package com.intelligent.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 文件表
 *
 * @Date 2025/4/14
 * @Version 1.0
 * @author Mysql
 */
@Data
@TableName("oss_file")
public class OssFile {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 存储空间
     */
    private String bucketName;

    /**
     * 全路径
     */
    @TableField(exist = false)
    private String url;

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
     * 逻辑删除字段
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

}
