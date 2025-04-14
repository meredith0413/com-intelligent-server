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
     * ����URL/API�Ľӿ� ��γ�����Ϣ
     *
     * @param list
     * @return
     */
    Integer saveLabelParameter(@Param("list") List<AgentUrlParameter> list);

    /**
     * ��ȡ URL/API�Ľӿ� ��γ�����Ϣ
     *
     * @param id URL/API�Ľӿ�ID
     * @return
     */
    List<AgentUrlParameter> listLabelParameter(@Param("id") Long id);

    /**
     * ɾ�� URL/API�Ľӿ� ��γ�����Ϣ
     *
     * @param id URL/API�Ľӿ�ID
     * @return
     */
    Integer removeLabelParameterById(@Param("id") Long id);
}
