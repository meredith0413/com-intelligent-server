package com.intelligent.controller;

import com.intelligent.common.RestResponse;
import com.intelligent.entity.OssFile;
import com.intelligent.service.OssFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class OssFileController {

    @Autowired
    private OssFileService ossFileService;

    @PostMapping("/updateFile")
    public RestResponse updateFile(@RequestPart("file") MultipartFile file){
        OssFile ossFile = ossFileService.updateFile(file);
        boolean save = ossFileService.save(ossFile);
        return RestResponse.builder().build().setResultBody(ossFile);
    }

    @GetMapping("/downloadFilePath")
    public void downloadFilePath(HttpServletRequest req, HttpServletResponse resp,@PathVariable("path") String path){
        ossFileService.downloadFile(req, resp, path);
    }
}
