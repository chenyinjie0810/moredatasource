package com.chenyj.moredatasource.datasource.controller;

import com.chenyj.moredatasource.datasource.master.dao.IEmpDao;
import com.chenyj.moredatasource.datasource.entity.Emp;
import com.chenyj.moredatasource.datasource.master.service.IEmpService;
import com.chenyj.moredatasource.datasource.slave.dao.IEmpDaoSlave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author chenyj
 * @Description
 * @Date create by 2020/3/29 0029
 * 陈银杰专属测试
 */
@RestController
public class EmpController {

    @Autowired
    private IEmpService empService;
    @Autowired
    private IEmpDaoSlave empDaoSlave;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    @Qualifier("redisTokenTemplate")
    private RedisTemplate redisTokenTemplate;

    @PostMapping("/master")
    public String save(@RequestBody Emp emp){
        int c=empService.save(emp);
        return ""+c;
    }

    @GetMapping("/slave")
    public List slave() {
        List<Emp> emps = empDaoSlave.selectList(null);
        redisTemplate.opsForValue().set("chen","天下风云出我辈");
        redisTokenTemplate.opsForValue().set("chen","一如江湖岁月催");

        String value1= (String) redisTemplate.opsForValue().get("chen");
        System.out.printf(value1);
        String value2= (String) redisTokenTemplate.opsForValue().get("chen");
        System.out.println(value2);
        return emps;
    }
}
