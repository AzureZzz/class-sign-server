package com.service;

import com.pojo.UserToken;

public interface UserTokenServer {

    /**
     * 更新用户token
     *
     * @param userToken
     * @return
     */
    int updateUserToken(UserToken userToken);
}
