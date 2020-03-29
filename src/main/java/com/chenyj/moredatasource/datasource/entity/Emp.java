package com.chenyj.moredatasource.datasource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author chenyj
 * @Description
 * @Date create by 2020/3/29 0029
 * 陈银杰专属测试
 */
@Data
public class Emp {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer age;
}

