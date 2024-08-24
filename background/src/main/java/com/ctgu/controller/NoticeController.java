package com.ctgu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.entity.Notice;
import com.ctgu.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cfn
 * @since 2024-08-24
 */

import java.util.List;

@RestController
@RequestMapping("/notice")
    public class NoticeController {
        //使用mybatisplus 增删改
        @Autowired
        public INoticeService noticeService;
        //查询所有通知信息
        @RequestMapping("/getAll")
        public List<Notice> getAll()
        {
            List<Notice> list = noticeService.list();
            for (Notice notice : list) {
                System.out.println(notice);
            }
            return list;
        }
        //删除通知信息
        @RequestMapping("/delNoId{id}")
        public boolean delNoId(@PathVariable Integer id)
        {
            boolean b = noticeService.removeById(id);
            return b;
        }
        //添加通知信息
        @RequestMapping("/add")
        public boolean add(@RequestBody Notice notice)
        {
            System.out.println(notice);
            boolean b = noticeService.save(notice);
            return b;
        }
        //消息通知模糊查询
        @RequestMapping("/getNotice")
        public List<Notice> getNoticeName(String name)
        {

            QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name",name);
            List<Notice> list = noticeService.list(queryWrapper);
            return list;
        }

        //修改通知信息
        @RequestMapping("/update")
        public boolean update(@RequestBody Notice notice)
        {
            boolean b = noticeService.updateById(notice);
            return b;
        }
        //分页查询
        @RequestMapping("/getByPage")
        public IPage<Notice> getByPage(Integer pageNum, Integer pageSize)
        {
            IPage<Notice> page = noticeService.page(new Page<>(pageNum, pageSize));

            return page;
        }



    }

