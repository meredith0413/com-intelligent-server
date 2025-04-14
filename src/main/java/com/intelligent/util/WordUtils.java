package com.intelligent.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordUtils {
/*    public static void main(String[] args) throws IOException, InvalidFormatException {
        // TODO Auto-generated method stub
        String filepathString = "***.docx";
        String destpathString = "***_result.docx";
        Map<String, String> map = new HashMap<String, String>();
        map.put("${text_1}", "I hava a pen");
        map.put("${text_2}", "I have an apple");
        map.put("${text_3}", "pen apple and pen");
        OPCPackage pack = POIXMLDocument.openPackage(filepathString);
        XWPFDocument document = new XWPFDocument(pack);

        *//**
         * 对段落中的标记进行替换
         *//*
        List<XWPFParagraph> parasList = document.getParagraphs();
        replaceInParagraph(parasList, map);

        *//**
         * 对表格中的标记进行替换
         *//*
//        List<XWPFTable> tables = document.getTables();
//        replaceInTables(tables, map);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(destpathString);
            document.write(outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    /**
     * 替换段落中的字符串
     *
     * @param xwpfParagraph
     * @param oldString
     * @param newString
     */
    public static void replaceInParagraph(XWPFParagraph xwpfParagraph, String oldString, String newString) {
        Map<String, Integer> pos_map = findSubRunPosInParagraph(xwpfParagraph, oldString);
        if (pos_map != null) {
            System.out.println("start_pos:" + pos_map.get("start_pos"));
            System.out.println("end_pos:" + pos_map.get("end_pos"));

            List<XWPFRun> runs = xwpfParagraph.getRuns();
            XWPFRun modelRun = runs.get(pos_map.get("end_pos"));
            XWPFRun xwpfRun = xwpfParagraph.insertNewRun(pos_map.get("end_pos") + 1);
            xwpfRun.setText(newString);
            System.out.println("字体大小：" + modelRun.getFontSize());
            if (modelRun.getFontSize() != -1) xwpfRun.setFontSize(modelRun.getFontSize());//默认值是五号字体，但五号字体getFontSize()时，返回-1
            xwpfRun.setFontFamily(modelRun.getFontFamily());
            for (int i = pos_map.get("end_pos"); i >= pos_map.get("start_pos"); i--) {
                System.out.println("remove run pos in :" + i);
                xwpfParagraph.removeRun(i);
            }
        }
    }

    /**
     * 找到段落中子串的起始XWPFRun下标和终止XWPFRun的下标
     *
     * @param xwpfParagraph
     * @param substring
     * @return
     */
    public static Map<String, Integer> findSubRunPosInParagraph(XWPFParagraph xwpfParagraph, String substring) {

        List<XWPFRun> runs = xwpfParagraph.getRuns();
        int start_pos = 0;
        int end_pos = 0;
        String subtemp = "";
        for (int i = 0; i < runs.size(); i++) {
            subtemp = "";
            start_pos = i;
            for (int j = i; j < runs.size(); j++) {
                if (runs.get(j).getText(runs.get(j).getTextPosition()) == null)
                    continue;
                subtemp += runs.get(j).getText(runs.get(j).getTextPosition());
                if (subtemp.equals(substring)) {
                    end_pos = j;
                    Map<String, Integer> map = new HashMap<>();
                    map.put("start_pos", start_pos);
                    map.put("end_pos", end_pos);
                    return map;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
//        String filePath = "C:\\Users\\86183\\Desktop\\javaXM\\222.docx";
//
//        Map<String,String> map = new HashMap<>();
//        map.put("按照近期招标价格计列主要设备、材料价格，技术经济指标和工程投资合理","按照近期招标价格计列主要设备、材料价格，技术经济指标和工程投资合理{{tihuan}}");
//        wordTextSubstitution(filePath,"DOCX",map);

        String inputFilePath = "input.docx";
        String outputFilePath = "output.doc";

//        try (FileInputStream fis = new FileInputStream(inputFilePath);
//             FileOutputStream fos = new FileOutputStream(outputFilePath)) {
//            XWPFDocument document = new XWPFDocument(fis);
//            Converter.getInstance().convert(document, fos, ConverterParameters.defaultParams());
//        }










    }

    /**
     * @param filePath 替换文件所在路径
     * @param formart  替换文件扩展名
     * @param map      替换数据集合
     * @description: 替换word中的文字
     * @author: Mr.Jkx
     * @time: 2023/1/10 13:19
     */
    public static void wordTextSubstitution(String filePath, String formart, Map<String, String> map) {
        String textPath = "C:\\Users\\86183\\Desktop\\javaXM\\2224444.docx";
        File file = new File(filePath);
        String fileName = file.getName();
        try {
            if ("DOCX".equals(formart)) {
                if (fileName != null && fileName != "") {
                    String name = fileName.substring(0, fileName.length() - 5);
                    textPath = filePath.replaceAll(fileName, name + "_" + System.currentTimeMillis() + ".docx");
                }
                XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(filePath));
                // 替换段落中的指定文字
                Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();

                while (itPara.hasNext()) {
                    XWPFParagraph paragraph = itPara.next();
                    replaceInParagraph(paragraph,"按照近期招标价格计列主要设备、材料价格，技术经济指标和工程投资合理","按照近期招标价格计列主要设备、材料价格，技术经济指标和工程投资合理{{tihuan}}");
//                    paragraph.
//                    paragraph.set
//                    if () {
//
//                    }
//                    List<XWPFRun> runs = paragraph.getRuns();
//                    for (int i = 0; i < runs.size(); i++) {
//                        String oneparaString = runs.get(i).getText(runs.get(i).getTextPosition());
//                        if (oneparaString != null) {
//                            for (Map.Entry<String, String> entry : map.entrySet()) {
//                                oneparaString = oneparaString.replace(entry.getKey(), entry.getValue());
//                            }
//                            runs.get(i).setText(oneparaString, 0);
//                        }
//                    }
                }

                // 创建新文件存放新内容
                FileOutputStream outStream = new FileOutputStream(textPath);
                document.write(outStream);
                outStream.close();
                System.out.println("--- SUCCESS！");
            } else if ("DOC".equals(formart)) {
                if (fileName != null && fileName != "") {
                    String name = fileName.substring(0, fileName.length() - 4);
                    textPath = filePath.replaceAll(fileName, name + "_" + System.currentTimeMillis() + ".doc");
                }
                HWPFDocument document = new HWPFDocument(new FileInputStream(filePath));
                Range range = document.getRange();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    range.replaceText(entry.getKey(), entry.getValue());
                }
                // 创建新文件存放新内容
                FileOutputStream outStream = new FileOutputStream(textPath);
                document.write(outStream);
                outStream.close();
                System.out.println("--- SUCCESS！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
