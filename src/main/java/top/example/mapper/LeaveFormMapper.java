package top.example.mapper;

import org.apache.ibatis.annotations.Param;
import top.example.entity.LeaveForm;

public interface LeaveFormMapper {
    Integer insert(@Param("form") LeaveForm leaveForm);
    Long getMaxId();
}
