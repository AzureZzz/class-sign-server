package com.mapper;


import com.pojo.UserToken;
import org.apache.ibatis.annotations.Param;


public interface UserTokenMapper {

    /**
     * 注册好的账户 写入 token
     *
     * @param record
     * @return
     */
    int insert(UserToken record);

    int insertSelective(UserToken record);

    /**
     * 根据用户Id号获取 token
     *
     * @param userId
     * @return
     */
    String getUserToken(@Param("userId") int userId);


    /**
     * 更新token
     *
     * @return
     */
    int updateUserToken(UserToken userToken);
}