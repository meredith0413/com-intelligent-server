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
 * @description �ļ�����
 * @time 2025/4/14
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class OssFileController {

    @Autowired
    private OssFileService ossFileService;

    /**
     * �ϴ��ļ�
     *
     * @param file MultipartFile�ļ�ʵ��
     * @return OssFile �ļ�������Ϣ
     */
    @PostMapping("/updateFile")
    public RestResponse updateFile(@RequestPart("file") MultipartFile file){
        OssFile ossFile = ossFileService.updateFile(file);
        log.info("�����ļ��ϴ���Σ�" + JSONUtil.toJsonStr(ossFile));
        boolean result = ossFileService.save(ossFile);
        log.info("�ļ��ϴ��ɹ���" + JSONUtil.toJsonStr(ossFile));
        if (result) {
            return RestResponse.successOk(ossFile);
        }
        return RestResponse.failureError();
    }

    /**
     * ��ȡ�ļ���ϸ��Ϣ
     *
     * @param id �ļ�ID
     * @return �ļ���Ϣ
     */
    @GetMapping("/getFileById")
    public RestResponse getFileById(@RequestParam("id")Long id){
        OssFile file = ossFileService.getFileById(id);
        return RestResponse.successOk(file);
    }

    /**
     * �����ļ�·�������ļ�
     *
     * @param req
     * @param resp
     * @param path �ļ�·�� oss objectName
     */
    @GetMapping("/downloadFilePath")
    public void downloadFilePath(HttpServletRequest req, HttpServletResponse resp,@RequestParam("path") String path){
        byte[] fileBytes = ossFileService.downloadFile(path);
        if (fileBytes == null) {
            throw new RuntimeException("�ļ��쳣");
        }
        String fileName = path.substring(path.lastIndexOf("/"));
        //���d�ļ�
        OssFileUtil.downloadFile(req,resp,fileBytes,fileName);
    }

    @GetMapping("/getFileStream")
    public void getFileStream(@RequestParam("path") String path){
        byte[] fileBytes = ossFileService.downloadFile(path);
        if (fileBytes == null) {
            throw new RuntimeException("�ļ��쳣");
        }
    }

    /**
     * �����ļ�path ���ļ�ת��markdown�ַ���
     * @param path
     * @return
     */
    @GetMapping("/getDocxMarkdown")
    public RestResponse getDocxMarkdown(@RequestParam("path") String path){
        byte[] fileBytes = ossFileService.downloadFile( path);
        if (fileBytes == null) {
            throw new RuntimeException("�ļ��쳣");
        }
        String str = DocxMarkdown.convertWordToMarkdownByByte(fileBytes);
        return RestResponse.builder().build().setResultBody(str);
    }


}
