package com.serviceImpl;


import com.mapper.UserMapper;
import com.mapper.UserTokenMapper;
import com.pojo.User;
import com.pojo.UserToken;
import com.service.LoginService;
import com.utils.TokenDecipherting;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired(required = true)
    private UserMapper userMapper;

    @Autowired(required = true)
    private UserTokenMapper userTokenMapper;
    @Autowired
    private TokenDecipherting tokenDecipherting;


    @Override
    public UserToken userLogin(User user) {
        UserToken userToken = new UserToken();
        //密码加密
        String username = user.getUserPhone();
        String password = user.getUserPassword();
        //采用盐值加密
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        SimpleHash passMd5 = new SimpleHash("MD5", password, credentialsSalt, 1024);
        user.setUserPassword(passMd5.toHex());
        //对比数据库数据
        int userId = 0;
        try {
            userId = userMapper.userLogin(user);
            if (userId != 0) {
                //查询token
                String token = userTokenMapper.getUserToken(userId);

                //先解密，再加密更新token记录
                User user1 = tokenDecipherting.tokenDecipherting(token);
                //再查询，将新的返回
                token = userTokenMapper.getUserToken(userId);

                userToken.setToken(token);
                userToken.setUserId((long) userId);
                return userToken;

            } else {

                return userToken;
            }
        } catch (Exception e) {

            return null;
        }


    }
}
