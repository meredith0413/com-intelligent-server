package com.intelligent.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelligent.common.RestResponse;
import com.intelligent.entity.AgentLabel;
import com.intelligent.entity.AgentUrlParameter;
import com.intelligent.entity.OssFile;
import com.intelligent.service.LabelService;
import com.intelligent.service.OssFileService;
import com.intelligent.util.WordUtil;
import com.intelligent.vo.LabelVo;
import com.intelligent.vo.ReplaceTemplateVo;
import com.intelligent.vo.RequestVo;
import com.intelligent.vo.TemplateChangeVo;
import org.apache.http.entity.ContentType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mysql
 * @description 标签管理
 * @time 2025/4/14
 */
@RestController
@RequestMapping("/label")
public class LabelController {


    @Autowired
    private LabelService labelService;

    @Autowired
    private OssFileService ossFileService;

    /**
     * 查看标签
     *
     * @param id  标签ID
     * @return
     */
    @GetMapping("/getLabel")
    public RestResponse getLabel(@RequestParam("id")Long id){
        LabelVo label = labelService.getLabel(id);
        return RestResponse.successOk(label);
    }

    @PostMapping("/getTest")
    public RestResponse getTest() {
        Page<AgentLabel> userPage = new Page<>(1, 1);
        Page<AgentLabel> page = labelService.page(userPage);
        return RestResponse.builder().build().setResultBody(page);
    }

    /**
     * 新增标签
     *
     * @param vo 标签信息
     * @return
     */
    @PostMapping("/saveLabel")
    public RestResponse saveLabel(@RequestBody LabelVo vo) {
        if (vo == null) {
            return RestResponse.failureError();
        }
        List<AgentUrlParameter> responseList = vo.getResponseList();
        if (responseList == null || responseList.size() == 0) {
            return RestResponse.failureError("出参不可为空");
        }
        LabelVo labelVo = labelService.saveLabel(vo);
        return RestResponse.successOk(labelVo);
    }

    /**
     * 修改标签
     *
     * @param vo 标签信息
     * @return
     */
    @PostMapping("/updateLabel")
    public RestResponse updateLabel(@RequestBody LabelVo vo) {
        // todo 需要做验证 看标签有没有被使用
        if (vo == null) {
            return RestResponse.failureError();
        }
        List<AgentUrlParameter> responseList = vo.getResponseList();
        if (responseList == null || responseList.size() == 0) {
            return RestResponse.failureError("出参不可为空");
        }
        LabelVo labelVo = labelService.updateLabel(vo);
        return RestResponse.successOk(labelVo);
    }

    /**
     * 删除标签
     *
     * @param id 标签Id
     * @return
     */
    @PostMapping("/deleteLabelById")
    public RestResponse<String> deleteLabelById(@RequestParam("id") Long id) {
        // todo 需要做验证 看标签有没有被使用
        Boolean result = labelService.deleteLabelById(id);
        if (result) {
            return RestResponse.successOk("删除成功");
        }
        return RestResponse.failureError("删除失败");
    }


    /**
     * 项目组操作模板 插入标签
     *
     * @param vo
     * @return
     */
    @PostMapping("/updateTemplate")
    public RestResponse getTestWord(@RequestBody TemplateChangeVo vo) {
        //todo 1 获取原始模板
        byte[] bytes = ossFileService.downloadFile(vo.getPath());

        //todo 2 更改模板
//        byte[] bytes1 = WordUtil.wordTextSubstitutionByByte(bytes,"docx",list);
//        MultipartFile file = new MockMultipartFile("file", "aaaaaa" + ".docx", ContentType.MULTIPART_FORM_DATA.toString(), bytes1);

        //todo 3 上传新的文件
//        OssFile ossFile = ossFileService.updateFile(file);

        // todo 4 更改业务数据
        return RestResponse.builder().build();
    }

    @GetMapping("/getZWF")
    public void getZWF(@RequestParam("path") String path) throws IOException {
        byte[] bytes = ossFileService.downloadFile(path);
        InputStream fis = new ByteArrayInputStream(bytes);
        XWPFDocument document = new XWPFDocument(fis);

    }

    /**
     * 新增标签 标签名不能重复
     */

    /**
     * 修改标签
     */

    /**
     * 删除标签
     */

    /**
     * 查询标签
     */
    @PostMapping("/getGenerateFile")
    public void getGenerateFile(@RequestBody RequestVo vo) {
        // todo 获取模板ID

        // todo 查询需要调用的api_url

        // todo 把数据插入到模板

        // todo 生成数据

    }


}
