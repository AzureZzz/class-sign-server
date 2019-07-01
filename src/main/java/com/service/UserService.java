package com.service;

import com.pojo.User;

public interface UserService {

    /**
     * 获取用户所在学校
     *
     * @param user
     * @return
     */
    String getUserScholle(User user);


    /**
     * 获取用户学号/工号
     *
     * @param user
     * @return
     */
    String getUserNo(User user);


    /**
     * 获取用户个人信息
     *
     * @param user
     * @return
     */
    User getUserInfo(User user);


    /**
     * 根据用户id 获取用户姓名
     *
     * @param userId
     * @return
     */
    String getUserName(Long userId);


    /**
     * 根据用户id 获取用户身份类型
     *
     * @param userId
     * @return
     */
    Integer getUserType(Long userId);

    //更新用户信息
    int updateByPrimaryKey(User record);
}
