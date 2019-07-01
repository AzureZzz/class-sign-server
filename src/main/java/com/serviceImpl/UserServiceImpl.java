package com.serviceImpl;

import com.mapper.UserMapper;
import com.pojo.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUserScholle(User user) {


        return userMapper.getUserSchool(user);
    }

    @Override
    public String getUserNo(User user) {
        return userMapper.getUserNo(user);
    }

    @Override
    public User getUserInfo(User user) {

        return userMapper.getUserInfo(user);
    }

    @Override
    public String getUserName(Long userId) {

        return userMapper.getUserName(userId);
    }

    @Override
    public Integer getUserType(Long userId) {

        return userMapper.getUserType(userId);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }
}
