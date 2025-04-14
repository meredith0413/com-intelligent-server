package com.intelligent.service.Impl;


import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelligent.config.OssConfig;
import com.intelligent.entity.OssFile;
import com.intelligent.mapper.OssFileMapper;
import com.intelligent.service.OssFileService;
import com.intelligent.util.DateUtil;
import com.intelligent.util.OssFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Slf4j
@Service
public class OssFileServiceImpl extends ServiceImpl<OssFileMapper, OssFile> implements OssFileService {

    @Autowired
    private OssConfig ossConfig;

    /**
     * 上传文件
     *
     * @param file MultipartFile文件实体
     * @return OssFile 文件详情信息
     */
    @Override
    public OssFile updateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不存在");
        }
        OssFile ossFile = new OssFile();

        StringBuffer buffer = new StringBuffer("file/");
        //获取相关配置
        String bucketName = ossConfig.getBucketName();
        String endPoint = ossConfig.getEndPoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        //创建OSS对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        LocalDate time = LocalDate.now();
        //yyyyMMdd格式字符串
        String format = DateUtil.dft.format(time);
        String fileName = generateUUID();
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        //ObjectName 格式 file/日期/唯一ID文件名.suffix
        String filePath = buffer.append(format).append("/").append(fileName).append(".").append(suffix).toString();
        try {
            PutObjectResult result = ossClient.putObject(bucketName, filePath, file.getInputStream());
            //拼装返回路径
            if (result != null) {
                String url =  "http://"+bucketName+"."+endPoint.substring(endPoint.lastIndexOf("//") + 2, endPoint.length() - 1)+"/"+filePath;
                ossFile.setFileName(originalFilename);
                ossFile.setFileSize(file.getSize());
                ossFile.setPath(filePath);
                ossFile.setSuffix(suffix);
                ossFile.setBucketName(bucketName);
                ossFile.setUrl(url);
                return ossFile;
            }
        } catch (IOException e) {
            log.error("文件上传失败:{}",e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }

    /**
     * 获取随机字符串
     * @return
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 根据path 获取byte数组
     *
     * @param path 文件路径
     * @return byte 文件byte数组
     */
    @Override
    public byte[] downloadFile(String path) {
        byte[] fileBytes = null;
        if (StringUtil.isBlank(path)) {
            return fileBytes;
        }
        String endPoint = ossConfig.getEndPoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        //创建OSS对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元数据以及一个输入流。
            OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), path);
            InputStream inputStream = ossObject.getObjectContent();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // 读取文件内容到字节数组。
            byte[] readBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(readBuffer)) != -1) {
                byteArrayOutputStream.write(readBuffer, 0, bytesRead);
            }
            // 获取最终的字节数组。
            fileBytes = byteArrayOutputStream.toByteArray();
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            inputStream.close();
            byteArrayOutputStream.close();
            // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            ossObject.close();
        } catch (OSSException oe) {
            log.error("下载上传失败:{}",oe.getMessage());
        } catch (Throwable ce) {
            log.error("下载上传失败:{}",ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return fileBytes;
    }

    /**
     * 获取文件详细信息
     *
     * @param id 文件ID
     * @return 文件详细信息
     */
    @Override
    public OssFile getFileById(Long id) {
        OssFile ossFile = this.getById(id);
        String endPoint = ossConfig.getEndPoint();
        String url =  "http://"+ossConfig.getBucketName()+"."+endPoint.substring(endPoint.lastIndexOf("//") + 2, endPoint.length() - 1)+"/"+ossFile.getPath();
        ossFile.setUrl(url);
        return ossFile;
    }
}
