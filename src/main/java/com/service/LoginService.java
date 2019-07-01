package com.service;


import com.pojo.User;
import com.pojo.UserToken;

public interface LoginService {


    /**
     * 用户登陆
     *
     * @param user
     * @return
     */
    UserToken userLogin(User user);

}
