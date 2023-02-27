package top.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    private Long employeeId;
    private String name;
    private Long departmentId ;
    private String departmentName ;
    private String title;
    private Integer level;
    private String path;

}
