package top.example.mapper;

import top.example.entity.Employee;

public interface EmployeeMapper {

    Employee selectById(Long id);

    String getDeptNameById(Long id);
}
