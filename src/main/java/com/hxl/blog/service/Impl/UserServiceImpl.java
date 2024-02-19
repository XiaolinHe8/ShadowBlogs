package com.hxl.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxl.blog.dao.UserMapper;
import com.hxl.blog.pojo.User;
import com.hxl.blog.service.UserService;
import com.hxl.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper = null;
    @Override
    public User checkUser(String username,String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username",username);
        User res = userMapper.selectOne(new QueryWrapper<User>().allEq(map));
        if(res == null)
            return null;
        if(MD5Utils.convertMD52(password).equals(MD5Utils.convertMD52(res.getPassword())))
            return res;
        return null;
    }
}
