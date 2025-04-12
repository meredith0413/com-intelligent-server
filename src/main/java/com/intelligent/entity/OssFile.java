package com.intelligent.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@TableName("oss_file")
public class OssFile {

//    private static final long

    private Long id;

    private String fileName;

    private String suffix;

    private Long fileSize;

    //文件路径
    private String path;

    //存储空间
    private String bucketName;

    @TableField(exist = false)
    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @TableLogic(value = "0", delval = "1") // 逻辑删除字段
    private Integer isDelete;

}
