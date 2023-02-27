package top.example.service;

import top.example.entity.User;
import top.example.mapper.UserMapper;
import top.example.service.exception.LoginException;
import top.example.utils.Md5Utils;

public class UserService {

    private UserMapper userMapper = new UserMapper();

    public User login(String username, String password) {
        User user = userMapper.selectByUserName(username);
        if (user == null) {
            throw new LoginException("用户名不存在");
        }
        //对password进行 md5 和 salt 加密 得到密文
        String md5Password = Md5Utils.md5Digest(password, user.getSalt());
        if (!md5Password.equals(user.getPassword())){
            throw new LoginException("密码错误");
        }
        return user;
    }
}
