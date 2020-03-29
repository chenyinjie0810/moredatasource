package com.chenyj.moredatasource.datasource.master.service.impl;

import com.chenyj.moredatasource.datasource.entity.Emp;
import com.chenyj.moredatasource.datasource.master.dao.IEmpDao;
import com.chenyj.moredatasource.datasource.master.service.IEmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author chenyj
 * @Description
 * @Date create by 2020/3/29 0029
 * 陈银杰专属测试
 */
@Service
@Transactional(value = "masterDataSourceTransactionManager",rollbackFor = Exception.class)
public class EmpServiceImpl implements IEmpService {

    @Autowired
    private IEmpDao empDao;

    @Override
    public int save(Emp emp) {
        return empDao.insert(emp);
    }
}
