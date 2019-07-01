package com.service;


import com.pojo.User;

public interface RegisterService {


    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    int userRegister(User user) throws Exception;

    /**
     * 获取用户注册记录
     *
     * @param user
     * @return
     */
    int getUserRecord(User user);

    /**
     * 获取用户id号
     *
     * @param user
     * @return
     */
    int getUerId(User user);


}
