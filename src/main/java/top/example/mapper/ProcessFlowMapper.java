package top.example.mapper;

import org.apache.ibatis.annotations.Param;
import top.example.entity.ProcessFlow;

public interface ProcessFlowMapper {
    Integer insert(@Param("pflow") ProcessFlow processFlow);
}
