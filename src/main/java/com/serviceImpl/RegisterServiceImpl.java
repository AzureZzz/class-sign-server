package com.serviceImpl;


import com.Jwt.Jwt;
import com.mapper.UserInfoMapper;
import com.mapper.UserMapper;
import com.mapper.UserTokenMapper;
import com.pojo.User;
import com.pojo.UserInfo;
import com.pojo.UserToken;
import com.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

    @Autowired(required = true)
    private UserMapper userMapper;

    @Autowired(required = true)
    private UserTokenMapper userTokenMapper;


    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public int userRegister(User user) {

        UserToken userToken = new UserToken();

        int record = userMapper.getUserRecord(user);
        if (record > 0) {
            //已经注册过，返回0

            return 999;

        } else {
            //写入注册信息
            record = userMapper.insertSelective(user);
            if (record == 1) {
                //获取已注册好的用户id号
                int uId = userMapper.gerUserId(user);
                user.setUserId((long) uId);
                //写入token //中转停留时间10分钟
                String token = Jwt.sign(user, 1000 * 60 * 60 * 24 * 7);

                userToken.setToken(token);
                userToken.setUserId((long) uId);
                record = userTokenMapper.insert(userToken);
                //插入一条用户详细信息
                UserInfo userInfo = new UserInfo();
                userInfo.setUserHeadSrc("timg.jpg");
                record = userInfoMapper.insertSelective(userInfo);
                if (record == 1) {
                    userToken.setToken(null);
                    return 1;
                } else {
//                    throw new Exception("出现未知错误，注册失败");
                    return 0;
                }

            } else {

                return 0;
            }
        }

    }

    @Override
    public int getUserRecord(User user) {
        return userMapper.getUserRecord(user);
    }

    @Override
    public int getUerId(User user) {


        return userMapper.gerUserId(user);
    }


}
