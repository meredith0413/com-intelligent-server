package com.intelligent.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelligent.common.Constants;
import com.intelligent.entity.AgentLabel;
import com.intelligent.entity.AgentUrlParameter;
import com.intelligent.mapper.LabelMapper;
import com.intelligent.service.LabelService;
import com.intelligent.vo.LabelVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mysql
 * @description
 * @time 2025/4/14 15:06
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, AgentLabel> implements LabelService {


    @Resource
    private LabelMapper labelMapper;

    /**
     * 查看标签
     *
     * @param id  标签ID
     * @return
     */
    @Override
    public LabelVo getLabel(Long id) {
        AgentLabel label = this.getById(id);
        LabelVo labelVo = BeanUtil.copyProperties(label, LabelVo.class);
        List<AgentUrlParameter> list = labelMapper.listLabelParameter(id);
        if (list != null && list.size() > 0) {
            List<AgentUrlParameter> requestList = list.stream().filter(item -> StringUtils.equals(Constants.ParameterRequest, item.getParameterType())).collect(Collectors.toList());
            labelVo.setRequestList(requestList);
            List<AgentUrlParameter> responseList = list.stream().filter(item -> StringUtils.equals(Constants.ParameterResponse, item.getParameterType())).collect(Collectors.toList());
            labelVo.setResponseList(responseList);
        }
        return labelVo;
    }

    /**
     * 新增标签
     *
     * @param vo 标签信息
     * @return
     */
    @Override
    public LabelVo saveLabel(LabelVo vo) {
        AgentLabel agentLabel = BeanUtil.copyProperties(vo, AgentLabel.class);
        vo.setId(agentLabel.getId());
        List<AgentUrlParameter> requestList = vo.getRequestList();
        List<AgentUrlParameter> responseList = vo.getResponseList();
        List<AgentUrlParameter> list = new ArrayList<>();
        if (requestList != null && requestList.size() > 0) {
            requestList.forEach(item->{
                item.setLabelId(agentLabel.getId());
            });
            list.addAll(requestList);
        }
        if (responseList != null && responseList.size() > 0) {
            responseList.forEach(item->{
                item.setLabelId(agentLabel.getId());
            });
            list.addAll(responseList);
        }

        labelMapper.saveLabelParameter(list);
        return vo;
    }

    /**
     * 新增标签
     *
     * @param vo 标签信息
     * @return
     */
    @Override
    public LabelVo updateLabel(LabelVo vo) {
        //删除旧参数
        Long id = vo.getId();
        labelMapper.removeLabelParameterById(id);
        List<AgentUrlParameter> requestList = vo.getRequestList();
        List<AgentUrlParameter> responseList = vo.getResponseList();
        List<AgentUrlParameter> list = new ArrayList<>();
        if (requestList != null && requestList.size() > 0) {
            requestList.forEach(item->{
                item.setLabelId(vo.getId());
            });
            list.addAll(requestList);
        }
        if (responseList != null && responseList.size() > 0) {
            responseList.forEach(item->{
                item.setLabelId(vo.getId());
            });
            list.addAll(responseList);
        }
        labelMapper.saveLabelParameter(list);
        return vo;
    }

    /**
     * 删除标签
     *
     * @param id 标签ID
     * @return
     */
    @Override
    public Boolean deleteLabelById(Long id) {
        labelMapper.removeLabelParameterById(id);
        return this.removeById(id);
    }
}
