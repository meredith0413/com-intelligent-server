package com.intelligent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.intelligent.entity.AgentLabel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LabelMapper extends BaseMapper<AgentLabel> {
}
