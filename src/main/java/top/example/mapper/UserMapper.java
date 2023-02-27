package top.example.mapper;

import top.example.entity.User;
import top.example.utils.MyBatisUtils;

public class UserMapper {

    public User selectByUserName(String userName){
        User user = (User) MyBatisUtils.executeQuery(sqlSession -> {
             return sqlSession.selectOne("top.example.mapper.UserMapper.selectByUserName",userName);
        });
        return user;
    }
}
