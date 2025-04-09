package com.intelligent.util;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.plugin.markdown.MarkdownRenderData;
import com.deepoove.poi.plugin.markdown.MarkdownRenderPolicy;
import com.deepoove.poi.plugin.markdown.MarkdownStyle;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomifeng1010
 * @version 1.0
 * @date: 2024-08-24 17:23
 * @Description
 */
@UtilityClass
@Slf4j
public class MarkdownUtil {


    /**
     * markdown转html
     *
     * @param markdownContent
     * @return
     */
    public String markdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(document);
        log.info(htmlContent);
        return htmlContent;
    }


    /**
     * 将markdown格式内容转换为word并保存在本地
     *
     * @param markdownContent
     * @param outputFileName
     */
    public void toDoc(String markdownContent, String outputFileName) {
        log.info("markdownContent:{}", markdownContent);
        MarkdownRenderData code = new MarkdownRenderData();
        code.setMarkdown(markdownContent);
        MarkdownStyle style = MarkdownStyle.newStyle();
        style = setMarkdownStyle(style);
        code.setStyle(style);
//      markdown样式处理与word模板中的标签{{md}}绑定
        Map<String, Object> data = new HashMap<>();
        data.put("md", code);

        Configure config = Configure.builder().bind("md", new MarkdownRenderPolicy()).build();
        try {
//            获取classpath
            String path = MarkdownUtil.class.getClassLoader().getResource("").getPath();
            log.info("classpath:{}", path);
            //由于部署到linux上后，程序是从jar包中去读取resources下的文件的，所以需要使用流的方式读取，所以获取流，而不是直接使用文件路径

            // 所以可以这样获取 InputStream resourceAsStream = MarkdownUtil.class.getClassLoader().getResourceAsStream("");
            // 建议使用spring的工具类来获取，如下
            ClassPathResource resource = new ClassPathResource("markdown" + File.separator + "markdown_template.docx");
            InputStream resourceAsStream = resource.getInputStream();
            XWPFTemplate.compile(resourceAsStream, config)
                    .render(data)
                    .writeToFile(path + "out_markdown_" + outputFileName + ".docx");
        } catch (IOException e) {
            log.error("保存为word出错====={}",e.getMessage());
        }

    }

    /**
     * 将markdown转换为word文档并下载
     *
     * @param markdownContent
     * @param response
     * @param fileName
     */
    public void convertAndDownloadWordDocument(String markdownContent, HttpServletResponse response, String fileName) {
        log.info("markdownContent:{}", markdownContent);
        MarkdownRenderData code = new MarkdownRenderData();
        code.setMarkdown(markdownContent);
        MarkdownStyle style = MarkdownStyle.newStyle();
        style = setMarkdownStyle(style);

        code.setStyle(style);
//      markdown样式处理与word模板中的标签{{md}}绑定
        Map<String, Object> data = new HashMap<>();
        data.put("md", code);
        Configure configure = Configure.builder().bind("md", new MarkdownRenderPolicy()).build();

        try {
            fileName=URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            //由于部署到linux上后，程序是从jar包中去读取resources下的文件的，所以需要使用流的方式读取，所以获取流，而不是直接使用文件路径

            // 所以可以这样获取 InputStream resourceAsStream = MarkdownUtil.class.getClassLoader().getResourceAsStream("");
            // 建议使用spring的工具类来获取，如下
            ClassPathResource resource = new ClassPathResource("markdown" + File.separator + "markdown_template.docx");
            InputStream resourceAsStream = resource.getInputStream();
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".docx");
//            contentType不设置也是也可以的，可以正常解析到
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8");
            XWPFTemplate template = XWPFTemplate.compile(resourceAsStream, configure)
                    .render(data);
            template.writeAndClose(response.getOutputStream());
        } catch (IOException e) {
            log.error("下载word文档失败:{}", e.getMessage());
        }
    }


    /**
     * 设置转换为word文档时的基本样式
     * @param style
     * @return
     */
    public MarkdownStyle setMarkdownStyle(MarkdownStyle style) {
//        一定设置为false,不然生成的word文档中各元素前边都会加上有层级效果的一串数字,
//        比如一级标题 前边出现1 二级标题出现1.1 三级标题出现1.1.1这样的数字
        style.setShowHeaderNumber(false);
        // 修改默认的表格样式
        // table header style(表格头部,通常为表格顶部第一行,用于设置列标题)
        RowStyle headerStyle = new RowStyle();
        CellStyle cellStyle = new CellStyle();
//        设置表格头部的背景色为灰色
        cellStyle.setBackgroundColor("cccccc");
        Style textStyle = new Style();
//        设置表格头部的文字颜色为黑色
        textStyle.setColor("000000");
//        头部文字加粗
        textStyle.setBold(true);
//        设置表格头部文字大小为12
        textStyle.setFontSize(12);
//       设置表格头部文字垂直居中
        cellStyle.setVertAlign(XWPFTableCell.XWPFVertAlign.CENTER);

        cellStyle.setDefaultParagraphStyle(ParagraphStyle.builder().withDefaultTextStyle(textStyle).build());
        headerStyle.setDefaultCellStyle(cellStyle);
        style.setTableHeaderStyle(headerStyle);

//        table border style(表格边框样式)
        BorderStyle borderStyle = new BorderStyle();
//        设置表格边框颜色为黑色
        borderStyle.setColor("000000");
//        设置表格边框宽度为3px
        borderStyle.setSize(3);
//        设置表格边框样式为实线
        borderStyle.setType(XWPFTable.XWPFBorderType.SINGLE);
        style.setTableBorderStyle(borderStyle);

//        设置普通的引用文本样式
        ParagraphStyle quoteStyle = new ParagraphStyle();
//        设置段落样式
        quoteStyle.setSpacingBeforeLines(0.5d);
        quoteStyle.setSpacingAfterLines(0.5d);

//        设置段落的文本样式
        Style quoteTextStyle = new Style();
        quoteTextStyle.setColor("000000");
        quoteTextStyle.setFontSize(8);
        quoteTextStyle.setItalic(true);
        quoteStyle.setDefaultTextStyle(quoteTextStyle);
        style.setQuoteStyle(quoteStyle);

        return style;
    }


    public static void main(String[] args) {
//        String markdownContent = "# 一级标题\n" +
//                "## 二级标题\n" +
//                "### 三级标题\n" +
//                "#### 四级标题\n" +
//                "##### 五级标题\n" +
//                "###### 六级标题\n" +
//                "## 段落\n" +
//                "这是一段普通的段落。\n" +
//                "## 列表\n" +
//                "### 无序列表\n" +
//                "- 项目1\n" +
//                "- 项目2\n" +
//                "- 项目3\n" +
//                "### 有序列表\n" +
//                "1. 项目1\n" +
//                "2. 项目2\n" +
//                "3. 项目3\n" +
//                "## 链接\n" +
//                "[百度](https://www.baidu.com)\n" +
//                "## 图片\n" +
//                "![图片描述](https://www.baidu.com/img/bd_logo1.png)\n" +
//                "## 表格\n" +
//                "| 表头1 | 表头2 | 表头3 |\n" +
//                "|-------|-------|-------|\n" +
//                "| 单元格1 | 单元格2 | 单元格3 |\n" +
//                "| 单元格4 | 单元格5 | 单元格6 |";
        String str1 = "## 1. Linux简单介绍\n" +
                "\n" +
                "\u200B\t\tLinux是1991年由芬兰人**林纳斯·托瓦兹**（Linus Torvalds）在赫尔辛基大学上学时出于个人爱好而编写的；\n" +
                "\n" +
                "\u200B\t\tLinux是一个自由免费的，开放源代码的操作系统；\n" +
                "\n" +
                "\u200B\t\tLinux是一个多用户、多任务、支持多线程和多cpu的操作系统；\n" +
                "\n" +
                "\u200B\t\tLinux发行版：`RedHat`、`Ubuntu`、`Suse`、`Debian` ......\n" +
                "\n" +
                "## 2. 安装XShell\n" +
                "\n" +
                "\u200B\t\tSSH客户端：Putty、SecureCRT(收费)、XShell等\n" +
                "\n" +
                "\u200B\t\tXShell 是一个强大的安全终端模拟软件，它支持SSH1，SSH2，以及微软平台的Telnet协议。\n" +
                "\n" +
                "\u200B\t\tXShell 是一个非常好用的SSH客户端，可以作为 Telnet、Rlogin、SSH、SFTP 等协议的安全终端模拟软件，可以让你轻松管理远程主机。\n" +
                "\n" +
                "\u200B\t\tXShell特色功能包括标签化管理远程会话、动态端口转发、自定义键盘映射、VB脚本支持、完全的 Unicode 支持等。\n" +
                "\n" +
                "## 3. Linux的目录\n" +
                "\n" +
                "### 3.1 绝对路径和相对路径\n" +
                "\n" +
                "\u200B\t\tLinux中没有C盘、D盘等盘符；Linux的根目录是：**/**\n" +
                "\n" +
                "1. **绝对路径**\n" +
                "\n" +
                "   从根目录**`/`**开始写起的文件名或者目录名称，比如：`/opt/jdk1.8`\n" +
                "\n" +
                "2. **相对路径**\n" +
                "\n" +
                "   相对于当前路径的文件名或目录名的写法\n" +
                "\n" +
                "#### 3.1.1 特殊目录\n" +
                "\n" +
                "1. <font color=\"red\" size=\"5\">.</font>：表示当前目录，也可以用./表示\n" +
                "2. <font color=\"red\"  size=\"5\">..</font>：表示上一级目录，也可以用../表示\n" +
                "3. <font color=\"red\"  size=\"5\">~</font>：表示用户主目录，相当于与Windows的C:/users/lenovo\n" +
                "4. <font color=\"red\"  size=\"5\">-</font>：上一次所在目录\n" +
                "\n" +
                "### 3.2 Centos的目录\n" +
                "\n" +
                "1. **/bin**：binary二进制，存放二进制可执行的文件（比如cd）\n" +
                "\n" +
                "2. **/boot**：存放开机时用到的各种文件；\n" +
                "\n" +
                "3. /dev：作为访问外部设备文件的接口\n" +
                "\n" +
                "4. **/etc**：存放系统配置文件，如用户密码等（在/etc/passwd文件中）\n" +
                "\n" +
                "5. /home：普通用户的用户主目录，相当于Windows的C:/Users目录\n" +
                "\n" +
                "6. /lib：存放系统种的运行程序所需要的函式库和其它模块；\n" +
                "\n" +
                "7. /media：目录存放自动挂载的硬件，包括软盘、光盘等（挂载点一般是由系统自动建立和删除的）；\n" +
                "\n" +
                "8. /mnt：暂时挂载一些文件系统，作用与/media类似；\n" +
                "\n" +
                "9. **/opt**：可以安装第三方软件的目录；\n" +
                "\n" +
                "10. /proc：虚拟文件系统，它存的数据都在内存中，如系统核心、进程信息等；\n" +
                "\n" +
                "11. **/root**：超级管理员用户root的用户目录；\n" +
                "\n" +
                "12. /run：存放系统开机时所产生的各项信息；\n" +
                "\n" +
                "13. **/sbin**：super binary，存放超级管理员root用户可以执行的二进制文件\n" +
                "\n" +
                "14. /srv：service的缩写，网络服务所需要的的数据目录\n" +
                "\n" +
                "15. /sys：虚拟文件系统，存放与硬件相关的信息，与/proc类似\n" +
                "\n" +
                "16. /tmp：存放临时文件\n" +
                "\n" +
                "17. **/usr**：用于存放系统应用程序，其中\n" +
                "\n" +
                "    /usr/bin就是/bin\n" +
                "\n" +
                "    /usr/lib就是/lib\n" +
                "\n" +
                "    /usr/sbin就是/sbin\n" +
                "\n" +
                "    /usr/local：一般用于安装软件\n" +
                "\n" +
                "18. /var：主要针对常态性变动的文件，包括缓存以及某些软件运作所产生的文件\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "## 4. 用户管理和用户组管理\n" +
                "\n" +
                "\u200B\t\t登录Linux系统需要账号和密码\n" +
                "\n" +
                "### 4.1 用户管理\n" +
                "\n" +
                "\u200B\t\t任何一个需要使用Linux系统资源的用户，都需要用于一个账号，每个账号都有唯一的用户名和密码，管理用户的文件：`/etc/passwd`\n" +
                "\n" +
                "#### 4.1.1 添加用户：`useradd`\n" +
                "\n" +
                "\u200B\t\t添加用户就是在系统中创建一个新用户，然后为新用户分配用户名、用户组、主目录和登录Shell等资源。**刚添加的账号是被锁定的，无法使用。**\n" +
                "\n" +
                "1. 添加用户\n" +
                "\n" +
                "   ```\n" +
                "   [root@192 ~]# useradd etoak\n" +
                "   ```\n" +
                "\n" +
                "   - 默认创建名称为etoak的用户和用户组\n" +
                "\n" +
                "   - 默认在/home下创建一个etoak的用户目录\n" +
                "\n" +
                "   - 在用户文件/etc/passwd中添加etoak用户这一行\n" +
                "\n" +
                "     \n" +
                "\n" +
                "2. 几个参数说明\n" +
                "\n" +
                "   ```\n" +
                "   -d:指定用户的家目录\n" +
                "   -u:指定用户的id\n" +
                "   -g:指定用户组（可以是名称，也可以是用户组id）\n" +
                "   ```\n" +
                "   \n" +
                "\n" +
//                "<img src=\"imgs/image-20211026152109378.png\" alt=\"image-20211026152109378\" style=\"zoom:50%; margin-left:60px\" />  \n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 4.1.2 设置密码：`passwd`\n" +
                "\n" +
                "- 用法：passwd 用户名\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026152307754.png\" alt=\"image-20211026152307754\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 4.1.3 切换用户：`su`\n" +
                "\n" +
                "- switch user\n" +
                "- su 用户名\n" +
                "\n" +
                "   - su - 用户名\n" +
                "\n" +
                "   - `su`和`su -`区别：\n" +
                "\n" +
                "     ```\n" +
                "     su只是切换了用户身份，但Shell环境仍然是切换之前的用户的Shell；\n" +
                "     su -同时切换了用户身份和Shell环境；\n" +
                "     只有切换了Shell环境才不会出现PATH环境变量错误；\n" +
                "     ```\n" +
                "\n" +
                "   \n" +
                "\n" +
                "#### 4.1.4 其它相关命令\n" +
                "- whoami：查看我是谁\n" +
                "\n" +
                "- id命令：查看用户的基本信息，包括用户id、组id等\n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 4.1.5 修改用户：`usermod`\n" +
                "\n" +
                "\u200B\t\t修改用户账号就是根据实际情况更改用户的有关属性，如用户id、主目录、用户组、登录Shell等。\n" +
                "\n" +
                "- 几个参数说明\n" +
                "\n" +
                "  ```\n" +
                "  -u:修改用户的id\n" +
                "  -g:修改用户组（可以是名称，也可以是用户组id）\n" +
                "  -G:修改用户的次要组\n" +
                "  -l:修改用户名\n" +
                "    usermod -l 新用户名 旧用户名\n" +
                "    usermod -l et etoak\n" +
                "  ```\n" +
                "\n" +
                " \n" +
                "\n" +
                "#### 4.1.6 删除用户：userdel\n" +
                "\n" +
                "\u200B\t\t如果一个用户的账号不再使用，可以从系统中删除。删除用户账号就是要将/etc/passwd等系统文件中的该用户记录删除，必要时还删除用户的主目录。\n" +
                "\n" +
                "- 参数说明\n" +
                "\n" +
                "  `-r：表示删除用户的同时删除用户的家目录`\n" +
                "\n" +
                "### 4.2 用户组管理\n" +
                "\n" +
                "\u200B\t\t每个用户都有一个用户组，系统可以对一个用户组中的所有用户进行集中管理。不同Linux 系统对用户组的规定有所不同，Linux下的用户一般属于与它同名的用户组，这个用户组在创建用户时同时创建。\n" +
                "\n" +
                "#### 4.2.1 添加用户组：`groupadd`\n" +
                "\n" +
                "- 参数说明\n" +
                "\n" +
                "  ```\n" +
                "  -g:指定用户组id，与useradd的-u参数类似\n" +
                "  ```\n" +
                "\n" +
                "#### 4.2.2 修改用户组：`groupmod`\n" +
                "\n" +
                "- 参数说明\n" +
                "\n" +
                "  ```\n" +
                "  -g:修改用户组id\n" +
                "  -n:修该用户组名；与usermod的-l参数类似\n" +
                "  ```\n" +
                "#### 4.2.3 删除用户组：`groupdel`\n" +
                "\n" +
                "- 删除刚刚创建的用户组\n" +
                "  `[root@192 home]# groupdel et1`\n" +
                "\n" +
                "  \n" +
                "  \n" +
                "- 删除已包含用户的用户组\n" +
                "  `[root@192 home]# groupdel etoak`\n" +
                "  `groupdel: cannot remove the primary group of user 'et'`\n" +
                "\n" +
                "  \n" +
                "  \n" +
                "- 解决办法：先删除用户组下的用户，然后删除用户组\n" +
                "  `[root@192 home]# userdel -r et`\n" +
                "  `[root@192 home]# groupdel etoak`\n" +
                "  `[root@192 home]# `\n" +
                "\n" +
                "\n" +
                "\n" +
                "## 5. 文件类型与权限问题\n" +
                "\n" +
//                "![image-20210929085653533](imgs/image-20210929085653533.png)\n" +
                "\n" +
                "## 6. 修改文件权限、所属用户和组\n" +
                "\n" +
                "- 详见：`文件权限与修改文件拥有者.pdf文档`\n" +
                "\n" +
                "## 7. Linux常用命令\n" +
                "\n" +
                "### 7.1 pwd\n" +
                "\n" +
                "- `print working directory`：显示当前所在目录\n" +
                "\n" +
                "- 举例\n" +
                "\n" +
                "  ```bash\n" +
                "  [root@localhost etoak]# pwd\n" +
                "  /home/etoak\n" +
                "  ```\n" +
                "\n" +
                "### 7.2. cd\n" +
                "\n" +
                "- change directory\n" +
                "\n" +
                "- cd 目录名\n" +
                "\n" +
                "- 举例\n" +
                "\n" +
                "  1. 绝对路径方式进入/opt目录\n" +
//                "      <img src=\"imgs/image-20211026155544082.png\" alt=\"image-20211026155544082\" style=\"zoom:50%;\" /> \n" +
                "      \n" +
                "  2. 在/opt目录下以相对路径的方式进入/home\n" +
                "  \n" +
//                "       <img src=\"imgs/image-20211026155642230.png\" alt=\"image-20211026155642230\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "- 特殊用法举例\n" +
                "\n" +
                "  ```\n" +
                "  1. 进入上一级目录\n" +
                "  [root@192 home]# cd ..\n" +
                "  [root@192 /]#\n" +
                "  ```\n" +
                "\n" +
                "  ```\n" +
                "  2. 返回上一次所在目录\n" +
                "  [root@192 /]# cd -\n" +
                "  /home\n" +
                "  ```\n" +
                "\n" +
                "  ```\n" +
                "  3. 直接进入用户根目录，切换到root用户的根目录\n" +
                "  [root@192 home]# cd ~\n" +
                "  [root@192 ~]# pwd\n" +
                "  /root\n" +
                "  ```\n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.3 ls\n" +
                "\n" +
                "- list：列出某个目录下的文件和目录\n" +
                "\n" +
                "- `ls [参数] [目录]`\n" +
                "\n" +
                "- 举例\n" +
                "\n" +
                "  1. 参数-l：详细列出某个目录的文件，使用ls -l 或者 ll.\n" +
                "  \n" +
                "  2. 列出指定目录下的文件和目录，列出/home目录的下的文件和目录\n" +
                "  \n" +
                "  3. 参数-a：列出目录下所有文件和目录(包括隐藏文件)\n" +
                "  \n" +
                "     \n" +
                "### 7.4 mkdir\n" +
                "\n" +
                "- make directory：创建目录\n" +
                "\n" +
                "- **创建单个目录**\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026160301786.png\" alt=\"image-20211026160301786\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- **创建多个目录**\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026160340352.png\" alt=\"image-20211026160340352\" style=\"zoom:50%;\" /> \n" +
                "  \n" +
                "- **递归创建子目录**：需要在创建目录的前边加一个 -p参数\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026160445920.png\" alt=\"image-20211026160445920\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "### 7.5 rm\n" +
                "\n" +
                "- 用法：rm [OPTION]... 文件或目录\n" +
                "\n" +
                "  -r：递归删除\n" +
                "\n" +
                "  -f：force，强制删除\n" +
                "\n" +
                "  \n" +
                "\n" +
                "- 示例1：不使用任何参数，直接删除文件\n" +
                "\n" +
                "  ```\n" +
                "  [root@192 etoak]# rm 1.txt \n" +
                "  rm: remove regular empty file ‘1.txt’? y\n" +
                "  [root@192 etoak]# ls -l\n" +
                "  total 4\n" +
                "  lrwxrwxrwx. 1 root root  16 Apr  7 16:33 dat -> /home/etoak/data\n" +
                "  drwxr-xr-x. 2 root root   6 Dec 28 16:14 data\n" +
                "  -rw-r--r--. 1 root root 109 Dec 28 16:14 Hello.java\n" +
                "  ```\n" +
                "\n" +
                "  \n" +
                "\n" +
                "- 示例2：递归强制删除目录\n" +
                "\n" +
                "  `[root@192 etoak]# rm -rf data2`\n" +
                "\n" +
                "  \n" +
                "\n" +
                "- 示例3：强制删除文件\n" +
                "\n" +
                "  `rm -f 文件名`\n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.6 mv\n" +
                "\n" +
                "1. **重命名**\n" +
                "\n" +
                "   把/home/et目录下的data2目录修改为data3\n" +
                "   \n" +
//                "   <img src=\"imgs/image-20211026160748918.png\" alt=\"image-20211026160748918\" style=\"zoom:50%;\" /> \n" +
                "   \n" +
                "   \n" +
                "   \n" +
                "   把/home/et目录下的1.txt文件修改为2.txt\n" +
                "   \n" +
//                "   <img src=\"imgs/image-20211026160837602.png\" alt=\"image-20211026160837602\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "2. **移动文件或目录**\n" +
                "\n" +
                "   `mv 文件/目录 已存在的目录`\n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.7 文件相关命令\n" +
                "\n" +
                "#### 7.7.1 touch：创建文件\n" +
                "\n" +
                "1. 创建单个文件\n" +
                "\n" +
                "   `[root@192 etoak]# touch a.html`\n" +
                "\n" +
                "   \n" +
                "\n" +
                "2. 创建多个文件\n" +
                "\n" +
                "   ```\n" +
                "   [root@192 etoak]# touch a.txt b.txt\n" +
                "   [root@192 etoak]# ls -l\n" +
                "   total 4\n" +
                "   -rw-r--r--. 1 root root   0 Apr  8 09:39 a.html\n" +
                "   -rw-r--r--. 1 root root   0 Apr  8 09:39 a.txt\n" +
                "   -rw-r--r--. 1 root root   0 Apr  8 09:39 b.txt\n" +
                "   ```\n" +
                "   \n" +
                "   \n" +
                "\n" +
                "#### 7.7.2 输出重定向\n" +
                "\n" +
                "1. **>**\n" +
                "\n" +
                "   这个符号会覆盖之前的内容\n" +
                "\n" +
                "   覆盖输出\n" +
                "\n" +
                "2. **>>**\n" +
                "\n" +
                "   这个符号会将新内容追加到原内容之后\n" +
                "\n" +
                "   追加输出\n" +
                "   \n" +
                "   \n" +
                "\n" +
                "#### 7.7.3 cat\n" +
                "\n" +
                "1. 查看文件内容\n" +
                "\n" +
                "   ```\n" +
                "   [root@192 etoak]# cat a.txt \n" +
                "   1.txt\n" +
                "   a\n" +
                "   a.html\n" +
                "   ```\n" +
                "\n" +
                "2. 合并文件内容\n" +
                "\n" +
                "   ```\n" +
                "   [root@192 etoak]# cat 1.txt a.txt > b.txt\n" +
                "   [root@192 etoak]# ls -l\n" +
                "   -rw-r--r--. 1 root root 498 Apr  8 09:44 1.txt\n" +
                "   -rw-r--r--. 1 root root  60 Apr  8 09:45 a.txt\n" +
                "   -rw-r--r--. 1 root root 558 Apr  8 09:46 b.txt\n" +
                "   ```\n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 7.7.4 head\n" +
                "\n" +
                "- 显示文件开头某个数量的文件内容，显示前多少行内容\n" +
                "\n" +
                "- 默认显示前10行\n" +
                "\n" +
                "- 显示b.txt的前3行内容\n" +
                "\n" +
                "   ```\n" +
                "  [root@192 etoak]# head -3 b.txt \n" +
                "  123123\n" +
                "  123123qwd\n" +
                "  1.txt\n" +
                "  ```\n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 7.7.5 tail\n" +
                "\n" +
                "1. 查看文件末尾某个数量的文件内容\n" +
                "\n" +
                "   - 直接使用`tail 文件名`，默认显示最后10行内容\n" +
                "\n" +
                "   - 指定显示最后多少行内容，使用`-n参数`\n" +
                "\n" +
                "     `tail -n 5 b.txt`\n" +
                "\n" +
                "     `tail -5 b.txt`\n" +
                "\n" +
                "   \n" +
                "\n" +
                "2. **动态查看(循环读取)文件内容，常用于查看线上日志**\n" +
                "\n" +
                "   使用`-f参数`，非常非常非常重要\n" +
                "\n" +
                "   **<font color=\"red\">循环显示，但是显示最后5行</font>**\n" +
                "\n" +
                "   `tail -f -n 5 b.txt `\n" +
                "\n" +
                "   `tail -5f b.txt`\n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 7.7.6 more\n" +
                "\n" +
                "- 查看文件内容\n" +
                "\n" +
                "  如果文件内容过大，会按百分比显示，与cat区分（cat会显示所有文件内容）\n" +
                "\n" +
                "  **按d：**向下查看文件内容\n" +
                "  \n" +
                "  **按b：**向上查看文件内容\n" +
                "  \n" +
                "  **按q**：退出\n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 7.7.7 <font color=\"red\">less</font>\n" +
                "\n" +
                "- 查看文件内容\n" +
                "\n" +
                "  less 文件名\n" +
                "  **按d：**向下查看文件内容\n" +
                "  **按b：**向上查看文件内容\n" +
                "  **按q：**退出less命令\n" +
                "  \n" +
                "  \n" +
                "\n" +
                "- 搜索文件内容\n" +
                "\n" +
                "  `/字符`内容：向下搜索\"字符内容\"\n" +
                "  `?字符`内容：向上搜索\"字符内容\"\n" +
                "  一般使用?的时候，都需要到达文件末尾，按G可以到达文件末尾\n" +
                "\n" +
                "  n：重复上一个搜锁操作，向前搜索\n" +
                "  N：重复上一个搜索操作，向后搜索\n" +
                "\n" +
                "\n" +
                "\n" +
                "#### 7.7.8 wc\n" +
                "\n" +
                "1. 统计行数：`wc -l b.txt`\n" +
                "\n" +
                "   ```bash\n" +
                "   [root@192 etoak]# wc -l b.txt \n" +
                "   34 b.txt\n" +
                "   ```\n" +
                "\n" +
                "2. 统计单词数 - 了解：`wc -w b.txt`\n" +
                "\n" +
                "   ```bash\n" +
                "   [root@192 etoak]# wc -w b.txt \n" +
                "   126 b.txt\n" +
                "   ```\n" +
                "   \n" +
                "3. 统计字节数 - 了解：`wc -c b.txt`\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.8 free\n" +
                "\n" +
                "- 查看内存使用情况\n" +
                "\n" +
                "- `free -m`\n" +
                "\n" +
                "- `free -h`\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026165818635.png\" alt=\"image-20211026165818635\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- `free -g`：以GB的方式显示\n" +
                "\n" +
                "- 以1000来计算内存，而不是以1024计算，增加参数：`--si`\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026165741391.png\" alt=\"image-20211026165741391\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.9 df\n" +
                "\n" +
                "- 显示文件系统磁盘的使用情况\n" +
                "\n" +
                "- `df -h`\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026170123418.png\" alt=\"image-20211026170123418\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.10 du\n" +
                "\n" +
                "- 查看指定目录或文件大小\n" +
                "- 参数-s：汇总大小\n" +
                "- 参数-h：以可读性较高的方式显示\n" +
                "\n" +
                "- `du -h`：分别显示所有的文件大小，最后显示汇总大小\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026170409033.png\" alt=\"image-20211026170409033\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- `du -sh`：以较高可读性方式，仅显示**汇总大小**\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026170323536.png\" alt=\"image-20211026170323536\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.11 top\n" +
                "\n" +
                "- 查看进程资源使用率，相当于windows中的任务管理器\n" +
                "\n" +
                "- 执行top\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026170633758.png\" alt=\"image-20211026170633758\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- 常用操作\n" +
                "\n" +
                "  1. 按内存使用率降序排序\n" +
                "\n" +
                "     输入top命令之后，按下M即可\n" +
                "\n" +
//                "     <img src=\"imgs/image-20211026170712812.png\" alt=\"image-20211026170712812\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "  2. 按CPU使用率降序排序\n" +
                "\n" +
                "     输入top命令之后，按下P即可\n" +
                "\n" +
//                "     <img src=\"imgs/image-20211026170749031.png\" alt=\"image-20211026170749031\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "  3. 查看每个CPU使用情况\n" +
                "\n" +
                "     输入top命令之后，按下1即可\n" +
                "\n" +
//                "     <img src=\"imgs/image-20211026170903284.png\" alt=\"image-20211026170903284\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.12 uptime\n" +
                "\n" +
                "- 打印系统总共运行了多长时间和系统的平均负载\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026171014825.png\" alt=\"image-20211026171014825\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "-  参数\n" +
                "\n" +
                "  -p：显示系统运行时长\n" +
                "\n" +
                "  -s：显示系统何时运行的\n" +
                "\n" +
                "   ```\n" +
                "  [root@192 home]# uptime -p\n" +
                "  up 1 hour, 50 minutes\n" +
                "  [root@192 home]# uptime --pretty\n" +
                "  up 1 hour, 51 minutes\n" +
                "   ```\n" +
                "\n" +
                "### 7.13 find\n" +
                "\n" +
                "- 用来在指定目录下查找文件、目录...\n" +
                "\n" +
                "- 语法：find   查找目录 (选项)    查找内容\n" +
                "\n" +
                "- 选项\n" +
                "\n" +
                "    -name：搜索文件名称\n" +
                "\n" +
                "    -type：搜索的文件类型\n" +
                "\n" +
                "    -type f: 搜索文件\n" +
                "\n" +
                "    -type d: 搜索文件夹\n" +
                "\n" +
                "    -type l: 搜索软连接\n" +
                "\n" +
                "- 示例1：从根目录查找Hello.java文件\n" +
                "\n" +
                "  `find / -name \"Hello.java\"`\n" +
                "\n" +
                "- 示例2：从当前目录下查找以`.java`结尾的文件\n" +
                "\n" +
                "  `find ./ -name \"*.java\"`\n" +
                "\n" +
                "- 示例3：从/home目录下查找所有的带java的文件\n" +
                "\n" +
                "  `[root@192 ~]# find /home -type f -name \"*java\"`\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026171515508.png\" alt=\"image-20211026171515508\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- 示例4：从/home目录下下查找所有带java的目录\n" +
                "\n" +
                "  `[root@192 et1910]# find /home -type d -name \"*java\"`\n" +
                "\n" +
                "### 7.14 netstat\n" +
                "\n" +
                "- 打印Linux中网络系统的状态信息\n" +
                "\n" +
                "- 参数\n" +
                "\n" +
                "  ```\n" +
                "  -t:显示tcp连接\n" +
                "  -l:显示处理监听状态的连接\n" +
                "  -p:显示进程号和程序名称；\n" +
                "  -n:直接\n" +
                "  \n" +
                "  -u:显示udp连接\n" +
                "  ```\n" +
                "\n" +
                "  \n" +
                "\n" +
                "### 7.15 ps\n" +
                "\n" +
                "- 查看进程状态\n" +
                "\n" +
                "- 参数\n" +
                "\n" +
                "  ```\n" +
                "  -e: 显示所有进程\n" +
                "  -f: 显示UID, PPIP, C与STIME栏位\n" +
                "  ```\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026172147136.png\" alt=\"image-20211026172147136\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "  ```\n" +
                "  a: 显示现行终端机下的所有程序，包括其他用户的程序。\n" +
                "  u: 以用户为主的格式来显示程序状况\n" +
                "  x: 显示所有程序，不以终端机来区分\n" +
                "  ```\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026172106905.png\" alt=\"image-20211026172106905\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "### 7.16 kill\n" +
                "\n" +
                "- 终止进程\n" +
                "- `kill 进程号`\n" +
                "- `kill -9 进程号`：强制终止进程\n" +
                "\n" +
                "\n" +
                "\n" +
                "### 7.17 cp：文件拷贝\n" +
                "\n" +
                "- cp 【参数】 源文件  目标文件（目录）\n" +
                "\n" +
                "- 拷贝文件\n" +
                "\n" +
                "- 拷贝目录\n" +
                "\n" +
                "  `-R\\-r`：拷贝目录必须要使用的参数，同时可以递归拷贝\n" +
                "\n" +
                "  \n" +
                "\n" +
                "### 7.18 scp：远程拷贝（走ssh协议）\n" +
                "\n" +
                "1. **拷贝文件到远程机器**\n" +
                "\n" +
//                "   <img src=\"imgs/image-20211026172817724.png\" alt=\"image-20211026172817724\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "2. **递归拷贝目录远程机器**\n" +
                "\n" +
                "   `-r:以递归的方式拷贝`\n" +
                "\n" +
//                "   <img src=\"imgs/image-20211026173048767.png\" alt=\"image-20211026173048767\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "3. **拷贝远程文件到本地**\n" +
                "\n" +
                "   `scp root@192.168.85.133:/home/et/hello.py ./` \n" +
                "\n" +
                "4. **拷贝远程目录到本地**\n" +
                "\n" +
//                "   <img src=\"imgs/image-20211026173231416.png\" alt=\"image-20211026173231416\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "### 7.19 tar\n" +
                "\n" +
                "- 压缩和解压tar.gz文件\n" +
                "\n" +
                "- 解压\n" +
                "\n" +
                "  ```\n" +
                "  -z:通过gzip指令处理备份文件\n" +
                "  -x:提取文件\n" +
                "  -v:显示解压过程\n" +
                "  -f:指定解压文件\n" +
                "  -C:指定解压目录\n" +
                "  ```\n" +
                "\n" +
                "  `tar -zxvf 文件名 [-C /opt]`\n" +
                "\n" +
                "### 7.20 ln\n" +
                "\n" +
                "- 创建软连接（相当于windows的快捷方式）\n" +
                "\n" +
                "- -s：对源文件建立软连接\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026173650114.png\" alt=\"image-20211026173650114\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "### 7.21 date\n" +
                "\n" +
                "- 显示时间\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026173927857.png\" alt=\"image-20211026173927857\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- 以\"年-月-日 时:分:秒\"显示时间\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026173909017.png\" alt=\"image-20211026173909017\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- 使用`date \"+%F\"`显示年月日\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026173958070.png\" alt=\"image-20211026173958070\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "- 显示昨天的日期\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026174131370.png\" alt=\"image-20211026174131370\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "- 显示上一个月的今天今天\n" +
                "\n" +
//                "  <img src=\"imgs/image-20211026174224641.png\" alt=\"image-20211026174224641\" style=\"zoom:50%;\" /> \n" +
                "\n" +
                "\n" +
                "### 7.22 clear\n" +
                "\n" +
                "- 清除屏幕内容：相当于windows中cls命令\n" +
                "\n" +
                "  \n" +
                "\n" +
                "### 7.23  shutdown\n" +
                "\n" +
                "- 系统关机命令\n" +
                "\n" +
                "- shutdown -h now：立即关机\n" +
                "\n" +
                "- shutdown -h 17:00 \"系统将在17:00关机\"\n" +
                "\n" +
                "  ```\n" +
                "  [root@192 ~]# shutdown -h 17:00 \"系统将要关机\"\n" +
                "  Shutdown scheduled for Wed 2020-04-08 17:00:00 CST, use 'shutdown -c' to cancel.\n" +
                "  \n" +
                "  [root@192 ~]# shutdown -c\n" +
                "  Broadcast message from root@192.168.189.130 (Wed 2020-04-08 16:32:36 CST):\n" +
                "  The system shutdown has been cancelled at Wed 2020-04-08 16:33:36 CST!\n" +
                "  ```\n" +
                "\n" +
                "\n" +
                "\n" +
                "### 5.24 reboot\n" +
                "\n" +
                "- 系统重启命令\n" +
                "\n" +
                "## 8. 安装vim编辑器\n" +
                "\n" +
                "`yum install -y vim`\n" +
                "\n" +
                "## 9. 安装JDK\n" +
                "\n" +
                "1. 解压jdk\n" +
                "\n" +
                "   `tar -zxvf jdk-8u201-linux-x64.tar.gz` \n" +
                "\n" +
                "2. 创建了一个软连接\n" +
                "\n" +
                "   `ln -s jdk1.8.0_102 java`\n" +
                "\n" +
                "3. 修改`/etc/profile`文件\n" +
                "\n" +
                "   ```bash\n" +
                "   export JAVA_HOME=/usr/local/java\n" +
                "   export PATH=$PATH:$JAVA_HOME/bin\n" +
                "   ```\n" +
                "\n" +
                "4. source /etc/profile\n" +
                "\n" +
                "5. java -version\n" +
                "\n" +
                "## 10. 安装lrzsz\n" +
                "\n" +
                "`yum install -y lrzsz`\n" +
                "\n" +
                "`rz命令`：选择本地文件上传到服务器\n" +
                "\n" +
                "`sz命令`：将服务器文件下载本地";
        toDoc(str1, "test23");


    }
}