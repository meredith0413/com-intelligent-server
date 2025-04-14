package com.intelligent.util;

import org.apache.poi.xwpf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * 将dword转成markdown格式
 */
public class DocxMarkdown {

    /**
     * docx->markdown
     * @param bytes
     */
    public static String convertWordToMarkdownByByte(byte[] bytes) {
        StringBuilder markdown = new StringBuilder();
        try (InputStream fis = new ByteArrayInputStream(bytes);
             XWPFDocument document = new XWPFDocument(fis)) {
            // 遍历文档的所有元素（段落、表格等）
            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph) {
                    // 处理段落
                    XWPFParagraph paragraph = (XWPFParagraph) element;
                    String text = paragraph.getText().trim();
                    if (text.isEmpty()){
                        continue;
                    }
                    String style = paragraph.getStyle();
                    if (style != null && style.toLowerCase().contains("heading")) {
                        // 处理标题
                        int level = Integer.parseInt(style.replaceAll("\\D", ""));
                        markdown.append(repeat("#",level)).append(" ").append(text).append("\n");
                    } else {
                        // 处理普通段落
                        markdown.append(text).append("\n\n");
                    }
                } else if (element instanceof XWPFTable) {
                    // 处理表格
                    XWPFTable table = (XWPFTable) element;
                    markdown.append("\n");
                    for (int i = 0; i < table.getRows().size(); i++) {
                        XWPFTableRow row = table.getRow(i);
                        List<XWPFTableCell> cells = row.getTableCells();
                        StringBuilder tableRow = new StringBuilder();

                        for (XWPFTableCell cell : cells) {
                            tableRow.append("| ").append(cell.getText().trim()).append(" ");
                        }
                        tableRow.append("|");
                        markdown.append(tableRow).append("\n");
                        // 添加表头分隔符（仅对第一行）
                        if (i == 0) {
                            StringBuilder separator = new StringBuilder();
                            for (int j = 0; j < cells.size(); j++) {
                                separator.append("| --- ");
                            }
                            separator.append("|");
                            markdown.append(separator).append("\n");
                        }
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
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
            String markdown = convertWordToMarkdown("C:\\Users\\86183\\Desktop\\javaXM\\222.docx");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output2.md"))) {
            writer.write(markdown);
            System.out.println("Markdown内容已成功写入文件 output.md");
        } catch (IOException e) {
            e.printStackTrace();
        }
            System.out.println(markdown);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertWordToMarkdown(String filePath) throws IOException {
        StringBuilder markdown = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            // 遍历文档的所有元素（段落、表格等）
            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph) {
                    // 处理段落
                    XWPFParagraph paragraph = (XWPFParagraph) element;
                    String text = paragraph.getText().trim();
                    if (text.isEmpty()) continue;

                    String style = paragraph.getStyle();
                    if (style != null && style.toLowerCase().contains("heading")) {
                        // 处理标题
                        int level = Integer.parseInt(style.replaceAll("\\D", ""));
                        markdown.append(repeat("#",level)).append(" ").append(text).append("\n");
                    } else {
                        // 处理普通段落
                        markdown.append(text).append("\n\n");
                    }
                } else if (element instanceof XWPFTable) {
                    // 处理表格
                    XWPFTable table = (XWPFTable) element;
                    markdown.append("\n");
                    for (int i = 0; i < table.getRows().size(); i++) {
                        XWPFTableRow row = table.getRow(i);
                        List<XWPFTableCell> cells = row.getTableCells();
                        StringBuilder tableRow = new StringBuilder();

                        for (XWPFTableCell cell : cells) {
                            tableRow.append("| ").append(cell.getText().trim()).append(" ");
                        }
                        tableRow.append("|");
                        markdown.append(tableRow).append("\n");

                        // 添加表头分隔符（仅对第一行）
                        if (i == 0) {
                            StringBuilder separator = new StringBuilder();
                            for (int j = 0; j < cells.size(); j++) {
                                separator.append("| --- ");
                            }
                            separator.append("|");
                            markdown.append(separator).append("\n");
                        }
                    }
                }
            }

        }
        return markdown.toString();
    }
}
