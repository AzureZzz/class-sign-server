package com.mapper;


import com.pojo.MyClass;
import com.pojo.MyJoin;

import java.util.List;

public interface MyJoinMapper {

    /**
     * 学生加入课堂
     *
     * @param record
     * @return
     */
    int insert(MyJoin record);

    int insertSelective(MyJoin record);

    /**
     * 根据userId 获取所有相应的课堂id号
     *
     * @param userId
     * @return
     */
    List<Long> getClassIdByUserId(Long userId);


    /**
     * 根据课堂Id号 获取该课堂下的所有学生id号
     */
    List<Long> getUserId(MyClass myClass);


    /**
     * 根据课堂id删除所有加课记录
     *
     * @param classId
     * @return
     */
    int disMissJoinByClassId(Long classId);


    /**
     * 根据 课堂ID 和 用户ID  删除一条记录
     *
     * @param myClass
     * @return
     */
    int deleteJoinInfo(MyClass myClass);


    /**
     *  根据课堂ID 和 用户ID 查询加课记录
     * @param myClass
     * @return
     */
    int getJoinRec(MyClass myClass);


}