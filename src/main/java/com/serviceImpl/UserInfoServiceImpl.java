package com.serviceImpl;

import com.mapper.UserInfoMapper;
import com.mapper.UserMapper;
import com.pojo.User;
import com.pojo.UserInfo;
import com.pojo.UserInfoPackage;
import com.service.UserInfoServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userInfoServie")
public class UserInfoServiceImpl implements UserInfoServie {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserInfoPackage getUserInfo(User user) {
        //获取用户Id
        int userId = userMapper.gerUserId(user);
        user.setUserId((long) userId);
        System.out.println("用户id " + user.getUserId());

        UserInfoPackage userInfoPackage = userInfoMapper.getUserInfoByUserId(user);
        return userInfoPackage;
    }

    @Override
    public int updateUserInfo(UserInfo userInfo) {

        return userInfoMapper.updateUserInfo(userInfo);
    }

    @Override
    public int getUserInfoRec(User user) {
        return userInfoMapper.getUserInfoRec(user);
    }

    @Override
    public int uploadUserInfo(UserInfo userInfo) {

        return userInfoMapper.insertSelective(userInfo);
    }

    @Override
    public String getUserHeadSrc(Long userId) {


        return userInfoMapper.getUserHeadSrc(userId);
    }

    @Override
    public int updateSelective(UserInfo userInfo) {
        return userInfoMapper.updateSelective(userInfo);
    }
}
