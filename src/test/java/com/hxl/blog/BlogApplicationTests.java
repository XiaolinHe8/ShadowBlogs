package com.hxl.blog;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.dao.TypeMapper;
import com.hxl.blog.dao.UserMapper;
import com.hxl.blog.pojo.Type;
import com.hxl.blog.pojo.User;
import com.hxl.blog.service.Impl.TypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TypeMapper typeMapper;
    @Test
    void contextLoads() {
        Page<Type> page = new Page<Type>();
        page.setCurrent(1);
        page.setSize(10);
        Page<Type> userPage = typeMapper.selectPage(page,null);
        JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("data", userPage);
        System.out.println(jsonObject);
    }

}
