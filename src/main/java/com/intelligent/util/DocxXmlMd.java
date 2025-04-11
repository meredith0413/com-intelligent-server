//package com.intelligent.util;
//
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFTableCell;
//import org.apache.poi.xwpf.usermodel.XWPFTableRow;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.List;
//public class DocxXmlMd {
//
//    public static void main(String[] args) {
//        String docxFilePath = "example.docx";
//        String xmlFilePath = "example.xml";
//        String markdownFilePath = "example.md";
//
//        try {
//            // 将docx转换为xml
//            convertDocxToXml(docxFilePath, xmlFilePath);
//
//            // 将xml转换为markdown
//            convertXmlToMarkdown(xmlFilePath, markdownFilePath);
//
//            System.out.println("转换完成！");
//        } catch (IOException | DocumentException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void convertDocxToXml(String docxFilePath, String xmlFilePath) throws IOException {
//        try (FileInputStream fis = new FileInputStream(docxFilePath);
//             XWPFDocument document = new XWPFDocument(fis)) {
//
//            Document xmlDocument = DocumentHelper.createDocument();
//            Element root = xmlDocument.addElement("document");
//
//            // 处理段落
//            for (XWPFParagraph paragraph : document.getParagraphs()) {
//                String text = paragraph.getText().trim();
//                if (!text.isEmpty()) {
//                    Element paragraphElement = root.addElement("paragraph");
//                    paragraphElement.setText(text);
//                }
//            }
//
//            // 处理表格
//            for (int i = 0; i < document.getNumberOfTables(); i++) {
//                XWPFTable table = document.getTableArray(i);
//                Element tableElement = root.addElement("table");
//
//                for (int j = 0; j < table.getRows().size(); j++) {
//                    XWPFTableRow row = table.getRow(j);
//                    Element rowElement = tableElement.addElement("row");
//
//                    for (int k = 0; k < row.getTableCells().size(); k++) {
//                        XWPFTableCell cell = row.getCell(k);
//                        Element cellElement = rowElement.addElement("cell");
//                        cellElement.setText(cell.getText().trim());
//                    }
//                }
//            }
//
//            // 保存xml文件
//            FileWriter xmlWriter = new FileWriter(xmlFilePath);
//            xmlDocument.write(xmlWriter);
//            xmlWriter.close();
//        }
//    }
//
//    public static void convertXmlToMarkdown(String xmlFilePath, String markdownFilePath) throws IOException, DocumentException {
//        org.dom4j.Document xmlDocument = DocumentHelper.parseText(new String(new FileInputStream(xmlFilePath).readAllBytes()));
//
//        StringBuilder markdown = new StringBuilder();
//        Element root = xmlDocument.getRootElement();
//
//        for (Object obj : root.elements()) {
//            Element element = (Element) obj;
//
//            if ("paragraph".equals(element.getName())) {
//                markdown.append(element.getText()).append("\n\n");
//            } else if ("table".equals(element.getName())) {
//                markdown.append("|");
//                for (Object tableObj : element.elements()) {
//                    Element tableElement = (Element) tableObj;
//                    if ("row".equals(tableElement.getName())) {
//                        for (Object cellObj : tableElement.elements()) {
//                            Element cellElement = (Element) cellObj;
//                            if ("cell".equals(cellElement.getName())) {
//                                markdown.append(" ").append(cellElement.getText()).append(" |");
//                            }
//                        }
//                        markdown.append("\n");
//                    }
//                }
//                markdown.append("\n");
//            }
//        }
//
//        // 保存markdown文件
//        FileWriter markdownWriter = new FileWriter(markdownFilePath);
//        markdownWriter.write(markdown.toString());
//        markdownWriter.close();
//    }
//}
