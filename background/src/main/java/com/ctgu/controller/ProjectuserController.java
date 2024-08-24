package com.ctgu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.entity.Projectuser;
import com.ctgu.service.IProjectuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/projectuser")
public class ProjectuserController {
    @Autowired
    private IProjectuserService projectuserService;
    // 查询:
    @RequestMapping("/getall")
    public List<Projectuser> getall(){
        List<Projectuser> list =  projectuserService.list();
        return list;
    }
    @RequestMapping("/getByPage")
    public IPage<Projectuser> getByPage(Integer pageNum, Integer pageSize){
        IPage<Projectuser> page = projectuserService.page(new Page<>(pageNum, pageSize));

        System.out.println("结果集"+page.getRecords());
        System.out.println("总条数"+page.getTotal());
        System.out.println("总页数"+page.getPages());
        System.out.println("每页多少条"+page.getSize());
        System.out.println("当前页"+page.getCurrent());
        return page;
    }
    @RequestMapping("/update/{pid}")
    public int update(@PathVariable Integer pid, @RequestBody Projectuser projectuser) {
        projectuser.setPid(pid);
        projectuserService.updateById(projectuser);
        return 1;
    }
    @RequestMapping("/add")
    public boolean add(@RequestBody Projectuser projectuser) {

        boolean b= projectuserService.save(projectuser);
        return b;
    }

    @RequestMapping("/delete/{pid}")
    public int delete(@PathVariable  Integer pid) {
        projectuserService.removeById(pid);
        return 0;
    }
    @RequestMapping("/getByName")
    public List<Projectuser> getUserByName(@RequestParam(required = false, defaultValue = "") String pusername) {
        QueryWrapper<Projectuser> wrapper = new QueryWrapper<>();
        if (!pusername.isEmpty()) {
            wrapper.like("pusername", pusername);
        }
        return projectuserService.list(wrapper);
    }
    @RequestMapping("/getBypName")
    public List<Projectuser> getUserBypName(@RequestParam(required = false, defaultValue = "") String projectname) {
        QueryWrapper<Projectuser> wrapper = new QueryWrapper<>();
        if (!projectname.isEmpty()) {
            wrapper.like("projectname", projectname);
        }
        return projectuserService.list(wrapper);


    }

}
