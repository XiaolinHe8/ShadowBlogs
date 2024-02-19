package com.hxl.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Create By hxl on 2021/3/21
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "url")
public class Configure {
    private String imageUrl;
    private String visImageUrl;
}
