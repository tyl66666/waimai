package com.tyl.waimai.service.impl;

import com.tyl.waimai.entity.Employee;
import com.tyl.waimai.mapper.EmployeeMapper;
import com.tyl.waimai.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author tyl
 * @since 2023-06-01
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
