package com.service;

import com.pojo.User;
import com.pojo.UserInfo;
import com.pojo.UserInfoPackage;

public interface UserInfoServie {


    /**
     * 查询用户的个人信息
     *
     * @param user
     * @return
     */
    UserInfoPackage getUserInfo(User user);


    /**
     * 修改个人信息
     *
     * @param userInfo
     * @return
     */
    int updateUserInfo(UserInfo userInfo);


    /**
     * 根据用户id 查询是否设置过个人信息
     *
     * @param user
     * @return
     */
    int getUserInfoRec(User user);


    /**
     * 上传用户的个人信息
     *
     * @param userInfo
     * @return
     */
    int uploadUserInfo(UserInfo userInfo);

    /**
     * 获取用户头像链接
     *
     * @param userId
     * @return
     */
    String getUserHeadSrc(Long userId);


    /**
     * 选择性修改用户的个人信息
     *
     * @param userInfo
     * @return
     */
    int updateSelective(UserInfo userInfo);
}
