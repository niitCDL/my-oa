package top.example.service;

import top.example.entity.Notice;
import top.example.mapper.NoticeMapper;
import top.example.utils.MyBatisUtils;

import java.util.List;

public class NoticeService {

    NoticeMapper noticeMapper = MyBatisUtils.getSqlSession().getMapper(NoticeMapper.class);

    public List<Notice> getNoticeList(Long employeeId){
        return noticeMapper.selectByReceiverId(employeeId)  ;
    }
}
