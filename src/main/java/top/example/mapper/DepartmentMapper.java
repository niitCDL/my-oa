package top.example.mapper;

import top.example.entity.Department;

public interface DepartmentMapper {
    Department selectById(Long departmentId);
}
