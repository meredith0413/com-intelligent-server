package com.intelligent.controller;

import cn.hutool.json.JSONUtil;
import com.intelligent.common.RestResponse;
import com.intelligent.entity.OssFile;
import com.intelligent.service.OssFileService;
import com.intelligent.util.DocxMarkdown;
import com.intelligent.util.OssFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mysql
 * @description 文件管理
 * @time 2025/4/14
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class OssFileController {

    @Autowired
    private OssFileService ossFileService;

    /**
     * 上传文件
     *
     * @param file MultipartFile文件实体
     * @return OssFile 文件详情信息
     */
    @PostMapping("/updateFile")
    public RestResponse updateFile(@RequestPart("file") MultipartFile file){
        OssFile ossFile = ossFileService.updateFile(file);
        log.info("单个文件上传入参：" + JSONUtil.toJsonStr(ossFile));
        boolean result = ossFileService.save(ossFile);
        log.info("文件上传成功：" + JSONUtil.toJsonStr(ossFile));
        if (result) {
            return RestResponse.successOk(ossFile);
        }
        return RestResponse.failureError();
    }

    /**
     * 获取文件详细信息
     *
     * @param id 文件ID
     * @return 文件信息
     */
    @GetMapping("/getFileById")
    public RestResponse getFileById(@RequestParam("id")Long id){
        OssFile file = ossFileService.getFileById(id);
        return RestResponse.successOk(file);
    }

    /**
     * 根据文件路径下载文件
     *
     * @param req
     * @param resp
     * @param path 文件路径 oss objectName
     */
    @GetMapping("/downloadFilePath")
    public void downloadFilePath(HttpServletRequest req, HttpServletResponse resp,@RequestParam("path") String path){
        byte[] fileBytes = ossFileService.downloadFile(path);
        if (fileBytes == null) {
            throw new RuntimeException("文件异常");
        }
        String fileName = path.substring(path.lastIndexOf("/"));
        //下載文件
        OssFileUtil.downloadFile(req,resp,fileBytes,fileName);
    }

    @GetMapping("/getFileStream")
    public void getFileStream(@RequestParam("path") String path){
        byte[] fileBytes = ossFileService.downloadFile(path);
        if (fileBytes == null) {
            throw new RuntimeException("文件异常");
        }
    }

    /**
     * 根据文件path 把文件转成markdown字符串
     * @param path
     * @return
     */
    @GetMapping("/getDocxMarkdown")
    public RestResponse getDocxMarkdown(@RequestParam("path") String path){
        byte[] fileBytes = ossFileService.downloadFile( path);
        if (fileBytes == null) {
            throw new RuntimeException("文件异常");
        }
        String str = DocxMarkdown.convertWordToMarkdownByByte(fileBytes);
        return RestResponse.builder().build().setResultBody(str);
    }


}
