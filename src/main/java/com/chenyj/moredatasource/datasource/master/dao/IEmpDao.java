package com.chenyj.moredatasource.datasource.master.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenyj.moredatasource.datasource.entity.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author chenyj
 * @Description
 * @Date create by 2020/3/29 0029
 * 陈银杰专属测试
 */
public interface IEmpDao  extends BaseMapper<Emp> {

    List<Emp> queryAll();

}
