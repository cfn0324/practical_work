package com.ctgu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.entity.Tq;
import com.ctgu.entity.Users;
import com.ctgu.service.ITqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cfn
 * @since 2024-08-23
 */
@RestController
@RequestMapping("/tq")
public class TqController {


    @Autowired
    private ITqService tqService;

    // 查询:
    @RequestMapping("/getall")
    public List<Tq> getall() {
        return tqService.list();
    }
    @RequestMapping("/getByPage")
    public IPage<Tq> getByPage(Integer pageNum, Integer pageSize){
        IPage<Tq> page = tqService.page(new Page<>(pageNum, pageSize));

        System.out.println("结果集"+page.getRecords());
        System.out.println("总条数"+page.getTotal());
        System.out.println("总页数"+page.getPages());
        System.out.println("每页多少条"+page.getSize());
        System.out.println("当前页"+page.getCurrent());

        return page;
    }
    @RequestMapping("/getByName")
    public List<Tq> getStudentByName( String name) {
        QueryWrapper<Tq> wrapper = new QueryWrapper<>();
        wrapper.like("uname", name);
        List<Tq> list = tqService.list(wrapper);
        return list;
    }
}