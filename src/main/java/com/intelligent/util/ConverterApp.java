package com.intelligent.util;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ConverterApp {
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
//                    markdown.append("#".repeat(level)).append(" ").append(text).append("\n");
                } else {
                    // 普通段落
                    markdown.append(text).append("\n");
                }
            }
        }
        return markdown.toString();
    }

    public static void main(String[] args) {
        try {
            String markdown = convertToMarkdown("example.docx");
            System.out.println(markdown);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}