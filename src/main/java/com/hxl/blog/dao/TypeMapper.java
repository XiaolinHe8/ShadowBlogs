package com.hxl.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hxl.blog.pojo.Type;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeMapper extends BaseMapper<Type> {

    @Select("select id,name from type where id in (select type_id from (select type_id from blog group by type_id order by count(*) desc limit #{size}) as t)")
    public List<Type> listTop(Integer size);

}
