package com.intelligent.controller;

import com.intelligent.service.ApiManageService;
import com.intelligent.vo.ApiAddVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Amie
 * @description
 * @time 2025/4/9 15:05
 */
@RestController
@RequestMapping("/api")
public class ApiManageController {
    @Autowired
    ApiManageService service;
    @PostMapping("/insertApi")
    public String addApi() {
        service.addApi(new ApiAddVO());
        return "success";
    }
}
