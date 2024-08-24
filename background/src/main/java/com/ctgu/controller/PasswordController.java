package com.ctgu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.entity.Password;
import com.ctgu.entity.Users;
import com.ctgu.service.IPasswordService;
import com.ctgu.service.IUsersService;
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
 * @since 2024-08-24
 */
@RestController
@RequestMapping("/password")
public class PasswordController {
    @Autowired
    private IPasswordService passwordService;
    // 查询:
    @RequestMapping("/getall")
    public List<Password> getall(){
        List<Password> list =  passwordService.list();
        return list;
    }
    @RequestMapping("/getByPage")
    public IPage<Password> getByPage(Integer pageNum, Integer pageSize){
        IPage<Password> page = passwordService.page(new Page<>(pageNum, pageSize));

        System.out.println("结果集"+page.getRecords());
        System.out.println("总条数"+page.getTotal());
        System.out.println("总页数"+page.getPages());
        System.out.println("每页多少条"+page.getSize());
        System.out.println("当前页"+page.getCurrent());
        return page;
    }
    @RequestMapping("/update/{id}")
    public int update(@PathVariable Integer id, @RequestBody Password password) {
        password.setId(id);
//        LocalDateTime now=LocalDateTime.now();
//        password.setUupdated(now);
        passwordService.updateById(password);
        return 1;
    }
    @RequestMapping("/add")
    public boolean add(@RequestBody Password password) {

        boolean b= passwordService.save(password);
        return b;
    }

    @RequestMapping("/delete/{id}")
    public int delete(@PathVariable  Integer id) {
        passwordService.removeById(id);
        return 0;
    }
    @RequestMapping("/getByName")
    public List<Password> getUserByName(@RequestParam(required = false, defaultValue = "") String name) {
        QueryWrapper<Password> wrapper = new QueryWrapper<>();
        if (!name.isEmpty()) {
            wrapper.like("name", name);
        }
        return passwordService.list(wrapper);
    }
}