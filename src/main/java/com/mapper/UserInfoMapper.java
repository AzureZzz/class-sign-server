package com.mapper;


import com.pojo.User;
import com.pojo.UserInfo;
import com.pojo.UserInfoPackage;

public interface UserInfoMapper {
    int insert(UserInfo record);

    /**
     * 上传用户信息
     *
     * @param record
     * @return
     */
    int insertSelective(UserInfo record);


    /**
     * 根据用户id 获取用户的个人信息
     *
     * @param user
     * @return
     */
    UserInfoPackage getUserInfoByUserId(User user);


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