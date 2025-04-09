package com.intelligent.service;

import com.intelligent.common.RestResponse;
import com.intelligent.vo.ApiAddVO;
import org.springframework.stereotype.Service;

/**
 * @author Amie
 * @description
 * @time 2025/4/9 15:06
 */
@Service
public class ApiManageService {
    public RestResponse addApi(ApiAddVO vo){
        System.out.println("just for test");
        return new RestResponse<>();
    }
}
