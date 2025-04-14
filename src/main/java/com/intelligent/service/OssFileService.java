package com.intelligent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.intelligent.entity.OssFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OssFileService extends IService<OssFile> {

    /**
     * 上传文件
     *
     * @param file MultipartFile文件实体
     * @return OssFile 文件详情信息
     */
    OssFile updateFile(MultipartFile file);

    /**
     * 根据path 获取byte数组
     *
     * @param path 文件路径
     * @return byte 文件byte数组
     */
    byte[] downloadFile(String path);

    /**
     * 获取文件详细信息
     *
     * @param id 文件ID
     * @return 文件详细信息
     */
    OssFile getFileById(Long id);
}
