package com.intelligent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.intelligent.entity.AgentLabel;
import com.intelligent.entity.AgentUrlParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Mysql
 * @description
 * @time 2025/4/9 15:06
 */
@Mapper
public interface LabelMapper extends BaseMapper<AgentLabel> {

    /**
     * 新增URL/API的接口 入参出参信息
     *
     * @param list
     * @return
     */
    Integer saveLabelParameter(@Param("list") List<AgentUrlParameter> list);

    /**
     * 获取 URL/API的接口 入参出参信息
     *
     * @param id URL/API的接口ID
     * @return
     */
    List<AgentUrlParameter> listLabelParameter(@Param("id") Long id);

    /**
     * 删除 URL/API的接口 入参出参信息
     *
     * @param id URL/API的接口ID
     * @return
     */
    Integer removeLabelParameterById(@Param("id") Long id);
}
