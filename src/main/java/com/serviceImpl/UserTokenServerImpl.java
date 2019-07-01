package com.serviceImpl;

import com.mapper.UserTokenMapper;
import com.pojo.UserToken;
import com.service.UserTokenServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userTokenServer")
public class UserTokenServerImpl implements UserTokenServer {

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Override
    public int updateUserToken(UserToken userToken) {


        return userTokenMapper.updateUserToken(userToken);
    }
}
