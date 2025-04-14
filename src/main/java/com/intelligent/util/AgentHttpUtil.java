package com.intelligent.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.Map;

/**
 * @Date 2025/4/13
 * @Version 1.0
 */
public class AgentHttpUtil {

    /**
     * GET请求(无参数)
     * @param url 请求地址
     * @return 响应字符串
     */
    public static String get(String url){
        return HttpUtil.get(url);
    }

    /**
     * GET请求
     * @param url 请求地址
     * @param map 请求参数
     * @return 响应字符串
     */
    public static String get(String url, Map<String,Object>map){
        return HttpUtil.get(url,map);
    }

    /**
     * POST请求(参数表单)
     * @param url 请求地址
     * @param map 请求参数
     * @return 响应字符串
     */
    public static String postFrom(String url, Map<String,Object>map){
        return HttpUtil.post(url,map);
    }

    /**
     * POST请求(JSON)
     * @param url 请求地址
     * @param map 请求参数
     * @return 响应字符串
     */
    public static String postJson(String url, Map<String,Object>map){
        String str = JSONUtil.toJsonStr(map);
        return HttpUtil.post(url,str);
    }

}
