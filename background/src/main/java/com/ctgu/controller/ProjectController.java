package com.ctgu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.entity.Project;
import com.ctgu.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-08-23
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private IProjectService projectService;
    // 查询:
    @RequestMapping("/getall")
    public List<Project> getall(){
        List<Project> list =  projectService.list();
        return list;
    }
    @RequestMapping("/getByPage")
    public IPage<Project> getByPage(Integer pageNum, Integer pageSize){
        IPage<Project> page = projectService.page(new Page<>(pageNum, pageSize));

        System.out.println("结果集"+page.getRecords());
        System.out.println("总条数"+page.getTotal());
        System.out.println("总页数"+page.getPages());
        System.out.println("每页多少条"+page.getSize());
        System.out.println("当前页"+page.getCurrent());

//    List<Student> list = studentService.list(page);
//    System.out.println(list);
        return page;
    }
    @RequestMapping("/update/{pid}")
    public int update(@PathVariable Integer pid, @RequestBody Project project) {
        project.setPid(pid);
        projectService.updateById(project);
        return 1;
    }
    @RequestMapping("/add")
    public boolean add(@RequestBody Project project) {

        boolean b= projectService.save(project);
        return b;
    }

    @RequestMapping("/delete/{pid}")
    public int delete(@PathVariable  Integer pid) {
        projectService.removeById(pid);
        return 0;
    }
    @RequestMapping("/getByName")
    public List<Project> getUserByName(@RequestParam(required = false, defaultValue = "") String projectname) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        if (!projectname.isEmpty()) {
            wrapper.like("projectname", projectname);
        }
        return projectService.list(wrapper);
    }
}
