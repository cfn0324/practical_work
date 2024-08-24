package com.ctgu.service.impl;

import com.ctgu.entity.Project;
import com.ctgu.mapper.ProjectMapper;
import com.ctgu.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cfn
 * @since 2024-08-24
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

}
