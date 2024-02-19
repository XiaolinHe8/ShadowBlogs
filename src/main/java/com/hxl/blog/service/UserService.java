package com.hxl.blog.service;

import com.hxl.blog.pojo.User;

public interface UserService {
    User checkUser(String username,String password);
}
