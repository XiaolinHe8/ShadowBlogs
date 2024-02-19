package com.hxl.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hxl.blog.dao.BlogMapper;
import com.hxl.blog.dao.TagMapper;
import com.hxl.blog.dao.TypeMapper;
import com.hxl.blog.myException.NotFoundException;
import com.hxl.blog.pojo.Blog;
import com.hxl.blog.pojo.Type;
import com.hxl.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Override
    public Type insertType(Type type) {
        int insert = typeMapper.insert(type);
        return type;
    }

    @Override
    public Type getType(Integer id) {
        return typeMapper.selectById(id);
    }


    @Override
    public Type getTypeByName(String name) {
        return typeMapper.selectOne(new QueryWrapper<Type>().eq("name",name));
    }

    @Override
    public List<Type> listType() {
        return typeMapper.selectList(null);
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        List<Type> types = typeMapper.listTop(size);
        for(int i=0;i<types.size();i++){
            types.get(i).setNumber(blogMapper
                    .selectCount(new QueryWrapper<Blog>()
                            .eq("type_id",types.get(i).getId())));
        }
        return types;
    }

    @Override
    public Type updateType(Integer id, Type type) {
        Type type1 = typeMapper.selectById(id);
        if(type1 == null){
            throw  new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,type1);
        typeMapper.insert(type1);
        return type1;
    }

    @Override
    public void deleteType(Integer id) {
        typeMapper.deleteById(id);

    }

    @Override
    public Page<Type> listType(Page<Type> page) {
        if(page == null){
            page = new Page<Type>(1,10);
        }
        Page<Type> res = typeMapper.selectPage(page, new QueryWrapper<Type>().orderByDesc("id"));
        return res;
    }
}
