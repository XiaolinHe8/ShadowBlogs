package com.hxl.blog.pojo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class BlogTag {
    private Integer blogId;
    private Integer tagId;
}
