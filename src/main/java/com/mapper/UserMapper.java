package com.mapper;

import com.pojo.User;


public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    /**
     * 写入 注册记录
     *
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 根据用户id 获取 个人信息
     *
     * @param userId
     * @return
     */
    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    //更新用户信息
    int updateByPrimaryKey(User record);


    /**
     * 查询用户注册记录，返回查询结果
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
    int gerUserId(User user);


    /**
     * 用户登陆
     *
     * @param user
     * @return
     */
    int userLogin(User user);


    /**
     * 获取用户所在学校
     *
     * @param user
     * @return
     */
    String getUserSchool(User user);

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


}