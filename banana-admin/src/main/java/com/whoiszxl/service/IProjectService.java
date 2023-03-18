package com.whoiszxl.service;

import com.whoiszxl.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2023-03-11
 */
public interface IProjectService extends IService<Project> {

    /**
     * 生成对应项目下需要的服务器
     * @param projectId 项目ID
     */
    void generate(Integer projectId);
}
