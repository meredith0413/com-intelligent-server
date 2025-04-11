package com.intelligent.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intelligent.entity.AgentLabel;
import com.intelligent.mapper.LabelMapper;
import com.intelligent.service.LabelService;
import org.springframework.stereotype.Service;

@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, AgentLabel> implements LabelService {
}
