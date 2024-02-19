package com.hxl.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Img {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String localImg;
    private String virtualImg;
    private Integer tagId;
    private String name;
    private String description;
}
