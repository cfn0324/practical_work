package com.ctgu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.entity.Users;
import com.ctgu.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-08-18
 */
@RestController
@RequestMapping("/users")
public class UsersController {


    @Autowired
    private IUsersService usersService;
    // 查询:
    @RequestMapping("/getall")
    public List<Users> getall(){
        List<Users> list =  usersService.list();
        return list;
    }
    @RequestMapping("/getByPage")
    public IPage<Users> getByPage(Integer pageNum, Integer pageSize){
        IPage<Users> page = usersService.page(new Page<>(pageNum, pageSize));

        System.out.println("结果集"+page.getRecords());
        System.out.println("总条数"+page.getTotal());
        System.out.println("总页数"+page.getPages());
        System.out.println("每页多少条"+page.getSize());
        System.out.println("当前页"+page.getCurrent());

//    List<Student> list = studentService.list(page);
//    System.out.println(list);
        return page;
    }
    @RequestMapping("/update/{userid}")
    public int update(@PathVariable Integer userid, @RequestBody Users users) {
        users.setUid(userid);
        LocalDateTime now = LocalDateTime.now();
        users.setUupdated(now);
        usersService.updateById(users);
        return 1;
    }
    @RequestMapping("/add")
    public boolean add(@RequestBody Users users) {

        boolean b= usersService.save(users);
        return b;
    }

    @RequestMapping("/delete/{userid}")
    public int delete(@PathVariable  Integer userid) {
        usersService.removeById(userid);
        return 0;
    }
    //    @RequestMapping("/getByName/{uname}")
//    public List<Users> getStudentByName(@RequestBody String uname) {
//        QueryWrapper<Users> wrapper = new QueryWrapper<>();
//        wrapper.like("name", uname);
//        List<Users> list = usersService.list(wrapper);
//        return list;
//    }
    @RequestMapping("/getByName")
    public List<Users> getStudentByName( String name) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.like("uname", name);
        List<Users> list = usersService.list(wrapper);
        return list;
    }

    @RequestMapping("/getpy")
    public List<String> getpy() {
        List<String> outputList = new ArrayList<>();
        try {
            // 调用Python脚本
            ProcessBuilder pb = new ProcessBuilder("python", "D:\\实训\\jqxx.py");
            Process p = pb.start();

            // 等待Python脚本执行完毕
            p.waitFor();

            // 检查Python脚本的退出值
            int exitValue = p.exitValue();
            if (exitValue == 0) {
                // 读取Python脚本的输出
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    // 去掉方括号
                    line = line.replace("[", "").replace("]", "").trim();
                    // 分割字符串并添加到列表
                    String[] values = line.split(", ");
                    for (String value : values) {
                        outputList.add(value);
                    }
                }
                reader.close();
                System.out.println("Python脚本执行成功，输出：" + outputList);
            } else {
                System.out.println("Python脚本执行失败，退出值：" + exitValue);
                outputList.add("Python脚本执行失败，退出值：" + exitValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputList.add("Python脚本执行异常：" + e.getMessage());
        }
        return outputList;
    }

}
