package top.piao888.user.service;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.piao888.user.thrift.user.UserInfo;
import top.piao888.user.thrift.user.UserService;
import top.piao888.user.mapper.UserMapper;
@Service
public class UserSeriveImpl implements UserService.Iface {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return userMapper.getUserByName(username);
    }

    @Override
    public void regiserUser(UserInfo u) throws TException {
        userMapper.registerUser(u);
    }
}
