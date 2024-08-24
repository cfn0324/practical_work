package com.ctgu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.entity.Rloe;
import com.ctgu.service.IRloeService;
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
@RequestMapping("/rloe")
public class RloeController {
    @Autowired
    private IRloeService rloeService;
    // 查询:
    @RequestMapping("/getall")
    public List<Rloe> getall(){
        List<Rloe> list =  rloeService.list();
        return list;
    }
    @RequestMapping("/getByPage")
    public IPage<Rloe> getByPage(Integer pageNum, Integer pageSize){
        IPage<Rloe> page = rloeService.page(new Page<>(pageNum, pageSize));

        System.out.println("结果集"+page.getRecords());
        System.out.println("总条数"+page.getTotal());
        System.out.println("总页数"+page.getPages());
        System.out.println("每页多少条"+page.getSize());
        System.out.println("当前页"+page.getCurrent());

//    List<Student> list = studentService.list(page);
//    System.out.println(list);
        return page;
    }
    @RequestMapping("/update/{rid}")
    public int update(@PathVariable Integer rid, @RequestBody Rloe project) {
        project.setRid(rid);
        rloeService.updateById(project);
        return 1;
    }
    @RequestMapping("/add")
    public boolean add(@RequestBody Rloe project) {

        boolean b= rloeService.save(project);
        return b;
    }

    @RequestMapping("/delete/{rid}")
    public int delete(@PathVariable  Integer rid) {
        rloeService.removeById(rid);
        return 0;
    }
    @RequestMapping("/getByName")
    public List<Rloe> getUserByName(@RequestParam(required = false, defaultValue = "") String rolename) {
        QueryWrapper<Rloe> wrapper = new QueryWrapper<>();
        if (!rolename.isEmpty()) {
            wrapper.like("rolename", rolename);
        }
        return rloeService.list(wrapper);
    }
}
