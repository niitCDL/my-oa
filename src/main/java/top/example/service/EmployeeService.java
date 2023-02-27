package top.example.service;

import top.example.entity.Employee;
import top.example.mapper.EmployeeMapper;
import top.example.utils.MyBatisUtils;

public class EmployeeService {

    EmployeeMapper employeeMapper = MyBatisUtils.getSqlSession().getMapper(EmployeeMapper.class);


    public Employee getEmp(Long id){
        Employee employee = employeeMapper.selectById(id);
        employee.setDepartmentName(employeeMapper.getDeptNameById(employee.getDepartmentId()));
        return employee;
    }
}
