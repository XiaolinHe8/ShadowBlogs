package com.hxl.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.pojo.Type;

import java.util.List;

public interface TypeService {
    Type insertType(Type type);

    Type getType(Integer id);

    Type getTypeByName(String name);

    Page<Type> listType(Page<Type> page);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    Type updateType(Integer id,Type type);

    void deleteType(Integer id);

}
