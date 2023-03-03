package top.example.mapper;

import org.apache.ibatis.annotations.Param;
import top.example.entity.Employee;

public interface EmployeeMapper {

    Employee selectById(Long id);

    String getDeptNameById(Long id);

    Employee getManagerByEmp(@Param("emp") Employee employee);


}
