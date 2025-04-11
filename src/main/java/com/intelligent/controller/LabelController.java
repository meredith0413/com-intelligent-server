package com.intelligent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.intelligent.common.RestResponse;
import com.intelligent.entity.AgentLabel;
import com.intelligent.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/label")
public class LabelController {


    @Autowired
    private LabelService labelService;

    @PostMapping("/getTest")
    public RestResponse getTest(){
//        List<AgentLabel> list = labelService.list();

        Page<AgentLabel> userPage = new Page<>(1, 1);
        Page<AgentLabel> page = labelService.page(userPage);
        return RestResponse.builder().build().setResultBody(page);

    }
}
