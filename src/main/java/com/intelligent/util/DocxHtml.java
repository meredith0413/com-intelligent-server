//package com.intelligent.util;
//
//
//import com.overzealous.remark.Remark;
//import org.zwobble.mammoth.DocumentConverter;
//import org.zwobble.mammoth.Result;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Set;
//
////将docx-> html -> markdown
//public class DocxHtml {
//
//
//    public static void main(String[] args) throws IOException {
//
//
//        DocumentConverter converter = new DocumentConverter();
//        Result<String> result = converter.convertToHtml(new File("C:\\Users\\86183\\Desktop\\javaXM\\222.docx"));
//        String html = result.getValue(); // 生成的HTML
//        Set<String> warnings = result.getWarnings(); // 转换过程中的警告
//        System.out.print(html);
//
//
//        // 创建文件目录
//        File file = new File("C:\\Users\\86183\\Desktop\\javaXM\\output2.html");
//        file.getParentFile().mkdirs();
//
//        // 使用BufferedWriter写入文件
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
//            writer.write(html);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Remark remark = new Remark();
//        String markdown = remark.convertFragment(html);
//        System.out.println(markdown);
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output2.md"))) {
//            writer.write(markdown);
//            System.out.println("Markdown内容已成功写入文件 output.md");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
