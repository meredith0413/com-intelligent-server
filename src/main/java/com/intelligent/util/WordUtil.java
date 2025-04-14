package com.intelligent.util;

import cn.hutool.core.util.StrUtil;
import com.intelligent.vo.ReplaceTemplateVo;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordUtil {

    /**
     *
     * 更改模板数据 可以细化
     * @param bytes 文件byte数组
     * @param suffix 文件后缀
     * @param list 替换目标
     * @return
     */
    public static byte[] wordTextSubstitutionByByte(byte[] bytes, String suffix, List<ReplaceTemplateVo> list) {
        byte[] wordByte = null;
        try {
            if (StrUtil.equalsIgnoreCase(suffix,"docx")) {
                InputStream fis = new ByteArrayInputStream(bytes);
                XWPFDocument document = new XWPFDocument(fis);
                // 替换段落中的指定文字
                Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
                while (itPara.hasNext()) {
                    XWPFParagraph paragraph = itPara.next();
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (int i = 0; i < runs.size(); i++) {
                        String oneparaString = runs.get(i).getText(runs.get(i).getTextPosition());
                        if (oneparaString != null) {
                            for (ReplaceTemplateVo replaceTemplateVo : list) {
                                oneparaString = oneparaString.replace(replaceTemplateVo.getReplaceStr(), replaceTemplateVo.getTargetStr());
                            }
                            runs.get(i).setText(oneparaString, 0);
                        }
                    }
                }
                // todo 更改表格数据 暂时没有
                // 创建新文件存放新内容
                ByteArrayOutputStream bas = new ByteArrayOutputStream();
                document.write(bas);
                wordByte = bas.toByteArray();
                bas.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordByte;
    }


    public static void main(String[] args) {
        String filePath = "C:\\Users\\86183\\Desktop\\javaXM\\222.docx";

        Map<String,String> map = new HashMap<>();
        map.put("按照近期招标价格计列主要设备、材料价格，技术经济指标和工程投资合理","按照近期招标价格计列主要设备、材料价格，技术经济指标和工程投资合理{{tihuan}}");
        wordTextSubstitution(filePath,"DOCX",map);
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
                    String text = paragraph.getText();

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
