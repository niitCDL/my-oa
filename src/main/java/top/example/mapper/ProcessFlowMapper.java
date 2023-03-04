package top.example.mapper;

import org.apache.ibatis.annotations.Param;
import top.example.entity.ProcessFlow;

import java.util.List;

public interface ProcessFlowMapper {
    Integer insert(@Param("pflow") ProcessFlow processFlow);

    void update(@Param("flow") ProcessFlow processFlow);
    List<ProcessFlow> selectByFormId(Long formId);
}
