package com.intelligent.util;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DocxToMarkdown {

    public static String convertToMarkdown(String filePath) throws IOException {
        StringBuilder markdown = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            // 遍历文档中的段落
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();
                if (text.isEmpty()) {
                    continue;
                }

                // 判断段落样式（标题、列表等）
                String style = paragraph.getStyle();
                if (style != null && style.toLowerCase().contains("heading")) {
                    // 标题
                    int level = Integer.parseInt(style.replaceAll("\\D", ""));
                    markdown.append(repeat("#",level)).append(" ").append(text).append("\n");
                } else {
                    // 普通段落
                    markdown.append(text).append("\n");
                }
            }
        }
        return markdown.toString();
    }
    public static String repeat(String str, int times) {
        if (times < 0) {
            throw new IllegalArgumentException("times must be >= 0");
        }
        return String.join("", Collections.nCopies(times, str));
    }

    public static void main(String[] args) {
        try {
            String markdown = convertToMarkdown("C:\\Users\\86183\\Desktop\\javaXM\\com-intelligent-server\\src\\main\\java\\com\\intelligent\\util\\out_markdown_test23.docx");
            System.out.println(markdown);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
