package com.service;


import com.pojo.MyClass;
import com.pojo.MyClassTeaInfo;
import com.pojo.MyJoin;

import java.util.List;

public interface MyJoinService {

    /**
     * 根据用户id 获取所有参加的课堂id
     *
     * @param userId
     * @return
     */
    List<Integer> joinClassId(int userId);


    /**
     * 获取用户参加的所有课堂列表
     *
     * @param userId
     * @return
     */
    List<MyClassTeaInfo> joinClassList(long userId);


    /**
     * 根据课堂Id号 获取该课堂下的所有学生id号
     */
    List<Long> getUserId(MyClass myClass);


    /**
     * 学生加入课堂
     *
     * @param record
     * @return
     */
    int joinClass(MyJoin record);


    /**
     * 学生退出课堂
     *
     * @param myClass
     * @return
     */
    int exitClass(MyClass myClass);


    /**
     *  根据课堂ID 和 用户ID 查询加课记录
     * @param myClass
     * @return
     */
    int getJoinRec(MyClass myClass);
}
