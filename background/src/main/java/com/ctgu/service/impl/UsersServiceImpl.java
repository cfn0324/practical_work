package com.ctgu.service.impl;

import com.ctgu.entity.Users;
import com.ctgu.mapper.UsersMapper;
import com.ctgu.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cfn
 * @since 2024-08-18
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
