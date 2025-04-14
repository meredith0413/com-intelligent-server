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
 * @description ��ǩ����
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
     * �鿴��ǩ
     *
     * @param id  ��ǩID
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
     * ������ǩ
     *
     * @param vo ��ǩ��Ϣ
     * @return
     */
    @PostMapping("/saveLabel")
    public RestResponse saveLabel(@RequestBody LabelVo vo) {
        if (vo == null) {
            return RestResponse.failureError();
        }
        List<AgentUrlParameter> responseList = vo.getResponseList();
        if (responseList == null || responseList.size() == 0) {
            return RestResponse.failureError("���β���Ϊ��");
        }
        LabelVo labelVo = labelService.saveLabel(vo);
        return RestResponse.successOk(labelVo);
    }

    /**
     * �޸ı�ǩ
     *
     * @param vo ��ǩ��Ϣ
     * @return
     */
    @PostMapping("/updateLabel")
    public RestResponse updateLabel(@RequestBody LabelVo vo) {
        // todo ��Ҫ����֤ ����ǩ��û�б�ʹ��
        if (vo == null) {
            return RestResponse.failureError();
        }
        List<AgentUrlParameter> responseList = vo.getResponseList();
        if (responseList == null || responseList.size() == 0) {
            return RestResponse.failureError("���β���Ϊ��");
        }
        LabelVo labelVo = labelService.updateLabel(vo);
        return RestResponse.successOk(labelVo);
    }

    /**
     * ɾ����ǩ
     *
     * @param id ��ǩId
     * @return
     */
    @PostMapping("/deleteLabelById")
    public RestResponse<String> deleteLabelById(@RequestParam("id") Long id) {
        // todo ��Ҫ����֤ ����ǩ��û�б�ʹ��
        Boolean result = labelService.deleteLabelById(id);
        if (result) {
            return RestResponse.successOk("ɾ���ɹ�");
        }
        return RestResponse.failureError("ɾ��ʧ��");
    }


    /**
     * ��Ŀ�����ģ�� �����ǩ
     *
     * @param vo
     * @return
     */
    @PostMapping("/updateTemplate")
    public RestResponse getTestWord(@RequestBody TemplateChangeVo vo) {
        //todo 1 ��ȡԭʼģ��
        byte[] bytes = ossFileService.downloadFile(vo.getPath());

        //todo 2 ����ģ��
//        byte[] bytes1 = WordUtil.wordTextSubstitutionByByte(bytes,"docx",list);
//        MultipartFile file = new MockMultipartFile("file", "aaaaaa" + ".docx", ContentType.MULTIPART_FORM_DATA.toString(), bytes1);

        //todo 3 �ϴ��µ��ļ�
//        OssFile ossFile = ossFileService.updateFile(file);

        // todo 4 ����ҵ������
        return RestResponse.builder().build();
    }

    @GetMapping("/getZWF")
    public void getZWF(@RequestParam("path") String path) throws IOException {
        byte[] bytes = ossFileService.downloadFile(path);
        InputStream fis = new ByteArrayInputStream(bytes);
        XWPFDocument document = new XWPFDocument(fis);

    }

    /**
     * ������ǩ ��ǩ�������ظ�
     */

    /**
     * �޸ı�ǩ
     */

    /**
     * ɾ����ǩ
     */

    /**
     * ��ѯ��ǩ
     */
    @PostMapping("/getGenerateFile")
    public void getGenerateFile(@RequestBody RequestVo vo) {
        // todo ��ȡģ��ID

        // todo ��ѯ��Ҫ���õ�api_url

        // todo �����ݲ��뵽ģ��

        // todo ��������

    }


}
