package top.example.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import top.example.entity.LeaveForm;

import java.util.List;
import java.util.Map;

public interface LeaveFormMapper {
    Integer insert(@Param("form") LeaveForm leaveForm);
    Long getMaxId();

    void update(@Param("form") LeaveForm form);
    LeaveForm selectById(Long formId);

    @MapKey("")
    List<Map<String,Object>> selectByParams(@Param("state")String state,@Param("operatorId")Long operatorId);
}
