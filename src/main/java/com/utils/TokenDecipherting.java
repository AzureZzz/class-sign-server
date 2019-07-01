package com.utils;

import com.Jwt.Jwt;
import com.pojo.User;
import com.pojo.UserToken;
import com.serviceImpl.RegisterServiceImpl;
import com.serviceImpl.UserTokenServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenDecipherting {


    @Autowired
    private RegisterServiceImpl registerService;

    @Autowired
    private UserTokenServerImpl updataUidTokrn;


    public User tokenDecipherting(String token) {
        int uid = 0;
        UserToken userToken = new UserToken();

        User user = new User();

        user = Jwt.unsign(token, User.class);

        if (user != null) {
            //System.out.println("TOKEN解密 : "+user.getUserName());
            // System.out.println("TOKEN解密 : "+user.getUserPhone());
//            System.out.println("username :" + loginRegister.getUsername());
            //重置token 返回给用户
            String newToken = Jwt.sign(user, 1000 * 60 * 60 * 24 * 7);   //中转停留时间7天
            //还需要将新的token更新到数据库保存。。。
            uid = registerService.getUerId(user);
            //uid = userControllerService.getUserId(loginRegister.getUsername());
            if (uid != 0) {
                //将uid 和 token 存入数据库
                userToken.setUserId((long) uid);
                userToken.setToken(newToken);
                //更新token
//                int updataResult = userControllerService.updataUidTokrn(userToken);
                int updataResult = updataUidTokrn.updateUserToken(userToken);
                if (updataResult == 1) {
                    user.setToken(token);
                    user.setUserId((long) uid);
                    return user;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}




