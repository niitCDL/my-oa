package top.example.mapper;

import org.apache.ibatis.annotations.Param;
import top.example.entity.Notice;

import java.util.List;

public interface NoticeMapper {
    int insert(@Param("notice") Notice notice);
    List<Notice> selectByReceiverId(Long receiverId);
}
