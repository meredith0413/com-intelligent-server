package com.intelligent.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class OssFileUtil {


    public static void downloadFile(HttpServletRequest req, HttpServletResponse resp,byte[] bytes,String fileName) {
        byte[] fileBytes = bytes;
        // 设置响应头
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        resp.setContentLength(fileBytes.length);

        // 将byte数组写入响应输出流
        try (InputStream inputStream = new ByteArrayInputStream(fileBytes);
             ServletOutputStream outputStream = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
