package com.intelligent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.intelligent.entity.OssFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OssFileService extends IService<OssFile> {

    OssFile updateFile(MultipartFile file);

    void downloadFile(HttpServletRequest req, HttpServletResponse resp, String path);
}
