package com.intelligent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.intelligent.entity.AgentLabel;
import com.intelligent.vo.LabelVo;

/**
 * @author Mysql
 * @description
 * @time 2025/4/9 15:06
 */
public interface LabelService extends IService<AgentLabel> {

    /**
     * 查看标签
     *
     * @param id  标签ID
     * @return
     */
    LabelVo getLabel(Long id);

    /**
     * 新增标签
     *
     * @param vo 标签信息
     * @return
     */
    LabelVo saveLabel(LabelVo vo);

    /**
     * 新增标签
     *
     * @param vo 标签信息
     * @return
     */
    LabelVo updateLabel(LabelVo vo);

    /**
     * 删除标签
     *
     * @param id 标签ID
     * @return
     */
    Boolean deleteLabelById(Long id);
}
